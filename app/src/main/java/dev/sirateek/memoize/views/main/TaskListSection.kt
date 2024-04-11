package dev.sirateek.memoize.views.main

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.type.DateTime
import dev.sirateek.memoize.components.TaskCard
import dev.sirateek.memoize.components.TaskCardParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task

class TaskListSectionPreviewParam : PreviewParameterProvider<Array<Task>> {
    override val values: Sequence<Array<Task>> = sequenceOf(
        arrayOf(
            Task(
                id="1",
                title = "Test",
                tag = TagList(
                    tags = arrayOf(Tag())
                ),
                dueDate = DateTime.getDefaultInstance()
            ),
    )
    )
}

@Composable
fun TaskListSection(
    @PreviewParameter(TaskListSectionPreviewParam::class) param: Array<Task>
) {
    // Tasks Section
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        for (i in 0..20) {
            Box(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
                TaskCard(param =
                TaskCardParam(
                    Task(
                        id = "Test",
                        title = "Test",
                        dueDate = DateTime.getDefaultInstance(),
                        tag = TagList(
                            tags = arrayOf(Tag(color = "#1FF110")),
                        ),
                    ),
                    Modifier,
                ) {
                    Log.i("Debug", "Test")
                }
                )
            }
        }
    }
}