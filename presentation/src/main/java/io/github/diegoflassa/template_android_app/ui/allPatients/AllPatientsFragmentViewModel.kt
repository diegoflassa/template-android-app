package io.github.diegoflassa.template_android_app.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.diegoflassa.template_android_app.data.entities.Patient
import io.github.diegoflassa.template_android_app.data.entities.Patients
import io.github.diegoflassa.template_android_app.data.repository.PatientsRepository
import io.github.diegoflassa.template_android_app.helper.Constants
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import javax.inject.Inject
import android.R.bool
import io.github.diegoflassa.template_android_app.enums.QueryFields
import io.github.diegoflassa.template_android_app.helper.SearchResult
import java.util.*


//@DelicateCoroutinesApi
//@HiltViewModel
class AllPatientsFragmentViewModel /*@Inject*/ constructor(
    private val handle: @JvmSuppressWildcards SavedStateHandle,
) : ViewModel() {

    companion object {
        private val tag = AllPatientsFragmentViewModel::class.simpleName
        private const val key_patients = "key_patients"
        private const val key_current_page = "key_current_page"
        private const val key_is_from_query = "key_is_from_query"
        private const val key_query_fields = "key_query_fields"
    }

    var isLoading: Boolean = false

    private var mPatients: MutableList<Patient> = ArrayList()
        set(value) {
            field = value
            GlobalScope.launch {
                withContext(Dispatchers.Main) { handle.set(key_patients, value) }
            }
        }

    init {
        Log.i(tag, "AllPatientsFragmentViewModel.init")
        handle.set(key_patients, mPatients)
        handle.set(key_current_page, 1)
        handle.set(key_is_from_query, false)
        handle.set(key_query_fields, HashMap<QueryFields, String>())
        loadItems(1)
    }

    private fun loadItems(page: Int) {
        isLoading= true
        val patientsRepo = PatientsRepository()
        viewModelScope.launch {
            patientsRepo.getAllPaginated(page, Constants.DEFAULT_PAGE_SIZE_MOBILE)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { data: Patients ->
                        if(mPatients.isNotEmpty()){
                            mPatients.removeLast()
                        }
                        mPatients.addAll(data.patients)
                        mPatients.add(Patient())
                        val patientsLiveData : MutableLiveData<List<Patient>> = handle.getLiveData(key_patients)
                        patientsLiveData.postValue(mPatients)
                        isLoading= false
                    },
                    { throwable: Throwable? ->
                        if (throwable != null) {
                            Log.e(tag, throwable.toString())
                        }
                        isLoading= false
                    },
                )
        }
    }

    private fun findItems(page: Int) {
        isLoading= true
        val patientsRepo = PatientsRepository()
        viewModelScope.launch {
            patientsRepo.findAllPaginated(
                queryFields,
                page,
                Constants.DEFAULT_PAGE_SIZE_MOBILE,
                null,
                null,
                null
            )
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { data: SearchResult ->
                        if(mPatients.isNotEmpty()){
                            mPatients.removeLast()
                        }
                        mPatients.addAll(data.patients.patients)
                        mPatients.add(Patient())
                        val patientsLiveData : MutableLiveData<List<Patient>> = handle.getLiveData(key_patients)
                        patientsLiveData.postValue(mPatients)
                        isLoading= false
                    },
                    { throwable: Throwable? ->
                        if (throwable != null) {
                            Log.e(tag, throwable.toString())
                        }
                        isLoading= false
                    },
                )
        }
    }

    fun reloadData() {
        currentPage = 1
        loadItems(currentPage)
    }

    fun getNextPage() {
        loadItems(++currentPage)
    }

    fun search() {
        findItems(++currentPage)
    }

    fun clear() {
        handle.set(key_patients, mPatients)
        handle.set(key_current_page, 1)
        handle.set(key_is_from_query, false)
        handle.set(key_query_fields, HashMap<QueryFields, String>())
        loadItems(currentPage)
    }

    val patientsLiveData: MutableLiveData<List<Patient>>
        get(): MutableLiveData<List<Patient>> {
            return handle.getLiveData(key_patients)
        }

    val patients: List<Patient>
        get(): List<Patient> {
            return handle.get<List<Patient>>(key_patients)!!
        }

    var queryFieldsPageLiveData: MutableLiveData<Map<QueryFields, String>>
        get(): MutableLiveData<Map<QueryFields, String>> {
            return handle.getLiveData(key_query_fields)
        }
        set(value) {
            return handle.set(key_query_fields, value)
        }

    var queryFields: Map<QueryFields, String>
        get():Map<QueryFields, String> {
            return handle.get<Map<QueryFields, String>>(key_query_fields)!!
        }
        set(value) {
            return handle.set(key_query_fields, value)
        }

    var currentPageLiveData: MutableLiveData<Int>
        get(): MutableLiveData<Int> {
            return handle.getLiveData(key_current_page)
        }
        set(value) {
            return handle.set(key_current_page, value)
        }

    var currentPage: Int
        get():Int {
            return handle.get<Int>(key_current_page)!!
        }
        set(value) {
            return handle.set(key_current_page, value)
        }

    var isFromQueryLiveData: MutableLiveData<bool>
        get(): MutableLiveData<bool> {
            return handle.getLiveData(key_is_from_query)
        }
        set(value) {
            return handle.set(key_is_from_query, value)
        }

    var isFromQuery: Boolean
        get(): Boolean {
            return handle.get<Boolean>(key_is_from_query)!!
        }
        set(value) {
            return handle.set(key_is_from_query, value)
        }
}
