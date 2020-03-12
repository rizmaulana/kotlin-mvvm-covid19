package id.rizmaulana.covid19.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.databinding.ItemDailyBinding
import id.rizmaulana.covid19.util.IncrementStatus
import id.rizmaulana.covid19.util.NumberUtils


/**
 * rizmaulana 2020-02-06.
 */
class DailyAdapter : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    private val items = mutableListOf<CovidDaily>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: ItemDailyBinding = ItemDailyBinding.bind(itemView)

        fun bind(item: CovidDaily) {
            with(binding) {
                txtDate.text = NumberUtils.formatTime(item.reportDate)

                root.context?.let {
                    txtInformation.text = it.getString(
                        R.string.information_location_total_case,
                        NumberUtils.numberFormat(item.mainlandChina),
                        NumberUtils.numberFormat(item.otherLocations))

                    txtConfirmed.text = it.getString(R.string.confirmed_case_count, NumberUtils.numberFormat(item.deltaConfirmed))
                    txtRecovered.text = it.getString(R.string.recovered_case_count, NumberUtils.numberFormat(item.deltaRecovered))

                    imgRecovered.setImageDrawable(ContextCompat.getDrawable(it,getFluctuationIcon(item.incrementRecovered)))
                    imgConfirmed.setImageDrawable(ContextCompat.getDrawable(it, getFluctuationIcon(item.incrementConfirmed)))
                }
            }
        }

        private fun getFluctuationIcon(status: Int) = when (status) {
            IncrementStatus.INCREASE -> R.drawable.ic_trending_up
            IncrementStatus.DECREASE -> R.drawable.ic_trending_down
            else -> R.drawable.ic_trending_flat
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_daily, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
    }

    fun addAll(data: List<CovidDaily>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }
}