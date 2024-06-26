package dev.sirateek.memoize.views.tag

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.Task
import dev.sirateek.memoize.repository.ReminderRepository
import dev.sirateek.memoize.repository.TagRepository

@Composable
fun TagManage(
    reminderSet: String,
    ctx: Context,
    onClickBack: () -> Unit,
    onClickCreateTag: () -> Unit
) {

    val tagList = remember {
        mutableStateListOf<Tag>()
    }


    GetTags(reminderSet) {
        tagList.clear()
        for (tagDoc in it.documents) {
            tagList.add(ParseTags(tagDoc))
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickCreateTag,
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        },
        content = { it ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .padding(5.dp)
            ) {
                Row {
                    Button(onClick = onClickBack) {
                        Text(text = "<")
                    }
                    Text(
                        "Manage Tags",
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 25.sp),
                        modifier = Modifier.padding(5.dp)
                    )
                }

                val scrollState = rememberScrollState()
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    for (tagData in tagList) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Box(modifier = Modifier.padding(15.dp)) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    TagBox(
                                        param = TagBoxParam(
                                            tag = tagData,
                                            overrideColor = null,
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            RemoveTag(ctx,tagData.id, {
                                                GetTags(reminderSet) {
                                                    tagList.clear()
                                                    for (tagDoc in it.documents) {
                                                        tagList.add(ParseTags(tagDoc))
                                                    }
                                                }
                                            })
                                        },
                                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                                    ) {
                                        Text("Delete")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

fun GetTags(reminderSet: String,onSuccess: (QuerySnapshot) -> Unit) {
    val uid = Firebase.auth.currentUser?.uid
    TagRepository().whereEqualTo("uid", uid).whereEqualTo("reminder_set", reminderSet).get().addOnSuccessListener(onSuccess)
}

fun ParseTags(doc: DocumentSnapshot): Tag {
    val result = Tag()
    result.id = doc.id
    result.title = doc.getString("title")
    result.color = doc.getString("color").toString()
    result.icon = doc.getString("icon")
    result.reminderSet = doc.getString("reminder_set").toString()

    return result
}

fun RemoveTag(ctx: Context, id: String, onDeleteSuccess: () -> Unit) {
    TagRepository().document(id).delete().addOnSuccessListener {
        Toast.makeText(ctx, "Tag $id deleted", Toast.LENGTH_LONG).show()
        onDeleteSuccess()
    }
}