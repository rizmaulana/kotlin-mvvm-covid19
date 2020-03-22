package id.rizmaulana.covid19.ui.dailygraph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.databinding.FragmentDailyGraphBinding
import id.rizmaulana.covid19.ui.base.BaseFragment
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.sharedViewModel

const val TOTAL_STATE = 0
const val DELTA_STATE = 1

class DailyGraphFragment : BaseFragment() {

    private val viewModel by sharedViewModel<DailyGraphViewModel>()

    private var _binding: FragmentDailyGraphBinding? = null
    private val binding get() = _binding!!

    private var currentState = TOTAL_STATE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // no op
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // no op
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentState = it.position
                    setupChart(viewModel.dailyItems.value.orEmpty())
                }
            }
        })
    }

    override fun observeChange() {
        observe(viewModel.dailyItems, ::onDailyDataLoaded)
    }

    private fun setupChart(dailyData: List<CovidDaily>) {
        val daily = dailyData.reversed()
        with(binding.lineChart) {
            animateX(1500)
            legend.textColor = color(R.color.white)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = color(R.color.cool_grey)

            axisLeft.textColor = color(R.color.cool_grey)
            axisRight.isEnabled = false
//            axisRight.textColor = color(R.color.cool_grey)
            description.isEnabled = false

//            axisRight.enableGridDashedLine(10f, 10f, 2f)
            axisLeft.enableGridDashedLine(10f, 10f, 2f)
            xAxis.enableGridDashedLine(10f, 10f, 2f)

            val dates = daily.map { NumberUtils.formatShortDate(it.reportDate) }
            xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dates[value.toInt()]
                }
            }

        }

        val totalConfirmed = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.totalConfirmed.toFloat(),
                    NumberUtils.formatTime(dailyItem.reportDate)
                )
            }, getString(R.string.confirmed)
        ).apply {
            setLineChartStyle(this, R.color.color_confirmed)
        }

        val totalRecovered = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.totalRecovered.toFloat(),
                    NumberUtils.formatTime(dailyItem.reportDate)
                )
            }, getString(R.string.recovered)
        ).apply {
            setLineChartStyle(this, R.color.color_recovered)
        }

        val deltaConfirmed = LineDataSet(
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

        val deltaRecovered = LineDataSet(
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

        val lineData = when (currentState) {
            TOTAL_STATE -> LineData(totalConfirmed, totalRecovered)
            else -> LineData(deltaConfirmed, deltaRecovered)
        }
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
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

    private fun onDailyDataLoaded(daily: List<CovidDaily>) {
//        dailyAdapter.addAll(daily)
        setupChart(daily)
    }

    companion object {
        fun newInstance(): DailyGraphFragment = DailyGraphFragment()
    }

}