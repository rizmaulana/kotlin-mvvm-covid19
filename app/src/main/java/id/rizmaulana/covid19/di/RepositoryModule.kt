package id.rizmaulana.covid19.di

import id.rizmaulana.covid19.data.repository.AppRepository
import org.koin.dsl.module

/**
 * rizmaulana 2020-02-24.
 */
 val repositoryModule = module {
    factory {
        AppRepository(get(), get())
    }
}