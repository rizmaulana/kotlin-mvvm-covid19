package id.rizmaulana.covid19.ui.dailygraph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import id.rizmaulana.covid19.data.mapper.CovidDailyDataMapper
import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.SingleLiveEvent
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

    private val _dailyItems = MutableLiveData<List<DailyItem>>()
    val dailyItems: LiveData<List<DailyItem>>
        get() = _dailyItems

    fun loadDailyData() {
        /*Assume daily data just got fresh data from remote api on previous page
          for UX Purpose, we directly load cache
        */
        _dailyItems.postValue(CovidDailyDataMapper.transform(appRepository.getCacheDaily().orEmpty()))
    }
}