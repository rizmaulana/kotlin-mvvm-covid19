package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemPinnedBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactory
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactoryImpl
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.NumberUtils

data class PinnedItem(
    val confirmed: Int? = null,
    val recovered: Int? = null,
    val deaths: Int? = null,
    val locationName: String,
    val lastUpdate: Long
): BaseViewItem {
    override fun typeOf(itemFactory: ItemTypeFactory): Int = itemFactory.type(this)
}

class PinnedItemViewHolder(itemView: View) : BaseViewHolder<PinnedItem>(itemView) {
    private val binding: ItemPinnedBinding = ItemPinnedBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        //Listener
    }

    override fun bind(item: PinnedItem) {
        with(binding) {
            val lastUpdate = NumberUtils.formatTime(item.lastUpdate)
            txtLocation.text = item.locationName
            txtUpdate.text = itemView.context.getString(R.string.information_last_update, lastUpdate)
            txtData.text = "${item.confirmed ?: '-'}"
            txtRcv.text = "${item.recovered ?: '-'}"
            txtDeath.text = "${item.deaths ?: '-'}"
        }
    }

    companion object {
        const val LAYOUT = R.layout.item_pinned
    }
}