package dev.sirateek.memoize.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

data class Tag (
    var id: String = "",
    var title: String? = "",
    var icon: String? = "🏷️",
    var color: String = "#FF0000",
    var isRealTag: Boolean = true,
    var reminderSet: String = "",
)

class TagList(
    var tags: MutableList<Tag>
): Iterable<Tag> {
    private var mainTagColor: Color? = null;

    fun getMainTagColor(): Color {
        if (mainTagColor != null) {
            return mainTagColor!!
        }

        for (tag in tags) {
            if (tag.isRealTag) {
                val color = Color(tag.color.toColorInt())
                mainTagColor = color
                return color
            }
        }
        mainTagColor = Color.Black
        return Color.Black
    }

    override fun iterator(): Iterator<Tag> {
        return tags.iterator()
    }


}