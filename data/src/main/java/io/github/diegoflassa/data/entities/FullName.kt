package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class FullName(
    @Json(name = TITLE)
    var title: String = "",
    @Json(name = FIRST)
    var first: String = "",
    @Json(name = LAST)
    var last: String = ""
) : Parcelable {
    companion object {
        const val TITLE: String = "title"
        const val FIRST: String = "first"
        const val LAST: String = "last"
    }

    fun getFullName(): String {
        return "$title $first $last"
    }
}
