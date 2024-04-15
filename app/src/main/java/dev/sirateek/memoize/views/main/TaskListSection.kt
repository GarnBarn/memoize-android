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
import java.util.Date


@Composable
fun TaskListSection(
    param: MutableList<Task>,
    onClickTag: (Tag) -> Unit
) {
    // Tasks Section
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        for (task in param) {
            Box(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
                TaskCard(param =
                TaskCardParam(
                    task,
                    Modifier,
                    onClick = { },
                    onClickTag = onClickTag,
                )
                )
            }
        }
    }
}