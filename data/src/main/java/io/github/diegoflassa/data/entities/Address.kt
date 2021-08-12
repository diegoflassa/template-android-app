package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    @Json(name = STREET)
    var street: Street = Street(),
    @Json(name = CITY)
    var city: String = "",
    @Json(name = STATE)
    var state: String = "",
    @Json(name = POSTCODE)
    var postCode: String = ""
) : Parcelable {
    companion object {
        const val STREET: String = "street"
        const val CITY: String = "city"
        const val STATE: String = "state"
        const val POSTCODE: String = "postcode"
    }

    fun getFullAddress(): String {
        return "${street.name} - ${street.number} - $city - $state - $postCode"
    }
}
