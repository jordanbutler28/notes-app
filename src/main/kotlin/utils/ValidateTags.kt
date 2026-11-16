package utils

fun parseTags(tagsInput: String): MutableList<String> =
    if (tagsInput.isNotBlank()) {
        tagsInput.split(",").map { it.trim() }.toMutableList()
    } else {
        mutableListOf()
    }
