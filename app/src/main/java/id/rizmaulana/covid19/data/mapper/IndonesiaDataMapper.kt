package id.rizmaulana.covid19.data.mapper

import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.indonesia.IndonesiaDaily
import id.rizmaulana.covid19.data.model.indonesia.IndonesiaPerProvince
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryDailyGraphItem
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryDailyItem
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryProvinceGraphItem
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryProvinceItem

/**
 * rizmaulana 22/03/20.
 */

object IndonesiaDailyDataMapper {

    fun transformToPerCountryDaily(responses: List<IndonesiaDaily>?) = responses?.map { response ->
        PerCountryDailyItem(
            response.fid,
            response.newCasePerDay,
            response.totalDeath,
            response.totalRecover,
            response.totalCase,
            response.date,
            response.days,
            R.string.indonesia_daily_info
        )
    }.orEmpty()

    fun transformIntoCountryDailyGraph(responses: List<IndonesiaDaily>?) = PerCountryDailyGraphItem(
        listData = transformToPerCountryDaily(responses.orEmpty())
    )

    fun transformIntoCountryProvince(responses: List<IndonesiaPerProvince>?) = responses?.map {
        PerCountryProvinceItem(
            it.provinceCode,
            it.provinceName.orEmpty(),
            it.confirmed,
            it.deaths,
            it.recovered
        )
    }.orEmpty()

    fun transformIntoCountryProvinceGraph(responses: List<IndonesiaPerProvince>?) =
        PerCountryProvinceGraphItem(
            listData = transformIntoCountryProvince(responses)
        )
}
