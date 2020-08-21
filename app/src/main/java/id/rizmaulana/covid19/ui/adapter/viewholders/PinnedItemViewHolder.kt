package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemPinnedBinding
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.NumberUtils
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

data class PinnedItem(
    val confirmed: Int? = null,
    val recovered: Int? = null,
    val deaths: Int? = null,
    val locationName: String,
    val lastUpdate: Long
) : BaseViewItem

class PinnedItemViewHolder(itemView: View) : RecyclerViewHolder<PinnedItem>(itemView) {
    private val binding: ItemPinnedBinding = ItemPinnedBinding.bind(itemView)

    override fun bind(position: Int, item: PinnedItem) {
        super.bind(position, item)
        with(binding) {
            val lastUpdate = NumberUtils.formatTime(item.lastUpdate)
            txtLocation.text = item.locationName
            txtUpdate.text =
                itemView.context.getString(R.string.information_last_update, lastUpdate)
            txtData.text = NumberUtils.numberFormat(item.confirmed)
            txtRcv.text = NumberUtils.numberFormat(item.recovered)
            txtDeath.text = NumberUtils.numberFormat(item.deaths)
        }
    }
}