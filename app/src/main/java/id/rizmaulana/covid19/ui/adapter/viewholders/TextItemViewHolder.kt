package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemTextBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactory
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactoryImpl
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.util.ext.gone
import id.rizmaulana.covid19.util.ext.visible


data class TextItem(
    val textResId: Int? = null,
    val textActionResId: Int? = null
) : BaseViewItem {
    override fun typeOf(itemFactory: ItemTypeFactory): Int = itemFactory.type(this)
}

class TextItemViewHolder(itemView: View) : BaseViewHolder<TextItem>(itemView) {
    private val binding: ItemTextBinding = ItemTextBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        binding.textAction.setOnClickListener(listener)
    }

    override fun bind(item: TextItem) {
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

    companion object {
        const val LAYOUT = R.layout.item_text
    }
}