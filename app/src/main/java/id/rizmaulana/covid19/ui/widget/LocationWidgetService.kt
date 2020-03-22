package id.rizmaulana.covid19.ui.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import id.rizmaulana.covid19.data.repository.Repository
import org.koin.android.ext.android.inject

/**
 * rizmaulana 22/03/20.
 */
class LocationWidgetService : Service(){
    private val appRepository by inject<Repository>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val allWidget = intent?.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)


        allWidget?.forEach {

        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}