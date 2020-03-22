package id.rizmaulana.covid19.data.model.indonesia

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * rizmaulana 22/03/20.
 */
data class IndonesiaPerProvince(
    @Expose @SerializedName("fid")
    val fid: Int = 0,
    @Expose @SerializedName("kasusMeni")
    val kasusMeni: Int = 0,
    @Expose @SerializedName("kasusPosi")
    val kasusPosi: Int = 0,
    @Expose @SerializedName("kasusSemb")
    val kasusSemb: Int = 0,
    @Expose @SerializedName("kodeProvi")
    val kodeProvi: Int = 0,
    @Expose @SerializedName("provinsi")
    val provinsi: String? = null
)