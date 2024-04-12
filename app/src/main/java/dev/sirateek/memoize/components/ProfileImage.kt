package dev.sirateek.memoize.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileImage(modifier: Modifier, imageSize: String="96") {
    val userProfile = Firebase.auth.currentUser?.photoUrl.toString()

    val splitResult = userProfile.split("=")
    val result = "${splitResult[0]}=s${imageSize}-c"
    Image(
        painter = rememberAsyncImagePainter(result),
        contentDescription = null,
        modifier = modifier
    )
}