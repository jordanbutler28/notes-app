package model

data class Item(
    var id: Int = 0,
    var description: String = "",
    var isCompleted: Boolean = false,
) {
    override fun toString(): String =
        """
        #$id
        Description: $description
        Completed?: $isCompleted
        """.trimIndent() + "\n"
}
