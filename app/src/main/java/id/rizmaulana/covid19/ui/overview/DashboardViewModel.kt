package id.rizmaulana.covid19.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.mapper.CovidDailyDataMapper
import id.rizmaulana.covid19.data.mapper.CovidOverviewDataMapper
import id.rizmaulana.covid19.data.mapper.CovidPinnedDataMapper
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.adapter.viewholders.OverviewItem
import id.rizmaulana.covid19.ui.adapter.viewholders.PinnedItem
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

    fun loadDashboard() {
        val overviewObservable = appRepository.overview()
            .observeOn(schedulerProvider.io()) //all stream below will be manage on io thread
            .map { CovidOverviewDataMapper.transform(it) }
            .switchIfEmpty(Observable.just(listOf())) //handle if observable empty

        val dailyObservable = appRepository.daily()
            .observeOn(schedulerProvider.io())
            .map { CovidDailyDataMapper.transform(it) }
            .switchIfEmpty(Observable.just(listOf()))

        val pinnedObservable = appRepository.getPinnedCountry()
            .observeOn(schedulerProvider.io())
            .map { CovidPinnedDataMapper.transform(it) }
            .switchIfEmpty(Observable.just(listOf()))

        Observable.combineLatest(
                overviewObservable,
                dailyObservable,
                pinnedObservable,
                Function3<List<OverviewItem>,
                        List<DailyItem>,
                        List<PinnedItem>,
                        List<BaseViewItem>> { overviews, dailies, pinned ->

                    val items: MutableList<BaseViewItem> = mutableListOf()

                    //GENERATE SCREEN POSITION HERE
                    if(overviews.isNotEmpty()) items.addAll(overviews)
                    if(pinned.isNotEmpty()) items.addAll(pinned)

                    if(dailies.isNotEmpty()) {
                        items.add(TextItem("Daily Updates"))
                        items.addAll(dailies)
                    }

                    return@Function3 items.toList()
                })
        .observeOn(schedulerProvider.ui()) //go back to ui thread
        .subscribe({
            _liveItems.postValue(it)
        }, {
            _toastMessage.postValue(Constant.ERROR_MESSAGE)
        }).addTo(compositeDisposable)
    }
}