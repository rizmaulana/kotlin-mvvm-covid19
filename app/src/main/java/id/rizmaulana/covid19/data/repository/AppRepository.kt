package id.rizmaulana.covid19.data.repository

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.source.pref.AppPrefSource
import id.rizmaulana.covid19.data.source.remote.AppRemoteSource
import id.rizmaulana.covid19.util.IncrementStatus
import io.reactivex.Observable
import java.util.*

/**
 * rizmaulana@live.com 2019-06-14.
 */
class AppRepository constructor(
    private val api: AppRemoteSource,
    private val pref: AppPrefSource
) {

    fun overview() = api.overview()
        .flatMap {
            setCacheOverview(it)
            Observable.just(it)
        }

    fun daily() = api.daily()
        .flatMap {
            var latestRecovered = 0
            var latestConfirmed = 0
            val proceedData = it.map { covid ->
                covid.incrementConfirmed =
                    when {
                        covid.deltaConfirmed > latestConfirmed -> IncrementStatus.INCREASE
                        covid.deltaConfirmed < latestConfirmed -> IncrementStatus.DECREASE
                        else -> IncrementStatus.FLAT
                    }
                covid.incrementRecovered =
                    when {
                        covid.deltaRecovered > latestRecovered -> IncrementStatus.INCREASE
                        covid.deltaRecovered < latestRecovered -> IncrementStatus.DECREASE
                        else -> IncrementStatus.FLAT
                    }
                latestRecovered = covid.deltaRecovered
                latestConfirmed = covid.deltaConfirmed
                covid
            }.toMutableList()
            proceedData.reverse()
            setCacheDaily(proceedData)
            Observable.just(proceedData)
        }

    fun confirmed() = api.confirmed()
        .flatMap {
            setCacheConfirmed(it)
            Observable.just(it)
        }

    fun deaths() = api.deaths()
        .flatMap {
            setCacheDeath(it)
            Observable.just(it)
        }

    fun recovered() = api.recovered()
        .flatMap {
            setCacheRecovered(it)
            Observable.just(it)
        }

    fun getCacheOverview(): CovidOverview? = pref.getOverview()

    fun getCacheDaily(): List<CovidDaily>? = pref.getDaily()

    fun getCacheConfirmed(): List<CovidDetail>? = pref.getConfirmed()

    fun getCacheDeath(): List<CovidDetail>? = pref.getDeath()

    fun getCacheRecovered(): List<CovidDetail>? = pref.getRecovered()

    private fun setCacheOverview(covidOverview: CovidOverview) = pref.setOverview(covidOverview)

    private fun setCacheDaily(covid: List<CovidDaily>) = pref.setDaily(covid)

    private fun setCacheConfirmed(covid: List<CovidDetail>) = pref.setConfirmed(covid)

    private fun setCacheDeath(covid: List<CovidDetail>) = pref.setDeath(covid)

    private fun setCacheRecovered(covid: List<CovidDetail>) = pref.setRecovered(covid)

}