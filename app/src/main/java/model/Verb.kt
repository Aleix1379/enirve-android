package model

import com.google.gson.annotations.SerializedName

data class Verb(
    @SerializedName("id")
    val id: Int,
    @SerializedName("present")
    val present: String,
    @SerializedName("participle")
    val participle: String,
    @SerializedName("simple")
    val simple: String,
    @SerializedName("matched")
    var matched: Boolean,
    @SerializedName("translations")
    val translations: List<Translation>
) {
    override fun toString(): String {
        return present
    }
}
