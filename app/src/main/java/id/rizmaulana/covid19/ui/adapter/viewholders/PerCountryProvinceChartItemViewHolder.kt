package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyPercountryGraphBinding
import id.rizmaulana.covid19.databinding.ItemProvincePercountryGraphBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.ext.color

data class PerCountryProvinceGraphItem(
    val listData: List<PerCountryProvinceItem>
) : BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_province_percountry_graph
}

data class PerCountryProvinceItem(
    val id: Int,
    val name: String,
    val totalConfirmed: Int,
    val totalRecovered: Int,
    val totalDeath: Int
)

class PerCountryProvinceGraphItemViewHolder(itemView: View) :
    BaseViewHolder<PerCountryProvinceGraphItem>(itemView) {
    private val binding: ItemProvincePercountryGraphBinding =
        ItemProvincePercountryGraphBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        binding.root.setOnClickListener { listener.invoke(it) }
    }

    override fun bind(item: PerCountryProvinceGraphItem) {
        setupChart(item.listData)
        setupData(item.listData)
    }

    private fun setupChart(data: List<PerCountryProvinceItem>) {
        with(binding.barChart) {
            animateX(1500)
            legend.textColor = color(R.color.white)
            setMaxVisibleValueCount(40)

            xAxis.isEnabled = false
            axisLeft.textColor = color(R.color.cool_grey)
            axisRight.textColor = color(R.color.cool_grey)
            description.isEnabled = false

            axisRight.enableGridDashedLine(10f, 10f, 2f)
            axisLeft.enableGridDashedLine(10f, 10f, 2f)
            xAxis.enableGridDashedLine(10f, 10f, 2f)

        }

    }

    private fun setupData(data: List<PerCountryProvinceItem>) {
        val values = data.mapIndexed { index, perCountryProvinceItem ->
            BarEntry(
                index.toFloat(), floatArrayOf(
                    perCountryProvinceItem.totalConfirmed.toFloat(),
                    perCountryProvinceItem.totalDeath.toFloat(),
                    perCountryProvinceItem.totalRecovered.toFloat()
                )
            )
        }
        val barDataSet = BarDataSet(values, "COVID19")
        val dataSet = arrayListOf<IBarDataSet>()
        dataSet.add(barDataSet)
        binding.barChart.data = BarData(dataSet)
        binding.barChart.invalidate()
    }


    companion object {
        const val LAYOUT = R.layout.item_province_percountry_graph
    }
}