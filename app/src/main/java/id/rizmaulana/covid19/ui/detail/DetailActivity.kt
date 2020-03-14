package id.rizmaulana.covid19.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding.widget.RxTextView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.databinding.ActivityDetailBinding
import id.rizmaulana.covid19.ui.adapter.DetailAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.maps.VisualMapsFragment
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailActivity : BaseActivity() {

    private val viewModel by viewModel<DetailViewModel>()
    private var mapsFragment: VisualMapsFragment? = null
    private lateinit var binding: ActivityDetailBinding

    private val caseType by lazy {
        intent.getIntExtra(CASE_TYPE, CaseType.FULL)
    }

    private val detailAdapter by lazy {
        DetailAdapter(caseType, {
            binding.layoutContent.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            hideSoftKeyboard()
            mapsFragment?.selectItem(it)
        }, {
            showItemListDialog(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        viewModel.getDetail(caseType)
    }

    private fun initView() {
        with(binding) {
            recyclerView.adapter = detailAdapter
            fabBack.setOnClickListener { onBackPressed() }
        }
        RxTextView.textChanges(binding.txtSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe {
                viewModel.findLocation(it.toString())
            }
    }

    override fun observeChange() {
        observe(viewModel.loading, ::loading)
        observe(viewModel.errorMessage, ::showSnackbarMessage)
        observe(viewModel.detailListLiveData, ::onListChanged)
    }

    private fun onListChanged(data: List<CovidDetail>) {
        detailAdapter.addAll(data)
        if (binding.txtSearch.text.toString().isEmpty()) {
            attachMaps(data)
        }
    }

    private fun attachMaps(data: List<CovidDetail>) {
        mapsFragment = VisualMapsFragment.newInstance(ArrayList(data), caseType)
        mapsFragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.layout_visual, it)
                .commitAllowingStateLoss()
        }
    }

    private fun showItemListDialog(dataContext: CovidDetail) {
        AlertDialog.Builder(this)
            .setItems(resources.getStringArray(R.array.detail_item_menu)) { dialog, which ->
                viewModel.putPrefCountry(dataContext)
            }
            .show()
    }

    companion object {
        private const val CASE_TYPE = "case_type"

        fun startActivity(context: Context?, type: Int) = context?.startActivity(
            Intent(context, DetailActivity::class.java).apply {
                putExtra(CASE_TYPE, type)
            }
        )
    }

}
