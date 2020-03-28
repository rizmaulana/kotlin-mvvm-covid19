package id.rizmaulana.covid19.ui.adapter

import android.view.View
import id.rizmaulana.covid19.ui.adapter.viewholders.*
import id.rizmaulana.covid19.ui.base.BaseViewItem

abstract class ItemTypeFactory {
    abstract fun onCreateViewHolder(
        containerView: View,
        viewType: Int
    ): BaseViewHolder<out BaseViewItem>

    abstract fun type(item: BaseViewItem): Int
}

class ItemTypeFactoryImpl : ItemTypeFactory() {
    override fun onCreateViewHolder(
        containerView: View,
        viewType: Int
    ): BaseViewHolder<out BaseViewItem> {
        return when (viewType) {
            DailyItemViewHolder.LAYOUT -> DailyItemViewHolder(containerView)
            OverviewItemViewHolder.LAYOUT -> OverviewItemViewHolder(containerView)
            TextItemViewHolder.LAYOUT -> TextItemViewHolder(containerView)
            PinnedItemViewHolder.LAYOUT -> PinnedItemViewHolder(containerView)
            PerCountryViewHolder.LAYOUT -> PerCountryViewHolder(containerView)
            LocationItemViewHolder.LAYOUT -> LocationItemViewHolder(containerView)
            LoadingStateItemViewHolder.LAYOUT -> LoadingStateItemViewHolder(containerView)
            ErrorStateItemViewHolder.LAYOUT -> ErrorStateItemViewHolder(containerView)
            PerCountryDailyItemViewHolder.LAYOUT -> PerCountryDailyItemViewHolder(containerView)
            PerCountryDailyGraphItemViewHolder.LAYOUT -> PerCountryDailyGraphItemViewHolder(
                containerView
            )
            PerCountryProvinceGraphItemViewHolder.LAYOUT -> PerCountryProvinceGraphItemViewHolder(
                containerView
            )
            else -> onCreateViewHolder(containerView, viewType)
        }
    }

    override fun type(item: BaseViewItem): Int = when (item) {
        is DailyItem -> DailyItemViewHolder.LAYOUT
        is OverviewItem -> OverviewItemViewHolder.LAYOUT
        is TextItem -> TextItemViewHolder.LAYOUT
        is PinnedItem -> PinnedItemViewHolder.LAYOUT
        is LocationItem -> LocationItemViewHolder.LAYOUT
        is LoadingStateItem -> LoadingStateItemViewHolder.LAYOUT
        is ErrorStateItem -> ErrorStateItemViewHolder.LAYOUT
        is PerCountryDailyItem -> PerCountryDailyItemViewHolder.LAYOUT
        is PerCountryDailyGraphItem -> PerCountryDailyGraphItemViewHolder.LAYOUT
        is PerCountryProvinceGraphItem -> PerCountryProvinceGraphItemViewHolder.LAYOUT
        is PerCountryItem -> PerCountryViewHolder.LAYOUT
        else -> throw ClassCastException()
    }

}

class DailyFactoryImpl : ItemTypeFactory() {
    override fun onCreateViewHolder(
        containerView: View,
        viewType: Int
    ): BaseViewHolder<out BaseViewItem> {
        return when (viewType) {
            DailyCompactViewHolder.LAYOUT -> DailyCompactViewHolder(containerView)
            else -> throw ClassCastException()
        }
    }

    override fun type(item: BaseViewItem): Int {
        return when (item) {
            is DailyItem -> DailyCompactViewHolder.LAYOUT
            else -> throw ClassCastException()
        }
    }
}