package dev.sirateek.memoize.models

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

data class Tag (
    val id: String = "",
    val title: String? = "",
    val icon: String? = "🏷️",
    val color: String = "#FF0000",
    val isRealTag: Boolean = true,
)

data class TagList(
    val tags: Array<Tag>
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