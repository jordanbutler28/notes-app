package service

import model.Item

class ItemService {
    private var items = ArrayList<Item>()
    private var lastId = 0
    private fun getId(): Int = lastId++

    fun addItem(item: Item) {
        item.id = getId()
        item.isCompleted = false
        items.add(item)
    }

    fun getItems(): List<Item> {
        return items
    }

    fun findItemById(id: Int): Item? {
        return items.find { it.id == id }
    }
}