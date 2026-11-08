package utils

fun parseTags(tagsInput: String): MutableList<String> {
    return if (tagsInput.isNotBlank()) {
        tagsInput.split(",").map { it.trim() }.toMutableList()
    } else {
        mutableListOf()
    }
}