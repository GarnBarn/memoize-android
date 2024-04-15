package dev.sirateek.memoize.models

import com.google.type.DateTime
import java.util.Date

data class Task (
    var id: String = "",
    var title: String? = "",
    var tag: TagList = TagList(mutableListOf()),
    var dueDate: Date = Date(),
    var description: String? = "",
    var collection: String? = "",
    var createdAt: Date = Date(),
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