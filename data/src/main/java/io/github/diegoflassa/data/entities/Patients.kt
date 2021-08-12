package io.github.diegoflassa.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Patients(
    @SerializedName(RESULTS) @Json(name = RESULTS) var patients: MutableList<Patient>
) : Parcelable {
    companion object {
        const val RESULTS = "results"
    }
}
