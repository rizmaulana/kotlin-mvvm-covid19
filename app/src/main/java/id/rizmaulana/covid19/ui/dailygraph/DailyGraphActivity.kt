package id.rizmaulana.covid19.ui.dailygraph

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.ColorRes
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ActivityDailyGraphBinding
import id.rizmaulana.covid19.ui.adapter.DailyAdapter
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItem
import id.rizmaulana.covid19.ui.base.BaseActivity
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
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
        observe(viewModel.dailyItems, ::onDailyDataLoaded)
        observe(viewModel.loading, ::swipeLoading)
    }

    private fun swipeLoading(loading: Boolean) {
        with(binding.swipeRefresh) {
            post { isRefreshing = loading }
        }
    }

    private fun onDailyDataLoaded(daily: List<DailyItem>) {
        dailyAdapter.addAll(daily)
        setupChart(daily)
    }

    private fun setupChart(dailyData: List<DailyItem>) {
        val daily = dailyData.reversed()
        with(binding.lineChart) {
            animateX(1500)
            legend.textColor = color(R.color.white)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = color(R.color.cool_grey)

            axisLeft.textColor = color(R.color.cool_grey)
//            axisRight.textColor = color(R.color.cool_grey)
            description.isEnabled = false

            axisRight.enableGridDashedLine(10f, 10f, 2f)
            axisLeft.enableGridDashedLine(10f, 10f, 2f)
            xAxis.enableGridDashedLine(10f, 10f, 2f)

            val dates = daily.map { NumberUtils.formatShortDate(it.reportDate) }
            xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dates[value.toInt()]
                }
            }

        }

        val totalCaseDataSet = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.mainlandChina.plus(dailyItem.otherLocations).toFloat(),
                    NumberUtils.formatTime(dailyItem.reportDate)
                )
            }, getString(R.string.total_case)
        )

        val confirmedDataSet = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.deltaConfirmed.toFloat(),
                    NumberUtils.formatTime(dailyItem.reportDate)
                )
            }, getString(R.string.confirmed)
        ).apply {
            setLineChartStyle(this, R.color.color_confirmed)
        }

        val recoveredDataSet = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.deltaRecovered.toFloat(),
                    NumberUtils.formatTime(dailyItem.reportDate)
                )
            }, getString(R.string.recovered)
        ).apply {
            setLineChartStyle(this, R.color.color_recovered)
        }

        val lineData = LineData(confirmedDataSet, recoveredDataSet)
        binding.lineChart.data = lineData
    }

    private fun setLineChartStyle(lineDataSet: LineDataSet, @ColorRes colorResId: Int) {
        with(lineDataSet) {
            color = color(colorResId)
            lineWidth = 2f
            circleRadius = 1f
            setDrawCircleHole(false)
            setCircleColor(color(colorResId))
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextColor = color(R.color.white)

            setDrawFilled(true)
            fillColor = color(colorResId)
            fillAlpha = 60
        }
    }

    companion object {
        fun startActivity(context: Context?) = context?.startActivity(
            Intent(context, DailyGraphActivity::class.java)
        )
    }
}
