package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemTextBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem


data class TextItem(
    val text: String? = null,
    val textResId: Int? = null
): BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_text
}

class TextItemViewHolder(itemView: View) : BaseViewHolder<TextItem>(itemView) {
    private val binding: ItemTextBinding = ItemTextBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        //Listener
    }

    override fun bind(item: TextItem) {
        with(binding) {
            root.context?.let {context ->
                textTitle.text = item.text ?: if(item.textResId != null) context.getString(item.textResId) else ""
            }
        }
    }

    companion object {
        const val LAYOUT = R.layout.item_text
    }
}