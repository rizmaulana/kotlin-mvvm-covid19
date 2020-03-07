package id.rizmaulana.covid19.ui.overview

import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.repository.AppRepository
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider

/**
 * rizmaulana
 */
class DashboardViewModel(
    private val appRepository: AppRepository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    val loading = MutableLiveData<Boolean>()
    val errorMessage = SingleLiveEvent<String>()
    val dailyListData = MutableLiveData<List<CovidDaily>>()
    val overviewData = MutableLiveData<CovidOverview>()

    fun getOverview() {
        appRepository.overview()
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {
                val cache = appRepository.getCacheOverview()
                if (cache == null) loading.postValue(true) else {
                    overviewData.postValue(cache)
                }
            }
            .doFinally { loading.postValue(false) }
            .subscribe({
                overviewData.postValue(it)
            }, {
                errorMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

    fun getDailyUpdate() {
        appRepository.daily().observeOn(schedulerProvider.ui())
            .doOnSubscribe {
                appRepository.getCacheDaily()?.let { data ->
                    dailyListData.postValue(data)
                }
            }
            .subscribe({
                dailyListData.postValue(it)
            }, {
                it.printStackTrace()
            })
            .addTo(compositeDisposable)
    }


}