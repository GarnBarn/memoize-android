package dev.sirateek.memoize.views.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.repository.ReminderRepository
import dev.sirateek.memoize.views.tag.GetTags
import dev.sirateek.memoize.views.tag.ParseTags
import java.text.SimpleDateFormat
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
    var tagList = remember {
        mutableStateListOf<Tag>()
    }
    var cacheTagList = remember {
        mutableStateListOf<Tag>()
    }

    val visibleTaskList = remember {
        mutableStateListOf<Task>()
    }


    GetTags {
        tagList.clear()
        for (doc in it.documents) {
            tagList.add(ParseTags(doc))
        }
    }

    var selectedTag by remember {
        mutableStateOf<Tag?>(null)
    }
    val shouldUseVisibleTaskList = remember {
        mutableStateOf(false)
    }


    val shouldUseVisibleTagList = remember {
        mutableStateOf(false)
    }

    val onClickTag = {
        cacheTagList.clear()
        for (tagData in tagList) {
            cacheTagList.add(tagData)
        }

        tagList.clear()
        tagList.add(selectedTag!!)

        shouldUseVisibleTagList.value = true
        visibleTaskList.clear()
        for (eachTask in taskList) {
            for (eachTag in eachTask.tag) {
                if (eachTag.id == selectedTag?.id) {
                    visibleTaskList.add(eachTask)
                    break
                }
            }
        }

        shouldUseVisibleTaskList.value = true
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

        taskList.sortBy { sortIT -> sortIT.dueDate }
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
                        shouldUseVisibleTagList.value = false
                        shouldUseVisibleTaskList.value = false
                        selectedTag = null
                        cacheTagList.clear()
                    }
                )
                TagListSection(
                    tags = TagList(
                        tags = tagList,
                    ),
                    onClickManageTag = param.onClickManageTag,
                    onClickSomeTag = {
                        if (selectedTag != null) {
                            shouldUseVisibleTagList.value = false
                            shouldUseVisibleTaskList.value = false
                            tagList.clear()
                            for (tagData in cacheTagList) {
                                tagList.add(tagData)
                            }

                            selectedTag = null
                            return@TagListSection
                        }
                        selectedTag = it
                        onClickTag()
                    }
                )
                if (shouldUseVisibleTaskList.value) {
                    TaskListSection(param = visibleTaskList)
                } else {
                    TaskListSection(param = taskList)
                }

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