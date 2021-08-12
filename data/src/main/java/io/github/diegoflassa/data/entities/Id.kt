package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Id(
    @Json(name = NAME)
    var name: String = "",
    @Json(name = VALUE)
    var value: String? = null
) : Parcelable {
    companion object {
        const val NAME: String = "name"
        const val VALUE: String = "value"
    }
}
