package io.github.diegoflassa.data.entities

import android.content.Context
import android.os.Parcelable
import com.squareup.moshi.Json
import io.github.diegoflassa.data.R
import io.github.diegoflassa.data.entities.Address
import io.github.diegoflassa.data.entities.Dob
import io.github.diegoflassa.data.entities.FullName
import io.github.diegoflassa.template_android_app.enums.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class Patient(
    @Json(name = ID)
    var id: Id = Id(),

    @Json(name = PICTURE)
    var picture: Picture = Picture(),

    @Json(name = FULL_NAME)
    var fullName: FullName = FullName(),

    @Json(name = EMAIL)
    var email: String = "",

    @Json(name = GENDER)
    var gender: String = "",

    @Json(name = DATE_OF_BIRTH)
    var dateOfBirth: Dob = Dob(),

    @Json(name = TELEPHONE)
    var telephone: String = "",

    @Json(name = CELLPHONE)
    var cellphone: String = "",

    @Json(name = NATIONALITY)
    var nationality: String = "",

    @Json(name = ADDRESS)
    var address: Address = Address(),
) : Parcelable {

    companion object {
        const val ID = "id"
        const val PICTURE = "picture"
        const val FULL_NAME = "name"
        const val EMAIL = "email"
        const val GENDER = "gender"
        const val DATE_OF_BIRTH = "dob"
        const val TELEPHONE = "phone"
        const val CELLPHONE = "cell"
        const val NATIONALITY = "nat"
        const val ADDRESS = "location"
    }

    fun getGenderAsEnum(): Gender {
        return genderStringToEnum(gender)
    }

    private fun genderStringToEnum(gender: String): Gender {
        var ret = Gender.UNKNOWN
        if (gender == "female") {
            ret = Gender.FEMALE
        } else if (gender == "male") {
            ret = Gender.MALE
        }
        return ret
    }

    private fun genderEnumToString(
        context: Context,
        gender: Gender,
        unknownAsBoth: Boolean = false
    ): String {
        var ret = context.getString(R.string.unknown)
        if (unknownAsBoth) {
            ret = context.getString(R.string.any)
        }
        when (gender) {
            Gender.MALE -> {
                ret = context.getString(R.string.male)
            }
            Gender.FEMALE -> {
                ret = context.getString(R.string.female)
            }
            Gender.PREFER_NOT_TO_SAY -> {
                ret = context.getString(R.string.prefer_not_to_say)
            }
            Gender.UNKNOWN -> {
                ret = context.getString(R.string.unknown)
                if (unknownAsBoth) {
                    ret = context.getString(R.string.any)
                }
            }
        }
        return ret
    }
}
