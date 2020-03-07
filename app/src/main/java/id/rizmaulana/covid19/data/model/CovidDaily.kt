package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.SerializedName
import id.rizmaulana.covid19.util.IncrementStatus


/**
 * rizmaulana 04/03/20.
 */
data class CovidDaily(
    @SerializedName("deltaConfirmed")
    val deltaConfirmed: Int = 0,
    @SerializedName("deltaRecovered")
    val deltaRecovered: Int = 0,
    @SerializedName("mainlandChina")
    val mainlandChina: Int = 0,
    @SerializedName("objectid")
    val objectid: Int = 0,
    @SerializedName("otherLocations")
    val otherLocations: Int = 0,
    @SerializedName("reportDate")
    val reportDate: Long = 0,
    @SerializedName("reportDateString")
    val reportDateString: String? = null,
    @SerializedName("totalConfirmed")
    val totalConfirmed: Int = 0,
    @SerializedName("totalRecovered")
    val totalRecovered: Int = 0,

    @SerializedName("incrementRecovered")
    var incrementRecovered: Int = IncrementStatus.FLAT,
    @SerializedName("incrementConfirmed")
    var incrementConfirmed: Int = IncrementStatus.FLAT

)