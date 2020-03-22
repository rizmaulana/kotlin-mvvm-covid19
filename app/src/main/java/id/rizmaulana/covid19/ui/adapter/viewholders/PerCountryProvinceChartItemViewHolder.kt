package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyPercountryGraphBinding
import id.rizmaulana.covid19.databinding.ItemProvincePercountryGraphBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem

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

    }


    companion object {
        const val LAYOUT = R.layout.item_province_percountry_graph
    }
}