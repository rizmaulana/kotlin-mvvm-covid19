package id.rizmaulana.covid19.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.viewholders.*
import id.rizmaulana.covid19.ui.base.BaseViewItem


/**
 * rizmaulana 2020-02-06.
 */

typealias DailyAdapterItemClickListener = ((BaseViewItem, View) -> Unit)

class DiffUtilItemCallback : DiffUtil.ItemCallback<BaseViewItem>() {
    override fun areItemsTheSame(oldItem: BaseViewItem, newItem: BaseViewItem): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BaseViewItem, newItem: BaseViewItem): Boolean {
        return oldItem == newItem
    }
}

class DailyAdapter(
    private val listener: DailyAdapterItemClickListener? = null
): ListAdapter<BaseViewItem, RecyclerView.ViewHolder>(DiffUtilItemCallback()) {

    private val DAILY_ITEM = "DAILY_ITEM".hashCode()
    private val TEXT_ITEM = "TEXT_ITEM".hashCode()
    private val OVERVIEW_ITEM = "OVERVIEW_ITEM".hashCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType){
            DAILY_ITEM -> DailyItemViewHolder(layoutInflater.inflate(R.layout.item_daily, parent, false)).apply {
                setOnClickListener { listener?.invoke(currentList.get(adapterPosition), it) }
            }
            OVERVIEW_ITEM -> OverviewItemViewHolder(layoutInflater.inflate(R.layout.item_overview, parent, false)).apply {
                setOnClickListener { listener?.invoke(currentList.get(adapterPosition), it) }
            }
            else -> TextItemViewHolder(layoutInflater.inflate(R.layout.item_text, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(currentList.get(position)){
            is DailyItem -> DAILY_ITEM
            is OverviewItem -> OVERVIEW_ITEM
            else -> TEXT_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            DAILY_ITEM -> (holder as? DailyItemViewHolder)?.bind(currentList[holder.adapterPosition] as DailyItem)
            OVERVIEW_ITEM -> (holder as? OverviewItemViewHolder)?.bind(currentList[holder.adapterPosition] as OverviewItem)
            else -> (holder as? TextItemViewHolder)?.bind(currentList[holder.adapterPosition] as TextItem)
        }
    }
}