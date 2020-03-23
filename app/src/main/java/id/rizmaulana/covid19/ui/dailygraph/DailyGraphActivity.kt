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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyGraphBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        viewModel.loadCacheDailyData()
    }

    private fun initView() {
        val fragment = DailyGraphFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.frame_layout, fragment, "graph_fragment").commit()
        binding.fabBack.setOnClickListener { onBackPressed() }
    }

    override fun observeChange() {
        observe(viewModel.toastMessage, ::showSnackbarMessage)
    }

    override fun onSwap() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, DailyDataListFragment.newInstance(), "list_fragment")
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        fun startActivity(context: Context?) = context?.startActivity(
            Intent(context, DailyGraphActivity::class.java)
        )
    }
}
