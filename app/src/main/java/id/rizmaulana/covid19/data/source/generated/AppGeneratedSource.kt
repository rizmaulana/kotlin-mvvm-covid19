package id.rizmaulana.covid19.data.source.generated

import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryItem

/**
 * rizmaulana 22/03/20.
 */
class AppGeneratedSource {

    fun getPerCountryItem() = listOf(
        PerCountryItem(
            PerCountryItem.ID,
            "Indonesia",
            "https://indonesia-covid-19.mathdro.id/api",
            R.drawable.flag_indonesia
        )
    )
}