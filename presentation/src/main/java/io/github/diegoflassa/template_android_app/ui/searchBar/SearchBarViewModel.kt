package io.github.diegoflassa.template_android_app.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.diegoflassa.template_android_app.MyApplication
import io.github.diegoflassa.template_android_app.R
import io.github.diegoflassa.template_android_app.data.entities.Patient
import javax.inject.Inject
import io.github.diegoflassa.template_android_app.enums.Gender
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
//@HiltViewModel
class SearchBarViewModel /*@Inject*/ constructor(
    private val handle: @JvmSuppressWildcards SavedStateHandle
) : ViewModel() {
    companion object {
        private val tag = SearchBarViewModel::class.simpleName
        private const val key_query = "key_query"
        private const val key_nationality = "key_nationality"
        private const val key_gender = "key_gender"
    }

    init {
        handle.set<String>(key_query, "")
        handle.set<String>(key_nationality, MyApplication.getContext().getString(R.string.all))
        handle.set<Gender>(key_gender, Gender.UNKNOWN)
        Log.i(tag, "SearchBarViewModel.init")
    }

    val queryLiveData: MutableLiveData<String>
        get(): MutableLiveData<String> {
            return handle.getLiveData(key_query)
        }
    var query: String?
        get(): String? {
            return handle.get<String>(key_query)
        }
        set(value) {
            handle.set<String>(key_query, value)
        }

    val nationalityLiveData: MutableLiveData<String>
        get(): MutableLiveData<String> {
            return handle.getLiveData(key_nationality)
        }
    var nationality: String?
        get(): String? {
            return handle.get<String>(key_nationality)
        }
        set(value) {
            handle.set<String>(key_nationality, value)
        }

    val genderLiveData: MutableLiveData<Gender>
        get(): MutableLiveData<Gender> {
            return handle.getLiveData(key_gender)
        }
    var gender: Gender
        get(): Gender {
            return handle.get<Gender>(key_gender)!!
        }
        set(value) {
            handle.set<Gender>(key_gender, value)
        }

    fun clear(){
        handle.set<String>(key_query, "")
        handle.set<String>(key_nationality, MyApplication.getContext().getString(R.string.all))
        handle.set<Gender>(key_gender, Gender.UNKNOWN)
    }
}