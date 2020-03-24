package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemLocationBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactory
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.CaseTypes
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.visible

data class LocationItem(
    val confirmed: Int = 0,
    val recovered: Int = 0,
    val deaths: Int = 0,
    val locationName: String,
    val lastUpdate: Long,
    val lat: Double,
    val long: Double,
    val countryRegion: String,
    val provinceState: String?,
    @CaseTypes val caseType: Int,
    val isPinned: Boolean = false
): BaseViewItem {
    override fun typeOf(itemFactory: ItemTypeFactory): Int = itemFactory.type(this)
    fun compositeKey() = countryRegion + provinceState
}

class LocationItemViewHolder(itemView: View) : BaseViewHolder<LocationItem>(itemView) {
    private val binding: ItemLocationBinding = ItemLocationBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        binding.root.setOnClickListener { listener.invoke(it) }
        binding.relativePinned.setOnClickListener { listener.invoke(it) }
    }

    override fun setOnLongClickListener(listener: (View) -> Unit) {
        binding.root.setOnLongClickListener {
            listener.invoke(it)
            true
        }
    }

    override fun bind(item: LocationItem) {
        with(binding) {

            relativePinned.visibility = if(item.isPinned) View.VISIBLE else View.GONE

            val context = itemView.context
            txtLocation.text = item.locationName
            txtInformation.text = context.getString(
                R.string.information_last_update,
                NumberUtils.formatTime(item.lastUpdate)
            )
            txtData.text = context.getString(
                R.string.confirmed_case_count,
                NumberUtils.numberFormat(item.confirmed)
            )
            txtRcv.text = context.getString(
                R.string.recovered_case_count,
                NumberUtils.numberFormat(item.recovered)
            )
            txtDeath.text = context.getString(
                R.string.death_case_count,
                NumberUtils.numberFormat(item.deaths)
            )

            when (item.caseType) {
                CaseType.CONFIRMED -> txtData.visible()
                CaseType.RECOVERED -> txtRcv.visible()
                CaseType.DEATHS -> txtDeath.visible()
                else -> {
                    txtData.visible()
                    txtRcv.visible()
                    txtDeath.visible()
                }
            }
        }
    }

    companion object {
        const val LAYOUT = R.layout.item_location
    }
}