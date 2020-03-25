package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyCompactBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.util.IncrementStatus
import id.rizmaulana.covid19.util.NumberUtils

class DailyCompactViewHolder(itemView: View) : BaseViewHolder<DailyItem>(itemView) {

    private val binding = ItemDailyCompactBinding.bind(itemView)

    override fun bind(item: DailyItem) {
        with(binding) {
            txtDate.text = item.reportDate

            root.context?.let {
                txtConfirmed.text = it.getString(R.string.confirmed_case_count, NumberUtils.numberFormat(item.deltaConfirmed))
                txtRecovered.text = it.getString(R.string.recovered_case_count, NumberUtils.numberFormat(item.deltaRecovered))
            }
        }
    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        // no op
    }

    companion object {
        val LAYOUT = R.layout.item_daily_compact
    }
}