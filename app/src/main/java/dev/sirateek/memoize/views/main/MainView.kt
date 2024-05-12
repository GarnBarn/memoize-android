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
import dev.sirateek.memoize.models.ReminderSet
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.repository.ReminderRepository
import dev.sirateek.memoize.views.main.composable.UseMainViewState

val TodayTag = Tag("today", title = "today", isRealTag = false, color = "#8f8f8f")
class MainViewParamParameterProvider : PreviewParameterProvider<MainViewParam> {
    override val values = sequenceOf(
        MainViewParam()
    )
}
data class MainViewParam (
    val onClickCreateTask: () -> Unit = {},
    val onClickProfileIcon: () ->Unit = {},
    val onClickManageTag: () -> Unit = {},
    val onChangeReminderSet: (name: String) -> Unit = {},
    val onClickTask: (taskID: String) -> Unit = {},
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainView(
    @PreviewParameter(MainViewParamParameterProvider::class) param: MainViewParam
) {
    val useMainViewState = UseMainViewState(param.onChangeReminderSet)

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
                    useMainViewState.selectedReminderSet,
                    onClickProfileIcon = param.onClickProfileIcon,
                    onClickReload = useMainViewState.onReload,
                    onClickNextReminderSet = useMainViewState.onClickNextReminderSetList
                )
                TagListSection(
                    tags = TagList(
                        tags = useMainViewState.visibleTagList,
                    ),
                    onClickManageTag = param.onClickManageTag,
                    onClickSomeTag = {
                        useMainViewState.onClickTag(it)
                    }
                )
                TaskListSection(param = useMainViewState.visibleTaskList, onClickTask = param.onClickTask,onClickTag = useMainViewState.onClickTag)
            }
        }
    )
}

fun GetTasks(
    reminderSet: String,
    onSuccess: (QuerySnapshot) -> Unit
) {
    val uid = Firebase.auth.currentUser?.uid
    ReminderRepository().whereEqualTo("uid", uid).whereEqualTo("reminder_set", reminderSet).get().addOnSuccessListener(onSuccess)
}