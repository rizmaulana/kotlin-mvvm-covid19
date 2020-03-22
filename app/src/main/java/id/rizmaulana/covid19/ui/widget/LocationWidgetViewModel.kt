package id.rizmaulana.covid19.ui.widget

import id.rizmaulana.covid19.data.repository.Repository
import id.rizmaulana.covid19.ui.base.BaseViewModel
import id.rizmaulana.covid19.util.rx.SchedulerProvider

/**
 * rizmaulana 22/03/20.
 */
data class LocationWidgetViewModel(
    private val appRepository: Repository,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel() {

}