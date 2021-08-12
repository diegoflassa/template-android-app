package io.github.diegoflassa.data.interfaces

import io.github.diegoflassa.data.entities.Patients
import io.github.diegoflassa.template_android_app.helper.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PatientsDao {

    @GET("/api/")
    fun getAllPaginated(
        @Query("page") page: Int,
        @Query("results") pageSize: Int,
        @Query("exc") unusedFields: String,
        @Query("seed") seed: String,
    ): Observable<Patients>

    @GET("/api/{query}")
    fun findAllPaginated(
        @Path("query")query: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = Constants.DEFAULT_PAGE_SIZE_MOBILE,
        @Query("exc") unusedFields: String,
        @Query("seed") seed: String,
    ): Observable<Patients>
}
