package id.rizmaulana.covid19.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * rizmaulana 22/03/20.
 */
data class BaseResponse<T>(
    @Expose @SerializedName("data")
    val data: T
)