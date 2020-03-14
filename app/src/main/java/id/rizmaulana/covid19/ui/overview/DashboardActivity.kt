package id.rizmaulana.covid19.ui.overview

import android.os.Bundle
import android.util.Log
import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ActivityDashboardBinding
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactoryImpl
import id.rizmaulana.covid19.ui.adapter.VisitableRecyclerAdapter
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.adapter.viewholders.OverviewItem
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.ui.detail.DetailActivity
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {

    private val viewModel by viewModel<DashboardViewModel>()
    private val dailyAdapter by lazy { VisitableRecyclerAdapter(ItemTypeFactoryImpl(), ::onItemClicked) }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        viewModel.loadOverviewAndDaily()
    }

    private fun initView() {
        with(binding.recyclerView) {
            adapter = dailyAdapter
            setHasFixedSize(true)
        }

        binding.fab.setOnClickListener { permission(CaseType.FULL) }
    }

    private fun permission(state: Int) {
        permission {
            DetailActivity.startActivity(this, state)
        }
    }

    override fun observeChange() {
        observe(viewModel.loading, ::loading)
        observe(viewModel.items, ::onDataLoaded)
        observe(viewModel.errorMessage, ::showSnackbarMessage)
    }

    private fun onDataLoaded(items: List<BaseViewItem>) {
        dailyAdapter.submitList(items)
    }

    private fun onItemClicked(viewItem: BaseViewItem, view: View) {
        when(viewItem) {
            is OverviewItem -> {
                when(view.id) {
                    R.id.layout_confirmed -> permission(CaseType.CONFIRMED)
                    R.id.layout_recovered -> permission(CaseType.RECOVERED)
                    R.id.layout_death -> permission(CaseType.DEATHS)
                }
            }
            is DailyItem -> {
                Log.e("DailyItem", "DailyItem Click: ${viewItem.deltaConfirmed}")
            }
        }
    }
}
