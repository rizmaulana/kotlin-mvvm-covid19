package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * rizmaulana 04/03/20.
 */
data class CovidOverview(
    @Expose @SerializedName("confirmed") val confirmed: CovidOverviewItem = CovidOverviewItem(),
    @Expose @SerializedName("recovered") val recovered: CovidOverviewItem = CovidOverviewItem(),
    @Expose @SerializedName("deaths") val deaths: CovidOverviewItem = CovidOverviewItem()
)