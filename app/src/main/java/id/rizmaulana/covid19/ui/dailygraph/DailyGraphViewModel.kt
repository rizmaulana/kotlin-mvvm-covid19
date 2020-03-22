package id.rizmaulana.covid19.ui.dailygraph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.mapper.CovidDailyDataMapper
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.SingleLiveEvent
import id.rizmaulana.covid19.util.ext.addTo
import id.rizmaulana.covid19.util.rx.SchedulerProvider

/**
 * rizmaulana 16/03/20.
 */
class DailyGraphViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {
    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val _loading = SingleLiveEvent<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _dailyItems = MutableLiveData<List<CovidDaily>>()
    val dailyItems: LiveData<List<CovidDaily>>
        get() = _dailyItems

    fun loadCacheDailyData() {
        /*Assume daily data just got fresh data from remote api on previous page
          for UX Purpose, we directly load cache
        */
        _dailyItems.postValue(appRepository.getCacheDaily().orEmpty())
    }

    fun loadRemoteDailyData() {
        appRepository.daily().subscribe({ response ->
            _loading.postValue(false)
            response.data?.let {
                _dailyItems.postValue(it)
            }
        }, {
            _loading.postValue(false)

        }).addTo(compositeDisposable)
    }
}