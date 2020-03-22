package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import androidx.annotation.StringRes
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemDailyPercountryBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.getStringWithArg

data class PerCountryDailyItem(
    val id: Int = 0,
    val confirmed: Int = 0,
    val totalDeath: Int = 0,
    val totalRecovered: Int = 0,
    val totalConfirmed: Int = 0,
    val date: Long = 0,
    val day: Int = 0,
    @StringRes val info: Int
) : BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_daily_percountry
}

class PerCountryDailyItemViewHolder(itemView: View) :
    BaseViewHolder<PerCountryDailyItem>(itemView) {
    private val binding: ItemDailyPercountryBinding = ItemDailyPercountryBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        binding.root.setOnClickListener { listener.invoke(it) }
    }

    override fun bind(item: PerCountryDailyItem) {
        with(binding) {
            txtInformation.text = itemView.context.getString(item.info)
            txtDate.text = "${NumberUtils.formatShortDate(item.date)} ${getStringWithArg(R.string.day_on, item.day.toString())}"
            txtConfirmed.text = getStringWithArg(
                R.string.confirmed_case_count,
                NumberUtils.numberFormat(item.totalConfirmed)
            )
            txtDeath.text = getStringWithArg(
                R.string.death_case_count,
                NumberUtils.numberFormat(item.totalDeath)
            )
            txtRcv.text = getStringWithArg(
                R.string.recovered_case_count,
                NumberUtils.numberFormat(item.totalRecovered)
            )
            txtNewCase.text = getStringWithArg(
                R.string.new_case_case_count,
                NumberUtils.numberFormat(item.confirmed)
            )

        }
    }


    companion object {
        const val LAYOUT = R.layout.item_daily_percountry
    }
}