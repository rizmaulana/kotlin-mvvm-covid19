package id.rizmaulana.covid19.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.mapper.CovidDailyDataMapper
import id.rizmaulana.covid19.data.mapper.CovidOverviewDataMapper
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.adapter.viewholders.OverviewItem
import id.rizmaulana.covid19.ui.adapter.viewholders.TextItem
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

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

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _liveItems = MutableLiveData<List<BaseViewItem>>()
    val items: LiveData<List<BaseViewItem>>
        get() = _liveItems

    fun loadOverviewAndDaily() {
        val overviewObservable = appRepository.overview()
            .observeOn(schedulerProvider.io()) //all stream below will be manage on io thread
            .map { CovidOverviewDataMapper.transform(it) }

        val dailyObservable = appRepository.daily()
            .observeOn(schedulerProvider.io()) //all stream below will be manage on io thread
            .map { CovidDailyDataMapper.transform(it) }

        Observable.combineLatest(overviewObservable, dailyObservable, BiFunction<OverviewItem?, List<BaseViewItem>?, List<BaseViewItem>> { overview, daily ->
            val items: MutableList<BaseViewItem> = mutableListOf()

            //GENERATE SCREEN POSITION HERE
            items.add(overview)
            items.add(TextItem("Daily Updates"))
            items.addAll(daily)

            return@BiFunction items.toList()
        })
        .observeOn(schedulerProvider.ui()) //go back to ui thread
        .subscribe({
            _liveItems.postValue(it)
        }, {
            _errorMessage.postValue(Constant.ERROR_MESSAGE)
        }).addTo(compositeDisposable)
    }
}