package model

data class Note (
    var id: Int = 0,
    var title: String = "",
    var body: String = "",
    var priority: Int = 0,
    var category: String = "",
    var isArchived: Boolean = false
) {
override fun toString(): String {
    return """
        #$id
        title: $title
        body: $body
        priority: $priority
        category: $category
        isArchived: $isArchived
    """.trimIndent() + "\n"
    }
}