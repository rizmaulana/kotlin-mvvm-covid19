package id.rizmaulana.covid19.di

import androidx.recyclerview.widget.LinearLayoutManager
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.util.rx.AppSchedulerProvider
import id.rizmaulana.covid19.util.rx.SchedulerProvider
import org.koin.dsl.module
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * rizmaulana 2020-02-24.
 */
val appModule = module {

    single {
        CalligraphyConfig.Builder()
            .setDefaultFontPath("fonts/GoogleSans-Regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
    }

    factory<SchedulerProvider> {
        AppSchedulerProvider()
    }


    factory {
        LinearLayoutManager(get())
    }

}