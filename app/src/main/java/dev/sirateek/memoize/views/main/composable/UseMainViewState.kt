package dev.sirateek.memoize.views.main.composable

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import dev.sirateek.memoize.models.ReminderSet
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.views.main.GetTasks
import dev.sirateek.memoize.views.main.TodayTag
import dev.sirateek.memoize.views.reminder.AddReminderSet
import dev.sirateek.memoize.views.reminder.GetReminderSet
import dev.sirateek.memoize.views.reminder.ParseReminderSet
import dev.sirateek.memoize.views.reminder.ParseTask
import dev.sirateek.memoize.views.tag.GetTags
import dev.sirateek.memoize.views.tag.ParseTags

data class UseMainViewStateReturn(
    val visibleTagList: SnapshotStateList<Tag>,
    val visibleTaskList: SnapshotStateList<Task>,
    val selectedReminderSet: MutableState<String>,
    val onReload: () -> Unit,
    val onClickTag: (Tag) -> Unit,
    val onClickNextReminderSetList: () -> Unit

)

@Composable
fun UseMainViewState(onNewReminderSet: (String) -> Unit): (UseMainViewStateReturn) {
    // Task List Variable
    val taskList = remember {
        mutableStateListOf<Task>()
    }
    val visibleTaskList = remember {
        mutableStateListOf<Task>()
    }

    // Tag List Variable
    val tagList = remember {
        mutableStateListOf<Tag>()
    }
    val visibleTagList = remember {
        mutableStateListOf<Tag>()
    }

    // ReminderSet
    val reminderSetList = remember {
        mutableStateListOf<ReminderSet>()
    }
    val selectedReminderSetNumber = remember {
        mutableIntStateOf(0)
    }
    val selectedReminderSet = remember {
        mutableStateOf("")
    }

    var selectedTag by remember {
        mutableStateOf<Tag?>(null)
    }

    val getTag = {
        GetTags(selectedReminderSet.value) {
            tagList.clear()
            visibleTagList.clear()
            visibleTagList.add(TodayTag)
            tagList.add(TodayTag)
            for (doc in it.documents) {
                val parsedTag = ParseTags(doc)
                tagList.add(ParseTags(doc))
                visibleTagList.add(parsedTag)
            }
        }
    }
    val getTask = {
        GetTasks(selectedReminderSet.value){
            taskList.clear()
            visibleTaskList.clear()
            for (doc in it.documents) {
                val parsedTask = ParseTask(doc)
                taskList.add(parsedTask)
                visibleTaskList.add(parsedTask)
            }

            taskList.sortBy { sortIT -> sortIT.dueDate }
        }
    }

    // Initial Loading
    LaunchedEffect(Unit){
        val reminderSet = GetReminderSet()
        reminderSetList.clear()
        if (reminderSet != null && reminderSet.documents.size != 0) {
            for (doc in reminderSet.documents) {
                reminderSetList.add(ParseReminderSet(doc))
            }
        } else {
            AddReminderSet("💼 Work")
            val result = AddReminderSet("🏠 Home")
            reminderSetList.add(result)
        }
        selectedReminderSet.value = reminderSetList[0].title
        onNewReminderSet(selectedReminderSet.value)

        getTag()
        getTask()
    }

    val onReload = {
        getTag()
        getTask()
    }

    val onClickNextReminderSetList =  {
        selectedReminderSetNumber.intValue = (selectedReminderSetNumber.intValue + 1) % reminderSetList.size
        selectedReminderSet.value = reminderSetList[selectedReminderSetNumber.intValue].title
        onNewReminderSet(selectedReminderSet.value)
        onReload()
    }

    val onClickTag = fun(tag: Tag) {

        // Clear all the visible.
        visibleTagList.clear()
        visibleTaskList.clear()

        if (selectedTag != null) {
            for (tagData in tagList) {
                visibleTagList.add(tagData)
            }
            selectedTag = null

        } else {
            selectedTag = tag
            // Add the selected one to the tag list
            visibleTagList.add(selectedTag!!)
        }

        for (eachTask in taskList) {
            for (eachTag in eachTask.tag) {
                if (selectedTag == null || eachTag.id == selectedTag?.id) {
                    visibleTaskList.add(eachTask)
                    break
                }
            }
        }
    }

    return UseMainViewStateReturn(
        visibleTagList = visibleTagList,
        visibleTaskList =  visibleTaskList,
        selectedReminderSet = selectedReminderSet,
        onReload = onReload,
        onClickTag = onClickTag,
        onClickNextReminderSetList = onClickNextReminderSetList,
    )
}