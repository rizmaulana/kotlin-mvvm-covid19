package id.rizmaulana.covid19.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.NumberUtils
import id.rizmaulana.covid19.util.ext.visible
import kotlinx.android.synthetic.main.item_location.view.*


/**
 * rizmaulana 2020-02-06.
 */
class DetailAdapter(
    val caseType: Int,
    val clicked: (data: CovidDetail) -> Unit
) : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private val items = mutableListOf<CovidDetail>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CovidDetail) {
            with(itemView) {
                txt_information.text = "Last update ${NumberUtils.formatTime(item.lastUpdate)}"
                txt_location.text = item.locationName

                txt_data.text = "Confirmed ${NumberUtils.numberFormat(item.confirmed)}"
                txt_rcv.text = "Recovered ${NumberUtils.numberFormat(item.recovered)}"
                txt_death.text = "Deaths ${NumberUtils.numberFormat(item.deaths)}"

                when (caseType) {
                    CaseType.CONFIRMED -> txt_data.visible()
                    CaseType.RECOVERED -> txt_rcv.visible()
                    CaseType.DEATHS -> txt_death.visible()
                    else -> {
                        txt_data.visible()
                        txt_rcv.visible()
                        txt_death.visible()
                    }
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