package dev.sirateek.memoize.components

import androidx.annotation.ColorInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.google.type.DateTime
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min


class TaskParamProvider : PreviewParameterProvider<TaskCardParam> {
    override val values = sequenceOf(
            TaskCardParam(
                Task(
                    "Test",
                    "This is very long text hahahahahah TestHello 2 Test Again",
                    TagList(
                        tags = mutableListOf(
                            Tag("", "Test","🏷️", "#9CCC65"),
                            Tag("", "Test2","🔥","#9CCC65"),
                        )
                    ),
                    Date(),
                    "",
                    ""
                ),
                Modifier,
                onClick = { }
            ),
            TaskCardParam(
                Task(
                    "Test",
                    "Small Text",

                    TagList(
                        tags = mutableListOf(
                            Tag("", "Test","🏷️","#FF0000"),
                            )
                    ),
                    Date(),
                    "",
                    ""
                ),
                Modifier,
                onClick = { }
            ),
        )
}

data class TaskCardParam (
    val task: Task,
    val modifier: Modifier,
    val onClick: () -> Unit
)

internal fun calculateContrastRatio(foreground: Color, background: Color): Float {
    val foregroundLuminance = foreground.luminance() + 0.05f
    val backgroundLuminance = background.luminance() + 0.05f

    return max(foregroundLuminance, backgroundLuminance) /
            min(foregroundLuminance, backgroundLuminance)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskCard(
    @PreviewParameter(TaskParamProvider::class) param: TaskCardParam,
) {
    val shape = RoundedCornerShape(15.dp)
    var textColor = Color.Black
    val cardBackground = param.task.tag.getMainTagColor()

    val contrast = calculateContrastRatio(Color.White, cardBackground)
    if (contrast < 1.5f) {
        textColor = Color.White
    }

    Card(
        modifier = param.modifier
            .fillMaxWidth()
            .heightIn(150.dp, 300.dp)
            .clip(shape)
            .clickable(
                onClick = param.onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = Color.DarkGray,
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground,
        )
    ) {
        Box(
            modifier = Modifier
                .padding(15.dp)
        ) {
            Column {
                Text(
                    text = param.task.title.toString(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                    ),
                )
                Row(
                    modifier = Modifier.padding(0.dp, 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    for (tag in param.task.tag.reversed()) {
                        TagBox(param = TagBoxParam(tag, modifier = Modifier, overrideColor = Color.White))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text =  "Due ${SimpleDateFormat("dd/MM/yyyy - HH:MM", Locale.getDefault()).format(param.task.dueDate)}",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = textColor,
                    )
                )
            }
        }
    }
}