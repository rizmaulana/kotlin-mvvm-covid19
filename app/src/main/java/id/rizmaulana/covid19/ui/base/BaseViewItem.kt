package id.rizmaulana.covid19.ui.base

import id.rizmaulana.covid19.ui.adapter.ItemTypeFactory
import id.rizmaulana.covid19.ui.adapter.ItemTypeFactoryImpl

interface BaseViewItem {
    fun typeOf(itemFactory: ItemTypeFactory): Int
}
