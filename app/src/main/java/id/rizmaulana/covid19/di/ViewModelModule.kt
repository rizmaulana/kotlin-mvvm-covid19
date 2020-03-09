package id.rizmaulana.covid19.di

import id.rizmaulana.covid19.ui.detail.DetailViewModel
import id.rizmaulana.covid19.ui.overview.DashboardViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * rizmaulana 2020-02-24.
 */

val viewModelModule = module {
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { DetailViewModel(get(), get()) }
}