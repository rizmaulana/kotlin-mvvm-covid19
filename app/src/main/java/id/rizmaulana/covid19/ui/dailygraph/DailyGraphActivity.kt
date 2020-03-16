package id.rizmaulana.covid19.ui.dailygraph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import id.rizmaulana.covid19.databinding.ActivityDashboardBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel

class DailyGraphActivity : BaseActivity() {

    private val viewModel by viewModel<DailyGraphViewModel>()
    private lateinit var binding: ActivityDashboardBinding
    private val dailyAdapter by lazy {
        DailyAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        viewModel.loadDailyData()
    }

    private fun initView() {
        binding.fab.setOnClickListener { onBackPressed() }
        with(binding.recyclerView) {
            adapter = dailyAdapter
            setHasFixedSize(true)
        }
    }

    override fun observeChange() {
        observe(viewModel.toastMessage, ::showSnackbarMessage)
        observe(viewModel.dailyItems, ::onDailyDataLoaded)
    }

    private fun onDailyDataLoaded(daily: List<DailyItem>) {
        dailyAdapter.addAll(daily)
    }

    companion object {
        fun startActivity(context: Context?) = context?.startActivity(
            Intent(context, DailyGraphActivity::class.java)
        )
    }
}
