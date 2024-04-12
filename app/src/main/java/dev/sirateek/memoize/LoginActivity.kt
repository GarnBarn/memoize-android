package dev.sirateek.memoize

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dev.sirateek.memoize.ui.theme.MemoizeTheme

val providers = arrayListOf(
    AuthUI.IdpConfig.GoogleBuilder().build(),
)

class LoginActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.d("Firebase User", user?.email.toString())
            routeToMainActivity()
        } else {
            Toast.makeText(this, "Error: ${response?.error.toString()}", Toast.LENGTH_LONG).show()
        }
    }

    fun signInWithGoogle() {
        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.memory)
            .build()
        signInLauncher.launch(signInIntent)
    }

    fun routeToMainActivity() {
        Toast.makeText(this, "Welcome ${Firebase.auth.currentUser?.email.toString()} to Memoize", Toast.LENGTH_LONG).show()
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is already logged in.
        // Initialize Firebase Auth
        auth = Firebase.auth

        if (auth.currentUser != null) {
            Log.d("Firebase User", auth.currentUser?.email.toString())
            routeToMainActivity()
        }

        setContent {
            MemoizeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onClick = {
                            signInWithGoogle()
                        }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("Auth", "No User Logged in")
        }
    }
}

@Composable
fun MainScreen(onClick: () -> Unit) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.memory),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    text = "Memoize",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp), onClick = {
                    onClick()
                }) {
                    Text("SignIn with Google")
                }
            }
        }
}