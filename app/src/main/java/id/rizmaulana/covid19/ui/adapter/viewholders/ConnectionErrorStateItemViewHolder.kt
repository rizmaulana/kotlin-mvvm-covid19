package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.databinding.ItemConnectionErrorStateBinding
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem


class ConnectionErrorStateItem: BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_connection_error_state
}

class ConnectionErrorStateItemViewHolder(itemView: View) : BaseViewHolder<ConnectionErrorStateItem>(itemView) {

    private val binding: ItemConnectionErrorStateBinding = ItemConnectionErrorStateBinding.bind(itemView)

    override fun setOnClickListener(listener: (View) -> Unit) {
        //Listener
        binding.textTryAgain.setOnClickListener { listener.invoke(it) }
    }

    override fun bind(item: ConnectionErrorStateItem) {

    }

    companion object {
        const val LAYOUT = R.layout.item_connection_error_state
    }
}