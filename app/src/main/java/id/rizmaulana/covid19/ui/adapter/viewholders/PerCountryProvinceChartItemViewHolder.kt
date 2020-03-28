package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemProvincePercountryGraphBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.getString
import id.rizmaulana.covid19.util.ext.visible

data class PerCountryProvinceGraphItem(
    val listData: List<PerCountryProvinceItem>
) : BaseViewItem

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

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {
                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val barEnrty = e as? BarEntry
                    with(binding) {
                        barEnrty?.let { dataBarEnty ->
                            layoutData.visible()
                            txtProvince.text = dataBarEnty.data.toString()
                            txtConfirmed.text = getString(
                                R.string.confirmed_case_count,
                                dataBarEnty.yVals[0].toInt().toString()
                            )
                            txtDeath.text = getString(
                                R.string.death_case_count,
                                dataBarEnty.yVals[1].toInt().toString()
                            )
                            txtRecovered.text = getString(
                                R.string.recovered_case_count,
                                dataBarEnty.yVals[2].toInt().toString()
                            )

                        }

                    }
                }
            })

        }

    }

    private fun setupData(data: List<PerCountryProvinceItem>) {
        val values = data.mapIndexed { index, perCountryProvinceItem ->
            BarEntry(
                index.toFloat(), floatArrayOf(
                    perCountryProvinceItem.totalConfirmed.toFloat(),
                    perCountryProvinceItem.totalDeath.toFloat(),
                    perCountryProvinceItem.totalRecovered.toFloat()
                ), perCountryProvinceItem.name
            )
        }
        val barDataSet = BarDataSet(values, getString(R.string.case_per_province_chart))
        barDataSet.stackLabels = arrayOf(
            getString(R.string.confirmed),
            getString(R.string.deaths),
            getString(R.string.recovered)
        )
        barDataSet.setColors(
            color(R.color.color_confirmed),
            color(R.color.color_death),
            color(R.color.color_recovered)
        )
        barDataSet.setDrawValues(false)
        val dataSet = arrayListOf<IBarDataSet>()
        dataSet.add(barDataSet)
        binding.barChart.data = BarData(dataSet)
        binding.barChart.invalidate()
    }


    companion object {
        const val LAYOUT = R.layout.item_province_percountry_graph
    }
}