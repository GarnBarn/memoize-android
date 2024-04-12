package dev.sirateek.memoize.views.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.components.TaskCard
import dev.sirateek.memoize.components.TaskCardParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task

class MainViewParamParameterProvider : PreviewParameterProvider<MainViewParam> {
    override val values = sequenceOf(
        MainViewParam()
    )
}
data class MainViewParam (
    val onClickCreateTask: () -> Unit = {},
    val onClickProfileIcon: () ->Unit = {},
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainView(
    @PreviewParameter(MainViewParamParameterProvider::class) param: MainViewParam
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = param.onClickCreateTask,
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content= { paddingValue ->
            Column(modifier = Modifier.padding(paddingValue)){
                HeaderSection(
                    onClickProfileIcon = param.onClickProfileIcon
                )
                TagListSection(tags = TagList(
                    tags = arrayOf(
                        Tag("", "Test","🏷️", "#9CCC65"),
                        Tag("", "Test2","🔥","#9CCC65"),
                        Tag("", "Test2","🔥","#9CCC65"),
                        Tag("", "Test2","🔥","#9CCC65"),
                        Tag("", "Test2","🔥","#9CCC65"),
                    ),
                ))
                TaskListSection(param = arrayOf(
                    Task(
                        id="1",
                        title = "Test",
                        tag = TagList(
                            tags = arrayOf(Tag())
                        ),
                        dueDate = DateTime.getDefaultInstance()
                    ),
                ))
            }
        }
    )
}