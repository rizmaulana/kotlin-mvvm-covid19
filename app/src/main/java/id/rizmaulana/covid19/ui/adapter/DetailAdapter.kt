package id.rizmaulana.covid19.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.NumberUtils
import kotlinx.android.synthetic.main.item_location.view.*


/**
 * rizmaulana 2020-02-06.
 */
class DetailAdapter(val caseType: Int, val clicked: (data: CovidDetail)-> Unit) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    val items = mutableListOf<CovidDetail>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CovidDetail) {
            with(itemView) {
                txt_information.text = "Last update ${NumberUtils.formatTime(item.lastUpdate)}"
                txt_location.text = item.locationName

                txt_data.setTextColor(resources.getColor(getColor(caseType)))
                txt_data.text = when (caseType) {
                    CaseType.DEATHS -> "Deaths ${NumberUtils.numberFormat(item.deaths)}"
                    CaseType.RECOVERED -> "Recovered ${NumberUtils.numberFormat(item.recovered)}"
                    else -> "Confirmed ${NumberUtils.numberFormat(item.confirmed)}"
                }

                setOnClickListener { clicked.invoke(item) }
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

}