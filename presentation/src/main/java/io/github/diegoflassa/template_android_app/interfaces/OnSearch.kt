package io.github.diegoflassa.template_android_app.interfaces

import android.os.Parcelable
import io.github.diegoflassa.template_android_app.enums.Gender
import kotlinx.android.parcel.Parcelize

interface OnSearch : Parcelable  {
  fun onSearch(
      query : String?, nationality:String?, gender : Gender = Gender.UNKNOWN)
  fun clear()
}
