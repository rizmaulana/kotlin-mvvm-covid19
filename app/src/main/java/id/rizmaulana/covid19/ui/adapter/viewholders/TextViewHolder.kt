package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import id.rizmaulana.covid19.databinding.ItemTextBinding
import id.rizmaulana.covid19.ui.base.BaseViewItem


data class TextItem(
    val text: String? = null,
    val textResId: Int? = null
): BaseViewItem

class TextItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val binding: ItemTextBinding = ItemTextBinding.bind(itemView)

    fun bind(item: TextItem) {
        with(binding) {
            root.context?.let {context ->
                textTitle.text = item.text ?: if(item.textResId != null) context.getString(item.textResId) else ""
            }
        }
    }
}