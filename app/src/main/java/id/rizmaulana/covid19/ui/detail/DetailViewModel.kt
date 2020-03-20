package id.rizmaulana.covid19.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.mapper.CovidDetailDataMapper
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.data.repository.Result
import id.rizmaulana.covid19.ui.adapter.viewholders.LoadingStateItem
import id.rizmaulana.covid19.ui.adapter.viewholders.LocationItem
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * rizmaulana 2020-02-24.
 */
class DetailViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var detailList = listOf<CovidDetail>()
    private var caseType: Int = CaseType.FULL

    private val _detailListViewItems = MutableLiveData<List<BaseViewItem>>()
    val detailListViewItems: LiveData<List<BaseViewItem>>
        get() = _detailListViewItems

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val errorMessage = SingleLiveEvent<String>()

    fun findLocation(keyword: String) {
        appRepository.getPinnedCountry()
            .observeOn(schedulerProvider.ui())
            .map {
                val transformedList = CovidDetailDataMapper.transform(detailList, caseType)

                val filtered = if(keyword.isNotEmpty()) transformedList.filter {
                    (it.provinceState?.contains(
                        keyword,
                        true
                    ) ?: false || it.countryRegion.contains(keyword, true))
                }.toMutableList() else transformedList.toMutableList()

                it.data?.let { pin ->
                    val position = filtered.indexOfFirst { it.compositeKey() == pin.compositeKey }
                    if(position != -1) filtered.set(position, filtered.get(position).copy(isPinned = true))
                }

                return@map filtered
            }
            .subscribe({
                _detailListViewItems.postValue(it)
            }, {

            }).addTo(compositeDisposable)
    }

    fun getDetail(caseType: Int) {
        this.caseType = caseType
        _loading.value = true
        _detailListViewItems.value = listOf(LoadingStateItem())

        val pinnedObservable = appRepository.getPinnedCountry()
        val casesObservable = when (caseType) {
            CaseType.RECOVERED -> appRepository.recovered()
            CaseType.DEATHS -> appRepository.deaths()
            CaseType.CONFIRMED -> appRepository.confirmed()
            else -> appRepository.fullStats()
        }.observeOn(schedulerProvider.io())
            .map {
                detailList = it
                CovidDetailDataMapper.transform(it, caseType)
            }
            .observeOn(schedulerProvider.ui())

        Observable.zip(casesObservable, pinnedObservable, BiFunction<
                    List<LocationItem>?,
                    Result<CovidDetail>?,
                    List<BaseViewItem>> { cases, pinned ->
                val items: MutableList<BaseViewItem> = mutableListOf()
                val casesMutable = cases.toMutableList()
                pinned.data?.let { pin ->
                    val position = cases.indexOfFirst { it.compositeKey() == pin.compositeKey }
                    if(position != -1) casesMutable.set(position, cases.get(position).copy(isPinned = true))
                }

                items.addAll(casesMutable)
                return@BiFunction items.toList()
            })
            .doFinally { _loading.postValue(false) }
            .subscribe({
                _detailListViewItems.postValue(it)
            }, {
                it.printStackTrace()
                errorMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

    fun putPinnedRegion(key: String) {
        detailList.firstOrNull { it.provinceState + it.countryRegion == key }?.let {
            appRepository.putPinnedRegion(it)
                .subscribeOn(schedulerProvider.ui())
                .subscribe({
                    errorMessage.postValue("Success")
                }, {
                    errorMessage.postValue(it.message)
                })
                .addTo(compositeDisposable)
        }
    }

}