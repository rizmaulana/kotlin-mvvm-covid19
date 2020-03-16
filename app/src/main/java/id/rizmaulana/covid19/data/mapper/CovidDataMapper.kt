package id.rizmaulana.covid19.data.mapper

import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.adapter.viewholders.OverviewItem
import id.rizmaulana.covid19.ui.adapter.viewholders.PinnedItem

object CovidDailyDataMapper {

    fun transform(responses: List<CovidDaily>) = responses.map { response ->
        DailyItem(
            response.objectid,
            response.deltaConfirmed,
            response.deltaRecovered,
            response.mainlandChina,
            response.otherLocations,
            response.reportDate,
            response.incrementRecovered,
            response.incrementConfirmed
        )
    }
}

object CovidOverviewDataMapper {

    fun transform(response: CovidOverview?) = if(response != null) listOf(OverviewItem(
        response.confirmed?.value ?: 0,
        response.recovered?.value ?: 0,
        response.deaths?.value ?: 0))
    else emptyList()
}

object CovidPinnedDataMapper {

    fun transform(response: CovidDetail?) = if(response != null) listOf(PinnedItem(
        response.confirmed,
        response.recovered,
        response.deaths,
        response.locationName,
        response.lastUpdate))
    else emptyList()
}