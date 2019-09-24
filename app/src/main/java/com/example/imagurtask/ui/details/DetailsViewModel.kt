package com.example.imagurtask.ui.details

import androidx.lifecycle.MutableLiveData
import com.example.imagurtask.domain.GalleryRepository
import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.util.coroutines.SchedulerProvider
import com.example.imagurtask.util.coroutines.with
import com.example.imagurtask.util.mvvm.RxViewModel

class DetailsViewModel(
    private val itemId: String,
    private val galleryRepository: GalleryRepository,
    private val schedulerProvider: SchedulerProvider
) : RxViewModel() {

    val galleryUIModel = MutableLiveData<GalleryDataUIModel>()

    fun getGalleryItem() {
        launch {
            galleryRepository
                .getGalleryItemById(itemId)
                .with(schedulerProvider)
                .subscribe({
                    galleryUIModel.postValue(GalleryDataUIModel(isSuccess = true, galleryItem = it))
                }, {
                    galleryUIModel.postValue(GalleryDataUIModel(error = it))
                })
        }
    }


}

data class GalleryDataUIModel(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val galleryItem: GalleryData? = null,
    val error: Throwable? = null
)