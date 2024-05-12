package dev.sirateek.memoize.views.reminder

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.models.ReminderSet
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.repository.ReminderRepository
import dev.sirateek.memoize.repository.ReminderSetRepository
import dev.sirateek.memoize.views.main.TodayTag
import dev.sirateek.memoize.views.tag.GetTags
import dev.sirateek.memoize.views.tag.ParseTags
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderView(
    reminderSet: String,
    ctx: Context,
    onClickBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row {
            Button(onClick = onClickBack) {
                Text(text = "<")
            }
            Text(
                "Create Reminder / Tasks",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
                modifier = Modifier.padding(5.dp)
            )
        }

        val state = rememberDatePickerState()
        val openDialog = remember { mutableStateOf(false) }
        var expanded by remember {
            mutableStateOf(false)
        }

        var reminderName by remember {
            mutableStateOf("")
        }
        var stubDateState by remember {
            mutableStateOf("")
        }
        var reminderDescription by remember {
            mutableStateOf("")
        }
        val timeState = rememberTimePickerState()
        val scrollState = rememberScrollState()
        var selectedTag by remember {
            mutableStateOf<Tag?>(null)
        }


        val tagList = remember {
            mutableStateListOf<Tag>()
        }

        LaunchedEffect(Unit) {
            GetReminderSet()
        }

        GetTags(reminderSet) {
            tagList.clear()
            for (doc in it.documents) {
                tagList.add(ParseTags(doc))
            }
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = reminderName,
                onValueChange = {
                    reminderName = it
                },
                label = {
                    Text(text = "Reminder Name")
                }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                value = reminderDescription,
                onValueChange = {
                    reminderDescription = it
                },
                minLines = 10,
                label = {
                    Text(text = "Description")
                }
            )

           Card {
               Column(modifier = Modifier.padding(10.dp)) {
                   
                   Text(text = "Tag")
                   
                   Row(modifier = Modifier.padding(vertical = 10.dp)) {
                        if (selectedTag != null) {
                            TagBox(param = TagBoxParam(tag = selectedTag!!, modifier = Modifier, null))
                        }

                       Box(
                           modifier = Modifier
                               .fillMaxWidth()
                               .wrapContentSize(Alignment.TopEnd)
                       ) {
                           IconButton(onClick = { expanded = !expanded }) {
                               Icon(
                                   imageVector = Icons.Default.Edit,
                                   contentDescription = "Add"
                               )
                           }

                           DropdownMenu(
                               expanded = expanded,
                               onDismissRequest = { expanded = false }) {
                               for (tagData in tagList) {
                                   DropdownMenuItem(
                                       text = {
                                       TagBox(param = TagBoxParam(tag = tagData, Modifier, null, onClick = {
                                           selectedTag = tagData
                                           expanded = false
                                       }))
                                        },
                                       onClick = {
                                           selectedTag = tagData
                                           expanded = false
                                       })
                               }
                           }
                       }
                   }
               }

           }

            Card(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Reminder Date & Time",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openDialog.value = true
                            },
                        value = stubDateState,
                        onValueChange = {},
                        label = {
                            Text(text = "Date")
                        },
                        enabled = false,
                    )

                    TimeInput(
                        state = timeState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            Button(onClick = {
                val result =
                    state.selectedDateMillis?.plus((timeState.hour * 60 * 60 * 1000 + timeState.minute * 60 * 1000) - (7 * 3600000))
                if (selectedTag == null) {
                    Toast.makeText(ctx, "Please select the tag", Toast.LENGTH_LONG).show()
                    return@Button
                }
                result?.let { Date(it) }
                    ?.let { CreateReminder(reminderName, reminderDescription, it, selectedTag!!,onClickBack) }
            }) {
                Text(text = "Submit")


                if (openDialog.value) {
                    DatePickerDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false

                                    val formattedDate = state.selectedDateMillis?.let { Date(it) }
                                    stubDateState = formattedDate.toString()
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("Close")
                            }
                        }
                    ) {
                        DatePicker(
                            state = state
                        )
                    }
                }
            }
        }
    }
}

fun CreateReminder(title: String, description: String, reminderDateTime: Date, tag: Tag, onSuccess: () -> Unit) {
    val user = Firebase.auth.currentUser?.uid
    val tagHash = hashMapOf(
        "id" to tag.id,
        "title" to tag.title,
        "color" to tag.color,
        "icon" to tag.icon,
    )
    val docData = hashMapOf(
        "title" to title,
        "description" to description,
        "reminder_date_time" to reminderDateTime,
        "uid" to user,
        "created_at" to Date(),
        "tags" to listOf(tagHash),
        "reminder_set" to tag.reminderSet
    )
    ReminderRepository().add(docData).addOnSuccessListener {
        Log.d(TAG, "DocumentSnapshot written with ID: ${it.id}")
        onSuccess()
    }
}

suspend fun GetReminderSet(): QuerySnapshot? {
    val user = Firebase.auth.currentUser?.uid
    return ReminderSetRepository().whereEqualTo("uid", user).get().await()
}

fun AddReminderSet(name: String): ReminderSet {
    val user = Firebase.auth.currentUser?.uid
    ReminderSetRepository().add(hashMapOf(
        "uid" to user,
        "title" to name,
    ))

    return ReminderSet(
        uid = user!!,
        title = name,
    )
}

fun ParseReminderSet(doc: DocumentSnapshot): ReminderSet {
    val result = ReminderSet()
    result.id = doc.id
    result.title = doc.getString("title").toString()
    result.uid = doc.getString("uid").toString()
    return result
}

fun ParseTask(result: DocumentSnapshot): Task {
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
        res.tag.tags.add(0, TodayTag)
    }

    return res
}