package dev.sirateek.memoize.views.reminder

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.views.tag.GetTags
import dev.sirateek.memoize.views.tag.ParseTags
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDetailView(
    reminderSet: String,
    taskID: String,
    ctx: Context,
    onClickBack: () -> Unit
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
                "Edit Task",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
                modifier = Modifier.padding(5.dp)
            )
        }

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            Log.i("Test: ", uri.toString())
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
            val task = GetTask(taskID)
            reminderName = task.title.toString()
            reminderDescription = task.description.toString()
            selectedTag = task.tag.getRealTag()
            stubDateState = task.dueDate.toString()
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
            
//            Button(onClick = {
//                launcher.launch(
//                    PickVisualMediaRequest(
//                        //Here we request only photos. Change this to .ImageAndVideo if
//                        //you want videos too.
//                        //Or use .VideoOnly if you only want videos.
//                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
//                    )
//                )
//            }) {
//                Text(text = "Test")
//            }

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
                    ?.let { UpdateReminder(taskID,reminderName, reminderDescription, it, selectedTag!!, reminderSet,onClickBack) }
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