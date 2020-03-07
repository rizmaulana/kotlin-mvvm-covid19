package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.SerializedName

/**
 * rizmaulana 04/03/20.
 */
data class CovidOverview(
    @SerializedName("confirmed")
    val confirmed: CovidOverviewItem? = null,
    @SerializedName("recovered")
    val recovered: CovidOverviewItem? = null,
    @SerializedName("deaths")
    val deaths: CovidOverviewItem? = null
)