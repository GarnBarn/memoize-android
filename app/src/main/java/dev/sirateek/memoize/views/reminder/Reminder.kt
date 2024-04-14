package dev.sirateek.memoize.views.reminder

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import dev.sirateek.memoize.repository.ReminderRepository
import java.text.DateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReminderView(
    onClickBack: () -> Unit,
    onClickSignOut: () -> Unit
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
                    state.selectedDateMillis?.plus((timeState.hour * 60 * 60 * 1000 + timeState.minute * 60 * 1000))
                result?.let { Date(it) }
                    ?.let { CreateReminder(reminderName, reminderDescription, it, onClickBack) }
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

fun CreateReminder(title: String, description: String, reminderDateTime: Date, onSuccess: () -> Unit) {
    val user = Firebase.auth.currentUser?.uid
    val docData = hashMapOf(
        "title" to title,
        "description" to description,
        "reminder_date_time" to reminderDateTime,
        "uid" to user,
        "created_at" to Date()
    )
    ReminderRepository().add(docData).addOnSuccessListener {
        Log.d(TAG, "DocumentSnapshot written with ID: ${it.id}")
        onSuccess()
    }
}