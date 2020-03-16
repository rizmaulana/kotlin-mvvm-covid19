package id.rizmaulana.covid19.ui.adapter

import android.view.View
import id.rizmaulana.covid19.ui.adapter.viewholders.*
import id.rizmaulana.covid19.ui.base.BaseViewItem

abstract class ItemTypeFactory {
    abstract fun onCreateViewHolder(containerView: View, viewType: Int): BaseViewHolder<out BaseViewItem>
}

class ItemTypeFactoryImpl: ItemTypeFactory() {
    override fun onCreateViewHolder(
        containerView: View,
        viewType: Int
    ): BaseViewHolder<out BaseViewItem> {
        return when(viewType) {
            DailyItemViewHolder.LAYOUT -> DailyItemViewHolder(containerView)
            OverviewItemViewHolder.LAYOUT -> OverviewItemViewHolder(containerView)
            TextItemViewHolder.LAYOUT -> TextItemViewHolder(containerView)
            PinnedItemViewHolder.LAYOUT -> PinnedItemViewHolder(containerView)
            LoadingStateItemViewHolder.LAYOUT -> LoadingStateItemViewHolder(containerView)
            ConnectionErrorStateItemViewHolder.LAYOUT -> ConnectionErrorStateItemViewHolder(containerView)
            else -> onCreateViewHolder(containerView, viewType)
        }
    }
}