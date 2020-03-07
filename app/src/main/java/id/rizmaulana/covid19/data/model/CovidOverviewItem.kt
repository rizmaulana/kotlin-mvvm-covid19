package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.SerializedName


/**
 * rizmaulana 04/03/20.
 */
data class CovidOverviewItem(
    @SerializedName("detail")
    val detail: String? = null,
    @SerializedName("value")
    val value: Int = 0
)