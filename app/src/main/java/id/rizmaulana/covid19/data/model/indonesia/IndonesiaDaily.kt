package id.rizmaulana.covid19.data.model.indonesia

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * rizmaulana 22/03/20.
 */
data class IndonesiaDaily(
    @Expose @SerializedName("fid")
    val fid: Int = 0,
    @Expose @SerializedName("harike")
    val harike: Int = 0,
    @Expose @SerializedName("jumlahKasusBaruperHari")
    val jumlahKasusBaruperHari: Int = 0,
    @Expose @SerializedName("jumlahKasusKumulatif")
    val jumlahKasusKumulatif: Int = 0,
    @Expose @SerializedName("jumlahPasienMeninggal")
    val jumlahPasienMeninggal: Int = 0,
    @Expose @SerializedName("jumlahPasienSembuh")
    val jumlahPasienSembuh: Int = 0,
    @Expose @SerializedName("jumlahpasiendalamperawatan")
    val jumlahpasiendalamperawatan: Int = 0,
    @Expose @SerializedName("persentasePasienMeninggal")
    val persentasePasienMeninggal: Float = 0f,
    @Expose @SerializedName("persentasePasienSembuh")
    val persentasePasienSembuh: Float = 0f,
    @Expose @SerializedName("persentasePasiendalamPerawatan")
    val persentasePasiendalamPerawatan: Float = 0f,
    @Expose @SerializedName("tanggal")
    val tanggal: Long = 0
)