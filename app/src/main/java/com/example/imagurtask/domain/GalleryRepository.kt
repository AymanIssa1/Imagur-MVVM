package com.example.imagurtask.domain

import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.models.Section
import com.example.imagurtask.remote.RemoteDataSource
import io.reactivex.Single

interface GalleryRepository {

    fun getGallery(
        section: Section = Section.hot,
        showViral: Boolean = true
    ): Single<ArrayList<GalleryData>>

    fun getGalleryByPage(
        section: String,
        page: Int,
        showViral: Boolean
    ): Single<ArrayList<GalleryData>>

    fun getGalleryItemById(itemId: String): Single<GalleryData?>

}

class GalleryRepositoryImp(private val remoteDataSource: RemoteDataSource) : GalleryRepository {

    private val galleryCache = arrayListOf<GalleryData>()

    override fun getGallery(
        section: Section,
        showViral: Boolean
    ): Single<ArrayList<GalleryData>> {
        return remoteDataSource.getGallery(section = section.name, showViral = showViral)
            .map {
                galleryCache.clear()
                galleryCache.addAll(it.body().data)
                galleryCache
            }
    }

    override fun getGalleryByPage(
        section: String,
        page: Int,
        showViral: Boolean
    ): Single<ArrayList<GalleryData>> {
        return remoteDataSource.getGallery(section = section, page = page, showViral = showViral)
            .map {
                galleryCache.clear()
                galleryCache.addAll(it.body().data)
                galleryCache
            }
    }

    override fun getGalleryItemById(itemId: String): Single<GalleryData?> {
        val galleryData = galleryCache.find { it.id == itemId }
        return if (galleryData != null)
            Single.just(galleryData)
        else
            Single.error(Throwable("Data not found"))
    }

}