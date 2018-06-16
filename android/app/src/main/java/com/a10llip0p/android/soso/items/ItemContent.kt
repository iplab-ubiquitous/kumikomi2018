package com.a10llip0p.android.soso.items

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object ItemContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<Item> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, Item> = HashMap()

    val CONTENT_LIST: List<String> = mutableListOf("トマト", "ナス", "ピーマン", "水", "カメラ")

    init {
        CONTENT_LIST.forEachIndexed { index, s ->
            addItem(createItem(index, s, s))
        }
    }

    private fun addItem(item: Item) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createItem(position: Int, content: String, details: String): Item {
        return Item(position.toString(), content, details);
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class Item(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
