package model

enum class Status(val display: String) {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done")
}

data class Note (
    var id: Int = 0,
    var title: String = "",
    var body: String = "",
    var priority: Int = 0,
    var status: Status = Status.TO_DO,
    var category: String = "",
    var tags: MutableList<String> = mutableListOf(),
    var version: Double = 1.0,
    var isArchived: Boolean = false
) {
override fun toString(): String {
    return """
        #$id
        Title: $title
        Body: $body
        Priority: $priority
        Status: ${status.display}
        Category: $category
        Tags: ${tags.joinToString(", ")}
        Version: $version
        Archive status: $isArchived
    """.trimIndent() + "\n"
    }
}