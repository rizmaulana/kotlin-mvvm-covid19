package id.rizmaulana.covid19.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.base.BaseViewItem

class DiffUtilItemCallback : DiffUtil.ItemCallback<BaseViewItem>() {
    override fun areItemsTheSame(oldItem: BaseViewItem, newItem: BaseViewItem): Boolean {
        return oldItem.layoutResId() == newItem.layoutResId()
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BaseViewItem, newItem: BaseViewItem): Boolean {
        return oldItem == newItem
    }
}

abstract class BaseViewHolder<T: BaseViewItem>(containerView: View): RecyclerView.ViewHolder(containerView) {
    abstract fun bind(item: T)
    abstract fun setOnClickListener(listener: (View) -> Unit)
}

interface ViewHolderFactory{
    fun layoutResId(): Int
    fun onCreateViewHolder(containerView: View): BaseViewHolder<out BaseViewItem>
    fun onBindViewHolder(holder: BaseViewHolder<BaseViewItem>, item: BaseViewItem) {
        holder.bind(item)
    }
}

typealias VisitableAdapterItemClickListener = ((BaseViewItem, View) -> Unit)

class VisitableRecyclerAdapter(
    private val factories: List<ViewHolderFactory>,
    private val listener: VisitableAdapterItemClickListener? = null
): ListAdapter<BaseViewItem, BaseViewHolder<BaseViewItem>>(DiffUtilItemCallback()) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseViewItem> {
        val context = parent.context
        return factories.firstOrNull { it.layoutResId() == viewType }
            ?.onCreateViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false))?.apply {
                setOnClickListener{ listener?.invoke(currentList.get(adapterPosition), it) }
            } as? BaseViewHolder<BaseViewItem>
            ?: throw Throwable(context.getString(R.string.visitable_recycler_adapter_unregistered_message, context.resources.getResourceEntryName(viewType)))
    }

    override fun getItemViewType(position: Int): Int = currentList.get(position).layoutResId()

    override fun onBindViewHolder(holder: BaseViewHolder<BaseViewItem>, position: Int) {
        factories.firstOrNull { it.layoutResId() == getItemViewType(position) }
            ?.onBindViewHolder(holder, currentList.get(position))
    }
}