package dev.sirateek.memoize.views.tag

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.repository.TagRepository

@Composable
fun TagCreate(
    reminderSet: String,
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

        val tagColor = remember {
            mutableStateOf(Color(0))
        }

        val tagColorHex = remember {
            mutableStateOf("")
        }

        val scrollState = rememberScrollState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                content = {
                          Text(text ="Color", modifier = Modifier.padding(20.dp))
                },
                colors = CardDefaults.cardColors(
                    containerColor = tagColor.value,
                )
            )

            val controller = rememberColorPickerController()
            HsvColorPicker(modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(10.dp),
                controller = controller,
                onColorChanged = {
                    tagColor.value = it.color
                    tagColorHex.value = it.hexCode
                }
                )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )
            Button(onClick = {
                CreateTag(onClickBack, tagName, tagIcon, tagColorHex.value, reminderSet)
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text("Create")
            }
        }
    }
}

fun CreateTag(onClickBack: () -> Unit, title: String, icon: String, color: String, reminderSet: String) {
    val uid = Firebase.auth.currentUser?.uid

    val data = hashMapOf(
        "title" to title,
        "icon" to icon,
        "color" to "#$color",
        "uid" to uid,
        "reminder_set" to reminderSet,
    )
    TagRepository().add(data).addOnSuccessListener {
        onClickBack()
        onClickBack()
    }
}