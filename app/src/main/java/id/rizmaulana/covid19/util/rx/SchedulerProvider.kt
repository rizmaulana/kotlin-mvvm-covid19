package id.rizmaulana.covid19.util.rx

import io.reactivex.Scheduler

/**
 * rizmaulana 25/02/20.
 */
interface SchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
}