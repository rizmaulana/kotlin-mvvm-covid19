package id.rizmaulana.covid19.data.repository

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.data.source.pref.AppPrefSource
import id.rizmaulana.covid19.data.source.remote.AppRemoteSource
import id.rizmaulana.covid19.util.IncrementStatus
import io.reactivex.Observable
import io.reactivex.functions.Function3

/**
 * rizmaulana@live.com 2019-06-14.
 */
open class AppRepository constructor(
    private val api: AppRemoteSource,
    private val pref: AppPrefSource
) : Repository {

    override fun overview() = api.overview()
        .flatMap {
            setCacheOverview(it)
            Observable.just(it)
        }

    override fun daily() = api.daily()
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

    override fun confirmed() = api.confirmed()
        .flatMap {
            setCacheConfirmed(it)
            Observable.just(it)
        }

    override fun deaths() = api.deaths()
        .flatMap {
            setCacheDeath(it)
            Observable.just(it)
        }

    override fun recovered() = api.recovered()
        .flatMap {
            setCacheRecovered(it)
            Observable.just(it)
        }

    override fun fullStats(): Observable<List<CovidDetail>> {
        return Observable.zip(
            api.confirmed(),
            api.recovered(),
            api.deaths(),
            Function3<List<CovidDetail>, List<CovidDetail>, List<CovidDetail>, List<CovidDetail>> { t1, t2, t3 ->
                val result: MutableList<CovidDetail> = mutableListOf()
                t1.forEach { it1 ->
                    val t2Match = if (it1.provinceState == null)
                        t2.firstOrNull { it.countryRegion == it1.countryRegion }
                    else t2.firstOrNull { it.provinceState == it1.provinceState }
                    val t3Match = if (it1.provinceState == null)
                        t3.firstOrNull { it.countryRegion == it1.countryRegion }
                    else t3.firstOrNull { it.provinceState == it1.provinceState }

                    result.add(
                        it1.copy(
                            recovered = t2Match?.recovered ?: -1,
                            deaths = t3Match?.deaths ?: -1
                        )
                    )

                }
                result
            })
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