package dev.sirateek.memoize.components

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.google.type.DateTime
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task

class TaskParamProvider : PreviewParameterProvider<TaskCardParam> {
    override val values = sequenceOf(
            TaskCardParam(
                Task(
                    "Test",
                    "This is very long text hahahahahah TestHello 2 Test Again",
                    TagList(
                        tags = arrayOf(
                            Tag("", "Test","🏷️", "#9CCC65"),
                            Tag("", "Test2","🔥","#9CCC65"),
                        )
                    ),
                    DateTime.getDefaultInstance(),
                    "",
                    ""
                ),
                Modifier
            ),
            TaskCardParam(
                Task(
                    "Test",
                    "Small Text",

                    TagList(
                        tags = arrayOf(
                            Tag("", "Test","🏷️","#FF0000"),
                            )
                    ),
                    DateTime.getDefaultInstance(),
                    "",
                    ""
                ),
                Modifier
            ),
        )
}

data class TaskCardParam (
    val task: Task,
    val modifier: Modifier
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskCard(
    @PreviewParameter(TaskParamProvider::class) param: TaskCardParam,
) {
    Card(
        modifier = param.modifier
            .fillMaxWidth()
            .heightIn(150.dp, 300.dp)
            .padding(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = param.task.tag.getMainTagColor(),
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
                    text = "Due 20 Feb 2023",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                )
            }
        }
    }
}