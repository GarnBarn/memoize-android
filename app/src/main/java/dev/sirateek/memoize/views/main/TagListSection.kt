package dev.sirateek.memoize.views.main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList


class TagListSectionPreviewParam : PreviewParameterProvider<TagList> {
    override val values: Sequence<TagList> = sequenceOf(
                        TagList(
                            arrayOf(
                                Tag("", "Test","🏷️", "#9CCC65"),
                                Tag("", "Test2","🔥","#9CCC65"),
                                Tag("", "Test2","🔥","#9CCC65"),
                                Tag("", "Test2","🔥","#9CCC65"),
                                Tag("", "Test2","🔥","#9CCC65"),
                            )
                        )
                    )
}


@Preview(showBackground = true)
@Composable
fun TagListSection(
    @PreviewParameter(TagListSectionPreviewParam::class) tags: TagList,
    ) {
    Box(modifier = Modifier.padding(start=20.dp, top=10.dp, bottom=10.dp)) {
        Row {
            TagBox(
                param = TagBoxParam(
                    tag = Tag(icon = "✏️"),
                    Modifier.padding(horizontal = 5.dp),
                    Color.Gray,
                )
            )

            val scrollState1 = rememberScrollState()
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.horizontalScroll(scrollState1)
            ) {

                for (tag in tags) {
                    TagBox(
                        param = TagBoxParam(
                            tag = tag,
                            Modifier.padding(horizontal = 5.dp),
                            Color.Gray,
                        )
                    )
                }
            }
        }
    }
}