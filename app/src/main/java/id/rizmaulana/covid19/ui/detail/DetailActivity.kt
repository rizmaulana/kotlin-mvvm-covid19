package id.rizmaulana.covid19.ui.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jakewharton.rxbinding.widget.RxTextView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.databinding.ActivityDetailBinding
import id.rizmaulana.covid19.ui.adapter.DetailAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.maps.VisualMapsFragment
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class DetailActivity : BaseActivity() {

    private val viewModel by viewModel<DetailViewModel>()
    private var mapsFragment: VisualMapsFragment? = null
    private lateinit var binding: ActivityDetailBinding
    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ViewGroup>

    private val caseType by lazy {
        intent.getIntExtra(CASE_TYPE, CaseType.FULL)
    }

    private val bottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            //nothing
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                hideSoftKeyboard()
                binding.txtSearch.clearFocus()
            }
        }
    }

    private val detailAdapter by lazy {
        DetailAdapter(caseType, ::onAdapterItemClicked, ::showItemListDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //This is needed for fixing bottom sheet behaviour when collapsing
        //Because soft keyboard changing the layout height
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        initView()
        viewModel.getDetail(caseType)
    }

    private fun initView() {
        with(binding) {
            recyclerView.adapter = detailAdapter
            bottomSheetBehaviour = BottomSheetBehavior.from(layoutBottomSheet)
            bottomSheetBehaviour.setBottomSheetCallback(bottomSheetBehaviorCallback)

            fabBack.setOnClickListener { onBackPressed() }
            txtSearch.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) expandBottomSheet() }
            imgClear.setOnClickListener { txtSearch.setText("") }
        }
        RxTextView.textChanges(binding.txtSearch)
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                binding.imgClear.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
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

    private fun onAdapterItemClicked(detail: CovidDetail) {
        hideSoftKeyboard()
        collapseBottomSheet()
        mapsFragment?.selectItem(detail)
    }

    private fun collapseBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun expandBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
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
