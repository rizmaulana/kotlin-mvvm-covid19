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
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.data.model.CovidOverview
import id.rizmaulana.covid19.databinding.ActivityDashboardBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.ui.detail.DetailActivity
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.gone
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {

    private val viewModel by viewModel<DashboardViewModel>()
    private val dailyAdapter by lazy { DailyAdapter() }
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        viewModel.getOverview()
        viewModel.getDailyUpdate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPinUpdate()
    }

    private fun initView() {
        with(binding.recyclerView) {
            adapter = dailyAdapter
            isNestedScrollingEnabled = false
        }

        with(binding) {
            layoutConfirmed.setOnClickListener { permission(CaseType.CONFIRMED) }
            layoutRecovered.setOnClickListener { permission(CaseType.RECOVERED) }
            layoutDeath.setOnClickListener { permission(CaseType.DEATHS) }
            fab.setOnClickListener { permission(CaseType.FULL) }
        }
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
        observe(viewModel.toastMessage, ::showSnackbarMessage)
        observe(viewModel.pinData, ::handlePinnedUpdate)
    }

    private fun overviewLoaded(overview: CovidOverview) {
        with(binding) {
            startNumberChangeAnimator(overview.confirmed?.value, txtConfirmed)
            startNumberChangeAnimator(overview.deaths?.value, txtDeaths)
            startNumberChangeAnimator(overview.recovered?.value, txtRecovered)
        }

        val pieDataSet = PieDataSet(
            listOf(
                PieEntry(overview.confirmed?.value?.toFloat() ?: 0f, "Confirmed"),
                PieEntry(overview.recovered?.value?.toFloat() ?: 0f, "Recovered"),
                PieEntry(overview.deaths?.value?.toFloat() ?: 0f, "Deaths")
            ), "COVID19"
        )

        binding.txtCases.text = NumberUtils.numberFormat(
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

        with(binding.pieChart) {
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

    private fun handlePinnedUpdate(data: CovidDetail?) {
        with(binding.countryInfo) {
            data?.let { detail ->
                val lastUpdate = NumberUtils.formatTime(detail.lastUpdate)
                txtLocation.text = detail.locationName
                txtUpdate.text = getString(R.string.information_last_update, lastUpdate)
                txtData.text = "${detail.confirmed ?: '-'}"
                txtRcv.text = "${detail.recovered ?: '-'}"
                txtDeath.text = "${detail.deaths ?: '-'}"
            } ?: root.gone()
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
