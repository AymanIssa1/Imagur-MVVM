package com.example.imagurtask.di

import com.example.imagurtask.domain.GalleryRepository
import com.example.imagurtask.domain.GalleryRepositoryImp
import com.example.imagurtask.ui.details.DetailsViewModel
import com.example.imagurtask.ui.main.MainViewModel
import com.example.imagurtask.util.coroutines.ApplicationSchedulerProvider
import com.example.imagurtask.util.coroutines.SchedulerProvider
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Gallery Data Repository
    single { GalleryRepositoryImp(get()) as GalleryRepository }

    // Rx Schedulers
    single<SchedulerProvider>(createdAtStart = true) { ApplicationSchedulerProvider() }

    viewModel<MainViewModel>()
    viewModel { (itemId: String) -> DetailsViewModel(itemId, get(), get()) }

}