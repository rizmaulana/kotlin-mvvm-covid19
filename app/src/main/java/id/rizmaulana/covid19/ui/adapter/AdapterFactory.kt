package id.rizmaulana.covid19.ui.adapter

import android.view.View
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyCompactViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.DailyItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.ErrorStateItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.LoadingStateItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.LocationItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.OverviewItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryDailyGraphItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryDailyItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryProvinceGraphItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.PerCountryViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.PinnedItemViewHolder
import id.rizmaulana.covid19.ui.adapter.viewholders.TextItemViewHolder
import id.rizmaulana.covid19.ui.base.BaseViewItem
import me.ibrahimyilmaz.kiel.adapterOf

typealias OnItemClickListener = (BaseViewItem, View) -> Unit
typealias OnItemLongClickListener = (BaseViewItem, View) -> Unit

fun createAdapter(
    onItemClick: OnItemClickListener? = null,
    onItemLongClickListener: OnItemLongClickListener? = null
) = adapterOf<BaseViewItem> {
    register(
        layoutResource = R.layout.item_daily_compact,
        viewHolder = ::DailyCompactViewHolder
    )
    register(
        layoutResource = R.layout.item_daily,
        viewHolder = ::DailyItemViewHolder,
        onBindBindViewHolder = { dailyItemViewHolder, _, dailyItem ->
            dailyItemViewHolder.itemView.setOnClickListener { onItemClick?.invoke(dailyItem, it) }
        }
    )
    register(
        layoutResource = R.layout.item_error_state,
        viewHolder = ::ErrorStateItemViewHolder,
        onBindBindViewHolder = { errorStateItemViewHolder, _, errorStateItem ->
            errorStateItemViewHolder.binding.textRetry.setOnClickListener {
                onItemClick?.invoke(
                    errorStateItem,
                    it
                )
            }
        }
    )
    register(
        layoutResource = R.layout.item_loading_state,
        viewHolder = ::LoadingStateItemViewHolder
    )
    register(
        layoutResource = R.layout.item_location,
        viewHolder = ::LocationItemViewHolder,
        onBindBindViewHolder = { locationItemViewHolder, _, locationItem ->
            with(locationItemViewHolder) {
                binding.root.setOnClickListener { onItemClick?.invoke(locationItem, it) }
                binding.relativePinned.setOnClickListener { onItemClick?.invoke(locationItem, it) }

                binding.root.setOnLongClickListener {
                    onItemLongClickListener?.invoke(locationItem, it)
                    true
                }
            }
        }
    )
    register(
        layoutResource = R.layout.item_overview,
        viewHolder = ::OverviewItemViewHolder,
        onBindBindViewHolder = { overviewItemViewHolder, _, overviewItem ->
            with(overviewItemViewHolder.binding) {
                layoutActive.setOnClickListener { onItemClick?.invoke(overviewItem, it) }
                layoutRecovered.setOnClickListener { onItemClick?.invoke(overviewItem, it) }
                layoutDeath.setOnClickListener { onItemClick?.invoke(overviewItem, it) }
            }
        }
    )
    register(
        layoutResource = R.layout.item_daily_percountry_graph,
        viewHolder = ::PerCountryDailyGraphItemViewHolder,
        onBindBindViewHolder = { perCountryDailyGraphItemViewHolder, _, perCountryDailyGraphItem ->
            perCountryDailyGraphItemViewHolder.binding.root.setOnClickListener {
                onItemClick?.invoke(perCountryDailyGraphItem, it)
            }
        }
    )
    register(
        layoutResource = R.layout.item_daily_percountry,
        viewHolder = ::PerCountryDailyItemViewHolder,
        onBindBindViewHolder = { perCountryDailyItemViewHolder, _, perCountryDailyItem ->
            perCountryDailyItemViewHolder.binding.root.setOnClickListener {
                onItemClick?.invoke(perCountryDailyItem, it)
            }
        }
    )
    register(
        layoutResource = R.layout.item_per_country,
        viewHolder = ::PerCountryViewHolder,
        onBindBindViewHolder = { perCountryViewHolder, _, perCountryItem ->
            perCountryViewHolder.binding.root.setOnClickListener {
                onItemClick?.invoke(perCountryItem, it)
            }
        }
    )
    register(
        layoutResource = R.layout.item_province_percountry_graph,
        viewHolder = ::PerCountryProvinceGraphItemViewHolder,
        onBindBindViewHolder = { perCountryProvinceGraphItemViewHolder, _, perCountryProvinceGraphItem ->
            perCountryProvinceGraphItemViewHolder.binding.root.setOnClickListener {
                onItemClick?.invoke(perCountryProvinceGraphItem, it)
            }
        }
    )
    register(
        layoutResource = R.layout.item_pinned,
        viewHolder = ::PinnedItemViewHolder
    )
    register(
        layoutResource = R.layout.item_text,
        viewHolder = ::TextItemViewHolder,
        onBindBindViewHolder = { textItemViewHolder, _, textItem ->
            textItemViewHolder.binding.textAction.setOnClickListener {
                onItemClick?.invoke(textItem, it)
            }
        }
    )
}
