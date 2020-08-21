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
import id.rizmaulana.covid19.databinding.ActivityDetailBinding
import id.rizmaulana.covid19.ui.adapter.createAdapter
import id.rizmaulana.covid19.ui.adapter.viewholders.LocationItem
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.base.BaseViewItem
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
            } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }
        }
    }

    private val detailAdapter = createAdapter(
        ::onItemClick, ::onLongItemClick
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //This is needed for fixing bottom sheet behaviour when collapsing
        //Because soft keyboard changing the layout height
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        initView()
        attachMaps()
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
        observe(viewModel.detailListViewItems, ::onListChanged)
    }

    private fun onListChanged(data: List<BaseViewItem>) {
        detailAdapter.submitList(data)
    }

    private fun attachMaps() {
        mapsFragment = VisualMapsFragment.newInstance(caseType)
        mapsFragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.layout_visual, it)
                .commitAllowingStateLoss()
        }
    }

    private fun showItemListDialog(item: LocationItem) {
        val items = resources.getStringArray(R.array.detail_item_menu).toMutableList()

        if (item.isPinned.not()) {
            AlertDialog.Builder(this)
                .setItems(items.toTypedArray()) { dialog, which ->
                    viewModel.putPinnedRegion(item.compositeKey())
                }
                .show()
        }
    }

    private fun onLongItemClick(item: BaseViewItem, view: View) {
        when (item) {
            is LocationItem -> {
                showItemListDialog(item)
            }
        }
    }

    private fun onItemClick(item: BaseViewItem, view: View) {
        when (item) {
            is LocationItem -> {
                when (view.id) {
                    R.id.root -> {
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
                        view.postDelayed({
                            hideSoftKeyboard()
                            collapseBottomSheet()
                            mapsFragment?.selectItem(item)
                        }, 100)
                    }
                    R.id.relative_pinned -> {
                        viewModel.removePinnedRegion()
                    }
                }
            }
        }
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

