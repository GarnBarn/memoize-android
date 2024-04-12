package dev.sirateek.memoize.views.profile

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.MainActivity
import dev.sirateek.memoize.R
import dev.sirateek.memoize.components.ProfileImage
import dev.sirateek.memoize.providers

@Composable
fun ProfileView(
    ctx: Context,
    onClickBack: () -> Unit,
    onClickSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(5.dp)
    ) {
        Button(onClick=onClickBack) {
            Text(text = "<")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(999999.dp)),
                imageSize = "400",
            )

            val user = Firebase.auth.currentUser

            Text(text = user?.displayName.toString(), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            Text(text = user?.uid.toString(), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 10.sp))

            Button(
                onClick = {
                    onClickSignOut()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Text(text = "Sign Out")
            }
        }
    }

}