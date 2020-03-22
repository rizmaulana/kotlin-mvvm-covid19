package id.rizmaulana.covid19.ui.dailygraph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import id.rizmaulana.covid19.databinding.ActivityDailyGraphBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel

class DailyGraphActivity : BaseActivity() {

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
        binding.fabBack.setOnClickListener { onBackPressed() }
//        with(binding.recyclerView) {
//            adapter = dailyAdapter
//            setHasFixedSize(true)
//        }
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadRemoteDailyData()
        }
    }

    override fun observeChange() {
        observe(viewModel.toastMessage, ::showSnackbarMessage)
        observe(viewModel.loading, ::swipeLoading)
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
