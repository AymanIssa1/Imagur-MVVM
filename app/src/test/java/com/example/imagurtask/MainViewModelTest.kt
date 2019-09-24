package com.example.imagurtask.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.imagurtask.di.appModule
import com.example.imagurtask.di.remoteDataSourceModule
import com.example.imagurtask.domain.GalleryRepository
import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.ui.main.GalleryUIModel
import com.example.imagurtask.ui.main.MainViewModel
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest : KoinTest {

    lateinit var mainViewModel: MainViewModel

    @Mock
    lateinit var galleryRepository: GalleryRepository
    @Mock
    lateinit var galleryObserver: Observer<GalleryUIModel>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun before() {
        startKoin {
            modules(listOf(appModule, remoteDataSourceModule()))
        }
        MockitoAnnotations.initMocks(this)
        mainViewModel = MainViewModel(galleryRepository, TestSchedulerProvider())
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun testGetGallerySuccess() {
        val returnedData = ArrayList<GalleryData>()

        Mockito.`when`(galleryRepository.getGallery()).thenReturn(Single.just(returnedData))

        mainViewModel.galleryUIModel.observeForever(galleryObserver)

        mainViewModel.getGallery()

        Mockito.verify(galleryObserver)
            .onChanged(GalleryUIModel(isSuccess = true, galleryList = returnedData))

    }

    @Test
    fun testGetGalleryFailed() {
        val returnedData = IllegalStateException("error!")

        Mockito.`when`(galleryRepository.getGallery()).thenReturn(Single.error(returnedData))

        mainViewModel.galleryUIModel.observeForever(galleryObserver)

        mainViewModel.getGallery()

        Mockito.verify(galleryObserver).onChanged(GalleryUIModel(error = returnedData))

    }


}