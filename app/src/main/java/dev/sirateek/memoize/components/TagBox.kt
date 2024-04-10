package dev.sirateek.memoize.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import dev.sirateek.memoize.models.Tag

data class TagBoxParam(
    val tag: Tag,
    val modifier: Modifier = Modifier,
    val overrideColor: Color?,
    val onClick: () -> Unit = {},
)

class TagBoxParamProvider: PreviewParameterProvider<TagBoxParam> {
    override val values: Sequence<TagBoxParam>
        get() = sequenceOf(
            TagBoxParam(
                Tag(
                    id = "Test",
                    title = "Example Tag.kt Log Log Long",
                    color = "#FF0000",
                ),
                Modifier,
                null,
            ) {}
        )
}


@Preview(showBackground = true)
@Composable
fun TagBox(
    @PreviewParameter(TagBoxParamProvider::class) param: TagBoxParam,
) {

    var cardColor = Color(param.tag.color.toColorInt())
    if (param.overrideColor != null) {
        cardColor = param.overrideColor
    }

    Card (
        shape = RoundedCornerShape(100),
        modifier = param.modifier
            .widthIn(20.dp, 150.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                onClick = param.onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = Color.DarkGray,
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
        )
        ) {
        Box(modifier = Modifier.padding(7.dp)) {
            Row {
                Text(text = param.tag.icon.toString())
                if (param.tag.title.toString() != "") {
                    Text(
                        text = param.tag.title.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(
                            10.dp, 0.dp
                        ),
                    )
                }
            }

        }
    }
}