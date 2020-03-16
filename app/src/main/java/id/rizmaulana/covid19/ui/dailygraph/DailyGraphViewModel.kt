package id.rizmaulana.covid19.ui.dailygraph

import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.rx.SchedulerProvider

/**
 * rizmaulana 16/03/20.
 */
class DailyGraphViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

}