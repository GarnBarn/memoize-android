package dev.sirateek.memoize.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import dev.sirateek.memoize.components.DotsIndicator
import dev.sirateek.memoize.components.TagBox
import dev.sirateek.memoize.components.TagBoxParam
import dev.sirateek.memoize.components.TaskCard
import dev.sirateek.memoize.components.TaskCardParam
import dev.sirateek.memoize.models.Tag
import dev.sirateek.memoize.models.TagList
import dev.sirateek.memoize.models.Task
import kotlin.math.round

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainView() {
    Column {

        val userProfile = Firebase.auth.currentUser?.photoUrl.toString()

        // Header Section
        Box(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("🏠 Home", style= TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
                Image(
                    painter = rememberAsyncImagePainter(userProfile),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(999.dp))
                    )
            }
        }

        Box(modifier = Modifier.padding(start=20.dp, top=10.dp, bottom=10.dp)) {
            Row {
                TagBox(
                    param = TagBoxParam(
                        tag = Tag(icon = "✏️"),
                        Modifier.padding(horizontal = 5.dp),
                        Color.Gray,
                    )
                )

                val scrollState1 = rememberScrollState()
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.horizontalScroll(scrollState1)
                ) {

                    for (i in 0..20) {
                        TagBox(
                            param = TagBoxParam(
                                tag = Tag(title = "Test"),
                                Modifier.padding(horizontal = 5.dp),
                                Color.Gray,
                            ))
                    }
                }
            }
        }


        // Tasks Section
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            for (i in 0..20) {
                Box(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
                    TaskCard(param =
                    TaskCardParam(
                        Task(
                            id = "Test",
                            title = "Test",
                            dueDate = DateTime.getDefaultInstance(),
                            tag = TagList(
                                tags = arrayOf(Tag(color = "#1FF110")),
                            ),
                        ),
                        Modifier,
                    ) {
                        Log.i("Debug", "Test")
                    }
                    )
                }
            }
        }
    }
}