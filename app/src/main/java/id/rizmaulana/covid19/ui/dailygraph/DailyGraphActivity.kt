package id.rizmaulana.covid19.ui.dailygraph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.detail.DetailActivity

class DailyGraphActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_graph)
    }

    override fun observeChange() {

    }

    companion object{
        fun startActivity(context: Context?, type: Int) = context?.startActivity(
            Intent(context, DetailActivity::class.java)
        )
    }
}
