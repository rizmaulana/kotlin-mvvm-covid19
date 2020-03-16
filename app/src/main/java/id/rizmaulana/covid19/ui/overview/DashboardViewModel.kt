package id.rizmaulana.covid19.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.mapper.CovidDailyDataMapper
import id.rizmaulana.covid19.data.mapper.CovidOverviewDataMapper
import id.rizmaulana.covid19.data.mapper.CovidPinnedDataMapper
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.data.repository.Result
import id.rizmaulana.covid19.ui.adapter.viewholders.ErrorStateItem
import id.rizmaulana.covid19.ui.adapter.viewholders.LoadingStateItem
import id.rizmaulana.covid19.ui.adapter.viewholders.TextItem
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.Function3

/**
 * rizmaulana
 */
class DashboardViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _liveItems = MutableLiveData<List<BaseViewItem>>()
    val items: LiveData<List<BaseViewItem>>
        get() = _liveItems

    private fun showLoadingState(){
        _loading.value = true
        if(_liveItems.value?.isEmpty() == null ||
                _liveItems.value?.firstOrNull { it is ErrorStateItem } != null){
            _liveItems.value = listOf(LoadingStateItem())
        }
    }

    private fun showErrorState(throwable: Throwable){
        _loading.value = false
        if(_liveItems.value?.isEmpty() == null ||
            _liveItems.value?.firstOrNull { it is ErrorStateItem || it is LoadingStateItem } != null){
            _liveItems.value = listOf(handleThrowable(throwable))
        }
    }

    fun loadDashboard() {
        showLoadingState()

        val overviewObservable = appRepository.overview()
            .observeOn(schedulerProvider.io()) //all stream below will be manage on io thread

        val dailyObservable = appRepository.daily()
            .observeOn(schedulerProvider.io())

        val pinnedObservable = appRepository.getPinnedCountry()
            .observeOn(schedulerProvider.io())

        Observable.combineLatest(
                overviewObservable,
                dailyObservable,
                pinnedObservable,
                Function3<Result<CovidOverview>,
                        Result<List<CovidDaily>>,
                        Result<CovidDetail>,
                        Pair<List<BaseViewItem>, Throwable?>> { overview, daily, pinned ->

                    val items: MutableList<BaseViewItem> = mutableListOf()
                    var currentThrowable: Throwable? = null

                    with(overview){
                        items.add(CovidOverviewDataMapper.transform(data))
                        error?.let { currentThrowable = it }
                    }

                    with(pinned){
                        CovidPinnedDataMapper.transform(data)?.let {
                            items.add(it)
                        }
                        error?.let { currentThrowable = it }
                    }

                    with(daily){
                        val dailies = CovidDailyDataMapper.transform(data)
                        if(dailies.isNotEmpty()) {
                                items.add(TextItem(R.string.daily_updates, R.string.show_graph))
                                items.addAll(dailies)
                        }
                        error?.let { currentThrowable = it }
                    }

                    return@Function3 items.toList() to currentThrowable
                })
        .observeOn(schedulerProvider.ui()) //go back to ui thread
        .subscribe({ (result, throwable) ->
            _liveItems.postValue(result)

            //For now only check if there is a throwable
            if(throwable != null) _toastMessage.value = Constant.ERROR_MESSAGE
            _loading.value = false
        }, {
            _toastMessage.value = Constant.ERROR_MESSAGE
            showErrorState(it)
        }).addTo(compositeDisposable)
    }
}