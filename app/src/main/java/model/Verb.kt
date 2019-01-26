package model

import com.google.gson.annotations.SerializedName

data class Verb(
    @SerializedName("id")
    val id: Int,
    @SerializedName("verb")
    val verb: String,
    @SerializedName("participle")
    val participle: String,
    @SerializedName("simple")
    val simple: String,
    @SerializedName("matched")
    val matched: Boolean,
    @SerializedName("translations")
    val translations: List<Translation>
) {
    override fun toString(): String {
        return verb
    }
}
