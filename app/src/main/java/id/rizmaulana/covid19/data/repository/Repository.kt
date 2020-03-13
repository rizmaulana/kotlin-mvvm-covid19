package id.rizmaulana.covid19.data.repository

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import io.reactivex.Observable

interface Repository {
    fun overview(): Observable<CovidOverview>
    fun daily(): Observable<List<CovidDaily>>
    fun confirmed(): Observable<List<CovidDetail>>
    fun deaths(): Observable<List<CovidDetail>>
    fun recovered(): Observable<List<CovidDetail>>
    fun fullStats(): Observable<List<CovidDetail>>
    fun getCacheOverview(): CovidOverview?
    fun getCacheDaily(): List<CovidDaily>?
    fun getCacheConfirmed(): List<CovidDetail>?
    fun getCacheDeath(): List<CovidDetail>?
    fun getCacheRecovered(): List<CovidDetail>?
    fun getCacheFull(): List<CovidDetail>?
}