package model

import com.google.gson.annotations.SerializedName

data class Translation(
    @SerializedName("id")
    val id: Int,
    @SerializedName("language")
    val language: String,
    @SerializedName("value")
    val value: String
)
