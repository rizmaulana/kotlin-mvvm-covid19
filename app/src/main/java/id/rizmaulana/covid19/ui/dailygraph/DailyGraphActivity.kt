package id.rizmaulana.covid19.ui.dailygraph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ActivityDailyGraphBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.util.ext.observe
import kotlinx.android.synthetic.main.activity_daily_graph.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class DailyGraphActivity : BaseActivity(), DailyGraphFragment.DailyListener {

    private val viewModel by viewModel<DailyGraphViewModel>()
    private lateinit var binding: ActivityDailyGraphBinding
    private val dailyAdapter by lazy {
        DailyAdapter {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        viewModel.loadCacheDailyData()
    }

    private fun initView() {
        val fragment = DailyGraphFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, fragment, "").commit()
        binding.fabBack.setOnClickListener { onBackPressed() }
//        with(binding.recyclerView) {
//            adapter = dailyAdapter
//            setHasFixedSize(true)
//        }
//        binding.swipeRefresh.setOnRefreshListener {
//            viewModel.loadRemoteDailyData()
//        }
    }

    override fun observeChange() {
        observe(viewModel.toastMessage, ::showSnackbarMessage)
        observe(viewModel.loading, ::swipeLoading)
    }

    override fun onSwap() {

    }

    private fun swipeLoading(loading: Boolean) {
        with(binding.swipeRefresh) {
            post { isRefreshing = loading }
        }
    }

    companion object {
        fun startActivity(context: Context?) = context?.startActivity(
            Intent(context, DailyGraphActivity::class.java)
        )
    }
}
