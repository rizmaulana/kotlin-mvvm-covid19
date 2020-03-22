package id.rizmaulana.covid19.data.model.indonesia

import com.google.gson.annotations.SerializedName


/**
 * rizmaulana 22/03/20.
 */
data class IndonesiaPerProvince(
    @SerializedName("creationdate")
    val creationdate: Long = 0,
    @SerializedName("creator")
    val creator: String? = null,
    @SerializedName("editdate")
    val editdate: Long = 0,
    @SerializedName("editor")
    val editor: String? = null,
    @SerializedName("fid")
    val fid: Int = 0,
    @SerializedName("globalid")
    val globalid: String? = null,
    @SerializedName("kasusMeninggalAkumulatif")
    val kasusMeninggalAkumulatif: Int = 0,
    @SerializedName("kasusSembuhAkumulatif")
    val kasusSembuhAkumulatif: Int = 0,
    @SerializedName("kasusTerkonfirmasiAkumulatif")
    val kasusTerkonfirmasiAkumulatif: Int = 0,
    @SerializedName("kodeProvinsi")
    val kodeProvinsi: Int = 0,
    @SerializedName("pembaruan")
    val pembaruan: String? = null,
    @SerializedName("provinsi")
    val provinsi: String? = null
)