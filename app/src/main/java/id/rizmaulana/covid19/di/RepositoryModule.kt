package id.rizmaulana.covid19.di

import id.rizmaulana.covid19.data.repository.AppRepository
import id.rizmaulana.covid19.data.repository.Repository
import org.koin.dsl.module

/**
 * rizmaulana 2020-02-24.
 */
 val repositoryModule = module {
    factory<Repository> {
        AppRepository(get(), get())
    }
}