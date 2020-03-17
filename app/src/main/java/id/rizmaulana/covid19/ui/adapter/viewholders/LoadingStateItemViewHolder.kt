package id.rizmaulana.covid19.ui.adapter.viewholders

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.BaseViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem


class LoadingStateItem: BaseViewItem {
    override fun layoutResId(): Int = R.layout.item_loading_state
}

class LoadingStateItemViewHolder(itemView: View) : BaseViewHolder<LoadingStateItem>(itemView) {

    override fun setOnClickListener(listener: (View) -> Unit) {
        //Listener
    }

    override fun bind(item: LoadingStateItem) {

    }

    companion object {
        const val LAYOUT = R.layout.item_loading_state
    }
}