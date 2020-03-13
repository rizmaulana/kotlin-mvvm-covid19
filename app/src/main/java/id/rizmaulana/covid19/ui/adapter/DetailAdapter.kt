package id.rizmaulana.covid19.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.databinding.ItemLocationBinding
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.visible


/**
 * rizmaulana 2020-02-06.
 */
class DetailAdapter(
    val caseType: Int,
    val clicked: (data: CovidDetail) -> Unit,
    val longClicked: (data: CovidDetail) -> Unit
) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private val items = mutableListOf<CovidDetail>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var binding: ItemLocationBinding = ItemLocationBinding.bind(itemView)

        fun bind(item: CovidDetail) {
            with(binding) {
                root.context?.let {
                    txtInformation.text = it.getString(
                        R.string.information_last_update,
                        NumberUtils.formatTime(item.lastUpdate)
                    )
                    txtLocation.text = item.locationName

                    txtData.text = it.getString(
                        R.string.confirmed_case_count,
                        NumberUtils.numberFormat(item.confirmed)
                    )
                    txtRcv.text = it.getString(
                        R.string.recovered_case_count,
                        NumberUtils.numberFormat(item.recovered)
                    )
                    txtDeath.text = it.getString(
                        R.string.death_case_count,
                        NumberUtils.numberFormat(item.deaths)
                    )
                }

                when (caseType) {
                    CaseType.CONFIRMED -> txtData.visible()
                    CaseType.RECOVERED -> txtRcv.visible()
                    CaseType.DEATHS -> txtDeath.visible()
                    else -> {
                        txtData.visible()
                        txtRcv.visible()
                        txtDeath.visible()
                    }
                }

                root.setOnClickListener { clicked.invoke(item) }
                root.setOnLongClickListener {
                    longClicked.invoke(item)
                    true
                }
            }
        }

        private fun getColor(status: Int) = when (status) {
            CaseType.DEATHS -> R.color.color_death
            CaseType.RECOVERED -> R.color.color_recovered
            else -> R.color.color_confirmed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    fun addAll(data: List<CovidDetail>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface ActionListener {
        fun onItemClick(data: CovidDetail)
        fun onLongItemClick(data: CovidDetail)
    }

}