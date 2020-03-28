package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import id.rizmaulana.covid19.util.IncrementStatus


/**
 * rizmaulana 04/03/20.
 */
data class CovidDaily(
    @SerializedName("active")
    var active: Int = 0,
    @SerializedName("confirmed")
    var confirmed: Confirmed = Confirmed(),
    @SerializedName("deaths")
    var deaths: Deaths = Deaths(),
    @SerializedName("deltaConfirmed")
    var deltaConfirmed: Int = 0,
    @SerializedName("deltaConfirmedDetail")
    var deltaConfirmedDetail: DeltaConfirmedDetail = DeltaConfirmedDetail(),
    @SerializedName("deltaRecovered")
    var deltaRecovered: Int = 0,
    @SerializedName("incidentRate")
    var incidentRate: Int = 0,
    @SerializedName("mainlandChina")
    var mainlandChina: Int = 0,
    @SerializedName("otherLocations")
    var otherLocations: Int = 0,
    @SerializedName("peopleTested")
    var peopleTested: Int = 0,
    @SerializedName("recovered")
    var recovered: Recovered = Recovered(),
    @SerializedName("reportDate")
    var reportDate: String = "",
    @SerializedName("totalConfirmed")
    var totalConfirmed: Int = 0,
    @SerializedName("totalRecovered")
    var totalRecovered: Int = 0,
    @Expose @SerializedName("incrementRecovered")
    var incrementRecovered: Int = IncrementStatus.FLAT,
    @Expose @SerializedName("incrementConfirmed")
    var incrementConfirmed: Int = IncrementStatus.FLAT
)

data class Confirmed(
    @SerializedName("china")
    var china: Int = 0,
    @SerializedName("outsideChina")
    var outsideChina: Int = 0,
    @SerializedName("total")
    var total: Int = 0
)

data class Deaths(
    @SerializedName("china")
    var china: Int = 0,
    @SerializedName("outsideChina")
    var outsideChina: Int = 0,
    @SerializedName("total")
    var total: Int = 0
)

data class DeltaConfirmedDetail(
    @SerializedName("china")
    var china: Int = 0,
    @SerializedName("outsideChina")
    var outsideChina: Int = 0,
    @SerializedName("total")
    var total: Int = 0
)

data class Recovered(
    @SerializedName("china")
    var china: Int = 0,
    @SerializedName("outsideChina")
    var outsideChina: Int = 0,
    @SerializedName("total")
    var total: Int = 0
)