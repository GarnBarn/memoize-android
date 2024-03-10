package dev.sirateek.memoize.models

import com.google.type.DateTime

data class Task (
    val id: String,
    val title: String? = "",
    val tag: TagList,
    val dueDate: DateTime,
    val description: String? = "",
    val collection: String? = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        return id == other.id
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + tag.hashCode()
        result = 31 * result + dueDate.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (collection?.hashCode() ?: 0)
        return result
    }
}