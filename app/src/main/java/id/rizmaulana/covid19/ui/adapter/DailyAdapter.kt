package id.rizmaulana.covid19.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDaily
import id.rizmaulana.covid19.util.IncrementStatus
import id.rizmaulana.covid19.util.NumberUtils
import kotlinx.android.synthetic.main.item_daily.view.*


/**
 * rizmaulana 2020-02-06.
 */
class DailyAdapter : RecyclerView.Adapter<DailyAdapter.ViewHolder>() {

    private val items = mutableListOf<CovidDaily>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CovidDaily) {
            with(itemView) {
                txt_date.text = NumberUtils.formatTime(item.reportDate)
                txt_confirmed.text = "Confirmed : ${NumberUtils.numberFormat(item.deltaConfirmed)}"
                txt_recovered.text = "Recovered : ${NumberUtils.numberFormat(item.deltaRecovered)}"
                txt_information.text =
                    "Total ${NumberUtils.numberFormat(item.mainlandChina)} cases on China and ${NumberUtils.numberFormat(
                        item.otherLocations
                    )} on the other location"
                img_recovered.setImageDrawable(resources.getDrawable(getFluctuationIcon(item.incrementRecovered)))
                img_confirmed.setImageDrawable(resources.getDrawable(getFluctuationIcon(item.incrementConfirmed)))

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