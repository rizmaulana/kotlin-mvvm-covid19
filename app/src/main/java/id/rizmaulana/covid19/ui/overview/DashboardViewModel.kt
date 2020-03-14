package id.rizmaulana.covid19.ui.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider

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

    private val _dailyListData = MutableLiveData<List<CovidDaily>>()
    val dailyListData: LiveData<List<CovidDaily>>
        get() = _dailyListData

    private val _overviewData = MutableLiveData<CovidOverview>()
    val overviewData: LiveData<CovidOverview>
        get() = _overviewData

    private val _countryData = MutableLiveData<CovidOverview>()
    val countryData: LiveData<CovidOverview>
        get() = _countryData

    private val _pinData = MutableLiveData<CovidDetail?>()
    val pinData: LiveData<CovidDetail?>
        get() = _pinData

    fun getOverview() {
        appRepository.overview()
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {
                val cache = appRepository.getCacheOverview()
                if (cache == null) _loading.postValue(true) else {
                    _overviewData.postValue(cache)
                }
            }
            .doFinally { _loading.postValue(false) }
            .subscribe({
                _overviewData.postValue(it)
            }, {
                _toastMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

    fun getDailyUpdate() {
        appRepository.daily()
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {
                appRepository.getCacheDaily()?.let { data ->
                    _dailyListData.postValue(data)
                }
            }
            .subscribe({
                _dailyListData.postValue(it)
            }, {
                _toastMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

    fun getPinUpdate() {
        val prefData = appRepository.getPrefCountry()
        if (prefData == null) {
            _pinData.value = null
        } else {
            appRepository
                .confirmed()
                .map { stream ->
                    stream.first {
                        if (it.provinceState != null) it.provinceState == prefData.provinceState
                        else it.countryRegion == prefData.countryRegion
                    }
                }
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { _pinData.postValue(prefData) }
                .subscribe({
                    _pinData.postValue(it)
                }, {
                    _toastMessage.postValue(Constant.UPDATE_ERROR_MESSAGE)
                })
                .addTo(compositeDisposable)
        }
    }

    fun getCountry(id: String) {
        appRepository.country(id)
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe {
                appRepository.getCacheCountry(id)?.let { data ->
                    _countryData.postValue(data)
                }
            }
            .subscribe({
                _countryData.postValue(it)
            }, {
                _toastMessage.postValue(Constant.ERROR_MESSAGE)
            })
            .addTo(compositeDisposable)
    }

}
