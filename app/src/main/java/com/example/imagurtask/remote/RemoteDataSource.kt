package com.example.imagurtask.remote

import com.example.imagurtask.models.Gallery
import com.example.imagurtask.models.Section
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteDataSource {

    @GET("/3/gallery/{section}/{page}")
    fun getGallery(
        @Path("section") section: String = Section.hot.name,
        @Path("page") page: Int = 0,
        @Query("showViral") showViral: Boolean = true
    ): Single<Response<Gallery>>

}