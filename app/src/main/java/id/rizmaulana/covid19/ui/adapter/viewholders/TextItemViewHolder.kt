package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.databinding.ItemTextBinding
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.ext.gone
import id.rizmaulana.covid19.util.ext.visible
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder


data class TextItem(
    val textResId: Int? = null,
    val textActionResId: Int? = null
) : BaseViewItem

class TextItemViewHolder(itemView: View) : RecyclerViewHolder<TextItem>(itemView) {
    val binding: ItemTextBinding = ItemTextBinding.bind(itemView)

    override fun bind(position: Int, item: TextItem) {
        super.bind(position, item)
        with(binding) {
            root.context?.let { context ->
                textTitle.text = item.textResId?.let { context.getString(it) }.orEmpty()
                if (item.textActionResId != null) {
                    with(textAction) {
                        visible()
                        text = context.getString(item.textActionResId)
                    }
                } else {
                    textAction.gone()
                }
            }
        }
    }
}