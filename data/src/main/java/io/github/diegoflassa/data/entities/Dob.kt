package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Dob(
    @Json(name = DATE)
    var date: String = Calendar.getInstance().toString(),
    @Json(name = AGE)
    var age: Int = 0
) : Parcelable {
    companion object {
        const val DATE: String = "date"
        const val AGE: String = "age"
    }

    fun getDateAsDateTime(): Date? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        return inputFormat.parse(date)
    }
}
