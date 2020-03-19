package id.rizmaulana.covid19.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider

/**
 * rizmaulana 2020-02-24.
 */
class DetailViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

    private var detailList = listOf<CovidDetail>()
    private var filteredDetailList = listOf<CovidDetail>()

    private val _detailListLiveData = MutableLiveData<List<CovidDetail>>()
    val detailListLiveData: LiveData<List<CovidDetail>>
        get() = _detailListLiveData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    val errorMessage = SingleLiveEvent<String>()

    fun findLocation(keyword: String) {
        if (keyword.isEmpty()) {
            _detailListLiveData.postValue(detailList)
        } else {
            filteredDetailList = detailList.filter {
                (it.provinceState?.contains(
                    keyword,
                    true
                ) ?: false || it.countryRegion?.contains(keyword, true) ?: false)
            }
            _detailListLiveData.postValue(filteredDetailList)
        }
    }

    fun getDetail(caseType: Int) {
        when (caseType) {
            CaseType.RECOVERED -> appRepository.recovered()
            CaseType.DEATHS -> appRepository.deaths()
            CaseType.CONFIRMED -> appRepository.confirmed()
            else -> appRepository.confirmed()
        }.subscribeOn(schedulerProvider.ui())
            .doOnSubscribe {
                val cache = when (caseType) {
                    CaseType.RECOVERED -> appRepository.getCacheRecovered()
                    CaseType.DEATHS -> appRepository.getCacheDeath()
                    CaseType.CONFIRMED -> appRepository.getCacheConfirmed()
                    else -> appRepository.getCacheFull()
                }
                if (cache == null) _loading.postValue(true) else {
                    detailList = cache
                    _detailListLiveData.postValue(detailList)
                }
            }
            .doFinally { _loading.postValue(false) }
            .subscribe({
                detailList = it
                _detailListLiveData.postValue(detailList)
            }, {
                it.printStackTrace()
                errorMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

    fun putPinnedRegion(data: CovidDetail) {
        appRepository.putPinnedRegion(data)
            .subscribeOn(schedulerProvider.ui())
            .subscribe({
                errorMessage.postValue("Success")
            }, {
                errorMessage.postValue(it.message)
            })
            .addTo(compositeDisposable)
    }

}