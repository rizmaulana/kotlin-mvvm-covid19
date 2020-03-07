package id.rizmaulana.covid19.ui.overview

import android.Manifest
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.detail.DetailActivity
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.observe
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {

    private val viewModel by viewModel<DashboardViewModel>()
    private val dailyAdapter by lazy { DailyAdapter() }
    private val dailyLayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initView()

        viewModel.getOverview()
        viewModel.getDailyUpdate()

    }

    private fun initView() {
        with(recycler_view) {
            adapter = dailyAdapter
            layoutManager = dailyLayoutManager
            isNestedScrollingEnabled = false
        }
        layout_confirmed.setOnClickListener {
            runWithDexter(permissionsList) {
                DetailActivity.startActivity(
                    this,
                    CaseType.CONFIRMED
                )
            }

        }
        layout_recovered.setOnClickListener {
            runWithDexter(permissionsList) {
                DetailActivity.startActivity(
                    this,
                    CaseType.RECOVERED
                )
            }

        }
        layout_death.setOnClickListener {
            runWithDexter(permissionsList) {
                DetailActivity.startActivity(this, CaseType.DEATHS)
            }
        }

    }

    override fun observeChange() {
        observe(viewModel.loading, ::loading)
        observe(viewModel.overviewData, ::overviewLoaded)
        observe(viewModel.dailyListData, ::onDailyLoaded)
        observe(viewModel.errorMessage, ::showSnackbarMessage)
    }

    private fun overviewLoaded(overview: CovidOverview) {
        startNumberChangeAnimator(overview.confirmed?.value, txt_confirmed)
        startNumberChangeAnimator(overview.deaths?.value, txt_deaths)
        startNumberChangeAnimator(overview.recovered?.value, txt_recovered)

        val pieDataSet = PieDataSet(
            listOf(
                PieEntry(overview.confirmed?.value?.toFloat() ?: 0f, "Confirmed"),
                PieEntry(overview.recovered?.value?.toFloat() ?: 0f, "Recovered"),
                PieEntry(overview.deaths?.value?.toFloat() ?: 0f, "Deaths")
            ), "COVID19"
        )
        txt_cases.text = NumberUtils.numberFormat(
            (overview.confirmed?.value?.plus(
                overview.recovered?.value ?: 0
            )?.plus(overview.deaths?.value ?: 0) ?: 0)
        ).toString()

        val colors =
            arrayListOf(
                Color.parseColor("#F2B900"),
                Color.parseColor("#00CC99"),
                Color.parseColor("#F76353")
            )
        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        with(pie_chart){
            data = pieData
            legend.isEnabled = false
            description = null
            holeRadius = 75f
            setHoleColor(Color.parseColor("#171B1E"))
            setDrawEntryLabels(false)
            animateY(1500, Easing.EaseInOutQuart)
            invalidate()
        }

    }

    private fun startNumberChangeAnimator(finalValue: Int?, view: TextView) {
        val initialValue = NumberUtils.extractDigit(view.text.toString())
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue ?: 0)
        valueAnimator.duration = 1000

        valueAnimator.addUpdateListener { value ->
            view.text = NumberUtils.numberFormat(value.animatedValue.toString().toInt())
        }
        valueAnimator.start()
    }

    private val permissionsList = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private fun runWithDexter(permissions: List<String>, action: () -> Unit) {
        Dexter.withActivity(this).withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.deniedPermissionResponses.size > 0) {
                            runWithDexter(
                                it.deniedPermissionResponses.map { p -> p.permissionName },
                                action
                            )
                        } else {
                            action.invoke()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener { showSnackbarError(Constant.ERROR_MESSAGE) }
            .check()
    }

    private fun onDailyLoaded(daily: List<CovidDaily>) {
        dailyAdapter.addAll(daily)
    }


}
