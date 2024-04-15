package dev.sirateek.memoize.views.tag

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.repository.TagRepository
import dev.sirateek.memoize.views.reminder.CreateReminder
import java.util.Date

@Composable
fun TagCreate(
    mContext: Context,
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
                "Create Tag",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
                modifier = Modifier.padding(5.dp)
            )
        }


        var tagName by remember {
            mutableStateOf("")
        }
        var tagIcon by remember {
            mutableStateOf("")
        }
        var tagColor by remember {
            mutableStateOf("")
        }

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
                value = tagName,
                onValueChange = {
                    tagName = it
                },
                label = {
                    Text(text = "Tag Name")
                }
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                value = tagIcon,
                onValueChange = {
                    Log.d("Test", it.length.toString())
                    if (it.length <= 2) tagIcon = it
                },
                label = {
                    Text(text = "Tag Icon")
                }
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                value = tagColor,
                onValueChange = {
                    tagColor = it
                },
                label = {
                    Text(text = "Color")
                }
            )
            Button(onClick = {
                CreateTag(onClickBack, tagName, tagIcon, tagColor)
            }, modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text("Create")
            }
        }
    }
}

fun CreateTag(onClickBack: () -> Unit, title: String, icon: String, color: String) {
    val uid = Firebase.auth.currentUser?.uid

    val data = hashMapOf(
        "title" to title,
        "icon" to icon,
        "color" to color,
        "uid" to uid,
    )
    TagRepository().add(data).addOnSuccessListener {
        onClickBack()
        onClickBack()
    }
}