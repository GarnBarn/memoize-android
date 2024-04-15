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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.components.TaskCard
import dev.sirateek.memoize.components.TaskCardParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.repository.ReminderRepository
import dev.sirateek.memoize.views.tag.GetTags
import dev.sirateek.memoize.views.tag.ParseTags
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainViewParamParameterProvider : PreviewParameterProvider<MainViewParam> {
    override val values = sequenceOf(
        MainViewParam()
    )
}
data class MainViewParam (
    val onClickCreateTask: () -> Unit = {},
    val onClickProfileIcon: () ->Unit = {},
    val onClickManageTag: () -> Unit = {},
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainView(
    @PreviewParameter(MainViewParamParameterProvider::class) param: MainViewParam
) {

    val taskList = remember {
        mutableStateListOf<Task>()
    }
    val tagList = remember {
        mutableStateListOf<Tag>()
    }

    GetTags {
        tagList.clear()
        for (doc in it.documents) {
            tagList.add(ParseTags(doc))
        }
    }


    val callback = {
        it: QuerySnapshot ->
        taskList.clear()
        for (result in it.documents) {
            val res = Task()
            res.id = result.id
            res.title = result.get("title").toString()
            res.description = result.get("description").toString()

            res.dueDate = result.getDate("reminder_date_time")!!
            res.createdAt = result.getDate("created_at") !!
            res.collection = result.get("collection").toString()

            if (result.get("tags") != null) {
                val tags = result.get("tags") as List<Map<String, String>>
                for (tag in tags) {
                    val tagData = Tag()
                    tagData.id = tag["id"].toString()
                    tagData.title = tag["title"].toString()
                    tagData.color = tag["color"].toString()
                    tagData.icon = tag["icon"].toString()
                    tagData.isRealTag = true
                    res.tag.tags.add(tagData)
                }
            }

            val todayDateOnly = Date.parse(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
            val resDateOnly = Date.parse(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(res.dueDate))

            val testData = resDateOnly.compareTo(todayDateOnly)

            if (testData == 0) {
                res.tag.tags.add(0, Tag("today", title = "today", isRealTag = false))
            }

            taskList.add(res)
        }

        taskList.sortByDescending { sortIT -> sortIT.dueDate }
    }

    GetTasks {
        callback(it)
    }
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
                    onClickProfileIcon = param.onClickProfileIcon,
                    onClickReload = {
                        GetTasks {
                            callback(it)
                        }
                        GetTags {
                            tagList.clear()
                            for (doc in it.documents) {
                                tagList.add(ParseTags(doc))
                            }
                        }
                    }
                )
                TagListSection(
                    tags = TagList(
                        tags = tagList,
                    ),
                    onClickManageTag = param.onClickManageTag,
                )
                TaskListSection(param = taskList)
            }
        }
    )
}

fun GetTasks(
    onSuccess: (QuerySnapshot) -> Unit
) {
    val uid = Firebase.auth.currentUser?.uid
    ReminderRepository().whereEqualTo("uid", uid).get().addOnSuccessListener(onSuccess)
}