package id.rizmaulana.covid19.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding.widget.RxTextView
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.ui.adapter.DetailAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.maps.VisualMapsFragment
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.ext.observe
import kotlinx.android.synthetic.main.activity_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailActivity : BaseActivity() {

    private val viewModel by viewModel<DetailViewModel>()
    private var mapsFragment: VisualMapsFragment? = null

    private val caseType by lazy {
        intent.getIntExtra(CASE_TYPE, CaseType.FULL)
    }

    private val detailAdapter by lazy {
        DetailAdapter(caseType) {
            layout_content.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            hideSoftKeyboard()
            mapsFragment?.selectItem(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initView()
        viewModel.getDetail(caseType)
    }

    private fun initView() {
        recycler_view.adapter = detailAdapter
        fab_back.setOnClickListener { onBackPressed() }
        RxTextView.textChanges(txt_search)
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
        if (txt_search.text.toString().isEmpty()) {
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

    companion object {
        private const val CASE_TYPE = "case_type"

        fun startActivity(context: Context?, type: Int) = context?.startActivity(
            Intent(context, DetailActivity::class.java).apply {
                putExtra(CASE_TYPE, type)
            }
        )
    }

}
