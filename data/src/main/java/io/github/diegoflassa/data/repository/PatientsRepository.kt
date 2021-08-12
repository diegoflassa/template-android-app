package io.github.diegoflassa.template_android_app.data.repository

import io.github.diegoflassa.template_android_app.helper.SearchResult
import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.diegoflassa.template_android_app.enums.QueryFields
import io.github.diegoflassa.template_android_app.helper.Constants
import io.github.diegoflassa.data.adapters.UriAdapter
import io.github.diegoflassa.data.entities.Patient
import io.github.diegoflassa.data.entities.Patients
import io.github.diegoflassa.data.interfaces.PatientsDao
import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class PatientsRepository {

    private val tag: String? = PatientsRepository::class.simpleName
    private val patientsBaseUrl = "https://randomuser.me"

    fun getAllPaginated(
        page: Int,
        pageSize: Int
    ): Observable<Patients> {
        val okHttpClient = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(UriAdapter())
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(patientsBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        Log.i(tag, "PatientsRepository.getAllPaginated")
        return retrofit.create(PatientsDao::class.java)
            .getAllPaginated(page, pageSize, getUnusedField(), getSeed())
    }

    fun findAllPaginated(
        query: Map<QueryFields, String>,
        page: Int = 1,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE_MOBILE,
        startPage: Int?,
        maxPageToSearch: Int?,
        previousResults: SearchResult?
    ): Observable<SearchResult> {
        val okHttpClient = OkHttpClient()
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(UriAdapter())
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(patientsBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        Log.i(tag, "PatientsRepository.findAllPaginated")
        val containsGender = query.containsKey(QueryFields.GENDER)
        val seed = ""
        if (containsGender) {
            getSeed()
        }
        var ret = io.github.diegoflassa.template_android_app.helper.SearchResult()
        ret.patients = retrofit.create(PatientsDao::class.java)
            .findAllPaginated(
                getQueryString(query),
                page,
                pageSize,
                getUnusedField(),
                seed
            ).blockingFirst()

        if (ret.patients.patients.isNotEmpty()) {
            if (query.keys.contains(QueryFields.FULL_NAME)) {
                val queryName = query[QueryFields.FULL_NAME]
                val tempPatients = mutableListOf<Patient>()
                for (patient in ret.patients.patients) {
                    if (patient.fullName
                            .getFullName()
                            .contains(queryName!!, true)
                    ) {
                        tempPatients.add(patient)
                    }
                }
                ret.patients = Patients(tempPatients)
            }

            var maxPageToSearchVar = maxPageToSearch
            if (maxPageToSearchVar != null) {
                maxPageToSearchVar = page + Constants.DEFAULT_MAX_PAGE_TO_SEARCH
            }
            if (previousResults != null) {
                ret.patients = previousResults.patients
                ret.lastSearchedPage = previousResults.lastSearchedPage
            }
            if (ret.patients.patients.size < Constants.DEFAULT_PAGE_SIZE_MOBILE &&
                (maxPageToSearchVar != null && page < maxPageToSearchVar)
            ) {
                ret = findAllPaginated(
                    query,
                    page + 1,
                    pageSize,
                    startPage,
                    maxPageToSearchVar,
                    ret,
                ).blockingFirst()
            } else {
                ret.lastSearchedPage = page
            }
        } else {
            throw Exception("Failed to load Patients")
        }
        return Observable.just(ret)
    }

    private fun getQueryString(query: Map<QueryFields, String>?): String {
        var ret = ""
        if (query != null && query.isNotEmpty()) {
            for (field in query.keys) {
                when (field) {
                    QueryFields.FULL_NAME -> {
                    }
                    // Do nothing
                    QueryFields.GENDER -> {
                        ret += "&gender=${query[field]!!.lowercase(Locale.getDefault())}"
                    }
                    QueryFields.NATIONALITY -> {
                        ret += "&nat=${query[field]}"
                    }
                    QueryFields.UNKNOWN -> {
                    }
                }
            }
        }
        return ret
    }

    private fun getUnusedField(): String {
        return "login, registered"
    }

    private fun getSeed(): String {
        return "42"
    }
}
