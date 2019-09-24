package com.example.imagurtask.ui.main

import androidx.lifecycle.MutableLiveData
import com.example.imagurtask.domain.GalleryRepository
import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.models.Section
import com.example.imagurtask.util.coroutines.SchedulerProvider
import com.example.imagurtask.util.coroutines.with
import com.example.imagurtask.util.mvvm.RxViewModel

class MainViewModel(
    private val galleryRepository: GalleryRepository,
    private val schedulerProvider: SchedulerProvider
) : RxViewModel() {

    val galleryUIModel = MutableLiveData<GalleryUIModel>()

    fun getGallery(
        showViral: Boolean = true,
        section: Section = Section.hot
    ) {
        launch {
            galleryUIModel.postValue(GalleryUIModel(isLoading = true))
            galleryRepository
                .getGallery(
                    showViral = showViral,
                    section = section
                )
                .with(schedulerProvider)
                .subscribe({
                    galleryUIModel.postValue(GalleryUIModel(isSuccess = true, galleryList = it))
                }, {
                    galleryUIModel.postValue(GalleryUIModel(error = it))
                })
        }
    }

}

data class GalleryUIModel(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val galleryList: ArrayList<GalleryData>? = null,
    val error: Throwable? = null
)