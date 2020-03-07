package id.rizmaulana.covid19.data.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * rizmaulana 04/03/20.
 */

@Parcelize
data class CovidDetail(
    @SerializedName("confirmed")
    val confirmed: Int = 0,
    @SerializedName("countryRegion")
    val countryRegion: String? = null,
    @SerializedName("deaths")
    val deaths: Int = 0,
    @SerializedName("lastUpdate")
    val lastUpdate: Long = 0,
    @SerializedName("lat")
    val lat: Double = 0.0,
    @SerializedName("long")
    val long: Double = 0.0,
    @SerializedName("provinceState")
    val provinceState: String? = null,
    @SerializedName("recovered")
    val recovered: Int = 0
) : Parcelable{
    val locationName get() = countryRegion + if (!provinceState.isNullOrEmpty()) ", $provinceState" else ""
}