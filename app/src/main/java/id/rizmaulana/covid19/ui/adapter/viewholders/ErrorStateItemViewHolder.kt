package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemErrorStateBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactory
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactoryImpl
import id.rizmaulana.covid19.ui.base.BaseViewItem


class ErrorStateItem(
    val titleResId: Int,
    val subtitleResId: Int
): BaseViewItem {
    override fun typeOf(itemFactory: ItemTypeFactory): Int = itemFactory.type(this)
}

class ErrorStateItemViewHolder(itemView: View) : BaseViewHolder<ErrorStateItem>(itemView) {

    private val binding: ItemErrorStateBinding = ItemErrorStateBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        //Listener
        binding.textRetry.setOnClickListener { listener.invoke(it) }
    }

    override fun bind(item: ErrorStateItem) {
        with(binding){
            textTitle.text = itemView.context.getString(item.titleResId)
            textSubtitle.text = itemView.context.getString(item.subtitleResId)
        }
    }

    companion object {
        const val LAYOUT = R.layout.item_error_state
    }
}