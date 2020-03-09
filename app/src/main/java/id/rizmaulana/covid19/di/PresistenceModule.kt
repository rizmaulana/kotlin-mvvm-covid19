package id.rizmaulana.covid19.di

import id.rizmaulana.covid19.data.source.pref.AppPrefSource
import org.koin.dsl.module

/**
 * rizmaulana@live.com 2019-06-14.
 */

val persistenceModule = module {
    single {
        AppPrefSource()
    }
}

