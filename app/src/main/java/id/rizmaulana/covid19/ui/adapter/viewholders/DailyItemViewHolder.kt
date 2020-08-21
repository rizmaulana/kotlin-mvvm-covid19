package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyBinding
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.IncrementStatus
import id.rizmaulana.covid19.util.NumberUtils
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

data class DailyItem(
    val deltaConfirmed: Int = 0,
    val deltaRecovered: Int = 0,
    val mainlandChina: Int = 0,
    val otherLocations: Int = 0,
    val reportDate: String = "",
    var incrementRecovered: Int = IncrementStatus.FLAT,
    var incrementConfirmed: Int = IncrementStatus.FLAT
) : BaseViewItem

class DailyItemViewHolder(itemView: View) : RecyclerViewHolder<DailyItem>(itemView) {
    val binding: ItemDailyBinding = ItemDailyBinding.bind(itemView)

    override fun bind(position: Int, item: DailyItem) {
        super.bind(position, item)
        with(binding) {
            txtDate.text = NumberUtils.formatStringDate(item.reportDate)

            root.context?.let {
                txtInformation.text = it.getString(
                    R.string.information_location_total_case,
                    NumberUtils.numberFormat(item.mainlandChina),
                    NumberUtils.numberFormat(item.otherLocations)
                )

                txtConfirmed.text = it.getString(
                    R.string.confirmed_case_count,
                    NumberUtils.numberFormat(item.deltaConfirmed)
                )
                txtRecovered.text = it.getString(
                    R.string.recovered_case_count,
                    NumberUtils.numberFormat(item.deltaRecovered)
                )

                imgRecovered.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        getFluctuationIcon(item.incrementRecovered)
                    )
                )
                imgConfirmed.setImageDrawable(
                    ContextCompat.getDrawable(
                        it,
                        getFluctuationIcon(item.incrementConfirmed)
                    )
                )
            }
        }
    }

    private fun getFluctuationIcon(status: Int) = when (status) {
        IncrementStatus.INCREASE -> R.drawable.ic_trending_up
        IncrementStatus.DECREASE -> R.drawable.ic_trending_down
        else -> R.drawable.ic_trending_flat
    }
}