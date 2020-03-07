package id.rizmaulana.covid19.ui.detail

import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.repository.AppRepository
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
    private val appRepository: AppRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    private var detailList: List<CovidDetail> = listOf()
    private var filteredDetailList: List<CovidDetail> = listOf()
    val detailListLiveData = MutableLiveData<List<CovidDetail>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String>()

    fun findLocation(keyword: String) {
        if (keyword.isEmpty()) {
            detailListLiveData.postValue(detailList)
        } else {
            filteredDetailList = detailList.filter {
                (it.provinceState?.contains(
                    keyword,
                    true
                ) ?: false || it.countryRegion?.contains(keyword, true) ?: false)
            }
            detailListLiveData.postValue(filteredDetailList)
        }
    }

    fun getDetail(caseType: Int) {
        when (caseType) {
            CaseType.RECOVERED -> appRepository.recovered()
            CaseType.DEATHS -> appRepository.confirmed()
            else -> appRepository.confirmed()
        }.subscribeOn(schedulerProvider.ui())
            .doOnSubscribe {
                val cache = when (caseType) {
                    CaseType.RECOVERED -> appRepository.getCacheRecovered()
                    CaseType.DEATHS -> appRepository.getCacheDeath()
                    else -> appRepository.getCacheConfirmed()
                }
                if (cache == null) loading.postValue(true) else {
                    detailList = cache
                    detailListLiveData.postValue(detailList)
                }
            }
            .doFinally {
                loading.postValue(false)
            }
            .subscribe({
                detailList = it
                detailListLiveData.postValue(detailList)
            }, {
                it.printStackTrace()
                errorMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

}