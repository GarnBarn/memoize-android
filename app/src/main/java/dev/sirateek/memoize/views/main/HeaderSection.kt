package dev.sirateek.memoize.views.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.sirateek.memoize.components.ProfileImage

@Composable
fun HeaderSection(
    reminderSetTitle: MutableState<String>,
    onClickProfileIcon: ()->Unit = {},
    onClickReload: ()->Unit = {},
    onClickNextReminderSet: ()->Unit = {}
) {
    // Header Section
    Box(modifier = Modifier.padding(20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row{
                Text(reminderSetTitle.value, style= TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
                IconButton(onClick = onClickNextReminderSet) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next"
                    )
                }
            }
            Row {
                IconButton(onClick = onClickReload) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "reload"
                    )
                }
                ProfileImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .clickable {
                            onClickProfileIcon()
                        }
                )
            }

        }
    }
}