package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import androidx.annotation.ColorRes
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyPercountryGraphBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.getString

data class PerCountryDailyGraphItem(
    val listData: List<PerCountryDailyItem>
) : BaseViewItem

class PerCountryDailyGraphItemViewHolder(itemView: View) :
    BaseViewHolder<PerCountryDailyGraphItem>(itemView) {
    private val binding: ItemDailyPercountryGraphBinding =
        ItemDailyPercountryGraphBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        binding.root.setOnClickListener { listener.invoke(it) }
    }

    override fun bind(item: PerCountryDailyGraphItem) {
        setupChart(item.listData)
        setupData(item.listData)
    }

    private fun setupChart(data: List<PerCountryDailyItem>) {
        with(binding.lineChart) {
            animateX(1500)
            legend.textColor = color(R.color.white)

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.textColor = color(R.color.cool_grey)

            axisLeft.textColor = color(R.color.cool_grey)
            axisRight.textColor = color(R.color.cool_grey)
            description.isEnabled = false

            axisRight.enableGridDashedLine(10f, 10f, 2f)
            axisLeft.enableGridDashedLine(10f, 10f, 2f)
            xAxis.enableGridDashedLine(10f, 10f, 2f)

            val dates = data.map { NumberUtils.formatTime(it.date) }
            xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return dates[value.toInt()]
                }
            }

        }

    }

    private fun setupData(daily: List<PerCountryDailyItem>) {
        val totalConfirmed = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.totalConfirmed.toFloat(),
                    NumberUtils.formatTime(dailyItem.date)
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
                    NumberUtils.formatTime(dailyItem.date)
                )
            }, getString(R.string.recovered)
        ).apply {
            setLineChartStyle(this, R.color.color_recovered)
        }
        val totalDeath = LineDataSet(
            daily.mapIndexed { index, dailyItem ->
                Entry(
                    index.toFloat(),
                    dailyItem.totalDeath.toFloat(),
                    NumberUtils.formatTime(dailyItem.date)
                )
            }, getString(R.string.deaths)
        ).apply {
            setLineChartStyle(this, R.color.color_death)
        }
        val lineData = LineData(totalConfirmed, totalDeath, totalRecovered)
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
            fillAlpha = 100
        }
    }


    companion object {
        const val LAYOUT = R.layout.item_daily_percountry_graph
    }
}