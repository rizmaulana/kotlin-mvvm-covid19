package id.rizmaulana.covid19.ui.overview

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.detail.DetailActivity
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.observe
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {

    private val viewModel by viewModel<DashboardViewModel>()
    private val dailyAdapter by lazy { DailyAdapter() }

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
            isNestedScrollingEnabled = false
        }

        layout_confirmed.setOnClickListener { permission(CaseType.CONFIRMED) }
        layout_recovered.setOnClickListener { permission(CaseType.RECOVERED) }
        layout_death.setOnClickListener { permission(CaseType.DEATHS) }
        fab.setOnClickListener { permission(CaseType.FULL) }
    }

    private fun permission(state: Int) {
        permission {
            DetailActivity.startActivity(this, state)
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

        val colors = arrayListOf(
            color(R.color.color_confirmed),
            color(R.color.color_recovered),
            color(R.color.color_death)
        )

        pieDataSet.colors = colors

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)

        with(pie_chart){
            data = pieData
            legend.isEnabled = false
            description = null
            holeRadius = PIE_RADIUS
            setHoleColor(Color.parseColor("#171B1E"))
            setDrawEntryLabels(false)
            animateY(PIE_ANIMATION_DURATION, Easing.EaseInOutQuart)
            invalidate()
        }

    }

    private fun startNumberChangeAnimator(finalValue: Int?, view: TextView) {
        val initialValue = NumberUtils.extractDigit(view.text.toString())
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue ?: 0)
        valueAnimator.duration = TEXT_ANIMATION_DURATION
        valueAnimator.addUpdateListener { value ->
            view.text = NumberUtils.numberFormat(value.animatedValue.toString().toInt())
        }
        valueAnimator.start()
    }

    private fun onDailyLoaded(daily: List<CovidDaily>) {
        dailyAdapter.addAll(daily)
    }

    companion object {
        private const val TEXT_ANIMATION_DURATION = 1000L
        private const val PIE_ANIMATION_DURATION = 1500
        private const val PIE_RADIUS = 75f
    }

}
