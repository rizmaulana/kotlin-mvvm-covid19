package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder

class DailyCompactViewHolder(itemView: View) : BaseViewHolder<DailyItem>(itemView) {

    override fun bind(item: DailyItem) {

    }

    override fun setOnClickListener(listener: (View) -> Unit) {
        // no op
    }

    companion object {
        val LAYOUT = R.layout.item_daily_compact
    }
}