package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
class Street(
    @Json(name = NUMBER)
    var number: Int = 0,
    @Json(name = NAME)
    var name: String = ""
) : Parcelable {
    companion object {
        const val NUMBER: String = "number"
        const val NAME: String = "name"
    }

}
