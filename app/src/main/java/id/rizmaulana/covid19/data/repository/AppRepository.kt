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
open class AppRepository constructor(
    private val api: AppRemoteSource,
    private val pref: AppPrefSource
) : Repository {

    override fun overview(): Observable<CovidOverview> = api.overview()
        .flatMap {
            setCacheOverview(it)
            Observable.just(it)
        }

    override fun daily(): Observable<MutableList<CovidDaily>> = api.daily()
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

    override fun confirmed(): Observable<List<CovidDetail>> = api.confirmed()
        .flatMap {
            setCacheConfirmed(it)
            Observable.just(it)
        }

    override fun deaths(): Observable<List<CovidDetail>> = api.deaths()
        .flatMap {
            setCacheDeath(it)
            Observable.just(it)
        }

    override fun recovered(): Observable<List<CovidDetail>> = api.recovered()
        .flatMap {
            setCacheRecovered(it)
            Observable.just(it)
        }

    override fun getCacheOverview(): CovidOverview? = pref.getOverview()

    override fun getCacheDaily(): List<CovidDaily>? = pref.getDaily()

    override fun getCacheConfirmed(): List<CovidDetail>? = pref.getConfirmed()

    override fun getCacheDeath(): List<CovidDetail>? = pref.getDeath()

    override fun getCacheRecovered(): List<CovidDetail>? = pref.getRecovered()

    private fun setCacheOverview(covidOverview: CovidOverview) = pref.setOverview(covidOverview)

    private fun setCacheDaily(covid: List<CovidDaily>) = pref.setDaily(covid)

    private fun setCacheConfirmed(covid: List<CovidDetail>) = pref.setConfirmed(covid)

    private fun setCacheDeath(covid: List<CovidDetail>) = pref.setDeath(covid)

    private fun setCacheRecovered(covid: List<CovidDetail>) = pref.setRecovered(covid)

}