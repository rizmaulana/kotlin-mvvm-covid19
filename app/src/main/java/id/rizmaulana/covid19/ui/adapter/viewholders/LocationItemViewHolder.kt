package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemLocationBinding
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.CaseTypes
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.visible
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

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
) : BaseViewItem {
    fun compositeKey() = countryRegion + provinceState
}

class LocationItemViewHolder(itemView: View) : RecyclerViewHolder<LocationItem>(itemView) {
    val binding: ItemLocationBinding = ItemLocationBinding.bind(itemView)


    override fun bind(position: Int, item: LocationItem) {
        super.bind(position, item)
        with(binding) {

            relativePinned.visibility = if (item.isPinned) View.VISIBLE else View.GONE

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
}