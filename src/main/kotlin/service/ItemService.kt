package service

import model.Item
import model.Note

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

    fun searchItemsByContent(input: String): List<Item> = items.filter { it.description.contains(input, ignoreCase = true) }

}