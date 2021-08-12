package io.github.diegoflassa.template_android_app.enums

import androidx.annotation.Keep

@Keep
enum class Gender(var gender: String) {
    MALE("male"), FEMALE("female"), PREFER_NOT_TO_SAY("prefer_not_to_say"), UNKNOWN("unknown");

    override fun toString(): String {
        return gender
    }

    companion object {
        fun stringToEnum(gender: String): Gender {
            return when (gender) {
                "male" -> MALE
                "female" -> FEMALE
                "prefer_not_to_say" -> PREFER_NOT_TO_SAY
                else -> UNKNOWN
            }
        }
    }
}
