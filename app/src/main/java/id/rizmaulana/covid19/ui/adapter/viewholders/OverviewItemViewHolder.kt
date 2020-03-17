package id.rizmaulana.covid19.ui.adapter.viewholders

import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemOverviewBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.color
import id.rizmaulana.covid19.util.ext.visible

data class OverviewItem(
    val confirmed: Int = 0,
    val recovered: Int = 0,
    val deaths: Int = 0
): BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_overview
}

class OverviewItemViewHolder(itemView: View) : BaseViewHolder<OverviewItem>(itemView) {
    private val binding: ItemOverviewBinding = ItemOverviewBinding.bind(itemView)
    private var confirmed: Int = 0
    private var recovered: Int = 0
    private var deaths: Int = 0

    private fun startNumberChangeAnimator(finalValue: Int?, view: TextView) {
        val initialValue = NumberUtils.extractDigit(view.text.toString())
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue ?: 0)
        valueAnimator.duration = TEXT_ANIMATION_DURATION
        valueAnimator.addUpdateListener { value ->
            view.text = NumberUtils.numberFormat(value.animatedValue.toString().toInt())
        }
        valueAnimator.start()
    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        with(binding) {
            layoutConfirmed.setOnClickListener { listener.invoke(it) }
            layoutRecovered.setOnClickListener { listener.invoke(it) }
            layoutDeath.setOnClickListener { listener.invoke(it) }
        }
    }

    override fun bind(item: OverviewItem) {
        if(item.confirmed == confirmed && item.recovered == recovered && item.deaths == deaths) return

        with(binding){
            startNumberChangeAnimator(item.confirmed, txtConfirmed)
            startNumberChangeAnimator(item.deaths, txtDeaths)
            startNumberChangeAnimator(item.recovered, txtRecovered)
        }

        confirmed = item.confirmed
        recovered = item.recovered
        deaths = item.deaths

        val context = itemView.context
        val pieDataSet = PieDataSet(
            listOf(
                PieEntry(item.confirmed.toFloat(), context.getString(R.string.confirmed)),
                PieEntry(item.recovered.toFloat(), context.getString(R.string.recovered)),
                PieEntry(item.deaths.toFloat(), context.getString(R.string.deaths))
            ), context.getString(R.string.covid19)
        )

        binding.txtCases.text = NumberUtils.numberFormat(
            (item.confirmed
                .plus(item.recovered)
                .plus(item.deaths))
        ).toString()

        with(binding.root.context) {
            val colors = arrayListOf(
                color(R.color.color_confirmed),
                color(R.color.color_recovered),
                color(R.color.color_death)
            )

            pieDataSet.colors = colors
        }

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(false)
        with(binding.pieChart) {
            if (data == pieData) return
            data = pieData
            legend.isEnabled = false
            description = null
            holeRadius = PIE_RADIUS
            setHoleColor(context.color(R.color.cinder_grey))
            setDrawEntryLabels(false)
            animateY(PIE_ANIMATION_DURATION, Easing.EaseInOutQuart)
            invalidate()
            binding.layoutCases.visible()
        }
    }

    companion object {
        const val LAYOUT = R.layout.item_overview

        private const val TEXT_ANIMATION_DURATION = 1000L
        private const val PIE_ANIMATION_DURATION = 1500
        private const val PIE_RADIUS = 75f
    }
}