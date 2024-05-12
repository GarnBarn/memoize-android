package dev.sirateek.memoize

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sirateek.memoize.ui.theme.MemoizeTheme
import dev.sirateek.memoize.views.main.MainView
import dev.sirateek.memoize.views.main.MainViewParam
import dev.sirateek.memoize.views.profile.ProfileView
import dev.sirateek.memoize.views.reminder.CreateReminderView
import dev.sirateek.memoize.views.reminder.ReminderDetailView
import dev.sirateek.memoize.views.tag.TagCreate
import dev.sirateek.memoize.views.tag.TagManage
import kotlin.math.sign

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
        setContent {
            MemoizeTheme {
                Surface {
                    BaseNavHost(applicationContext, signOutFunction = {
                        signOut(applicationContext)
                    })
                }
            }
        }
    }

    private fun signOut(ctx: Context) {
        Firebase.auth.signOut()
        Toast.makeText(ctx, "Successfully signed out", Toast.LENGTH_LONG).show()

        val i = Intent(this, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
        finish()
    }
}

@Composable
fun BaseNavHost(ctx: Context, signOutFunction: () -> Unit) {
    val navController = rememberNavController()
    val reminderSetRemember = remember {
        mutableStateOf("")
    }
    val taskID = remember {
        mutableStateOf("")
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainView(
                param = MainViewParam(
                    onClickProfileIcon = {
                        navController.navigate("profile")
                    },
                    onClickCreateTask = {
                        navController.navigate("create-reminder")
                    },
                    onClickManageTag = {
                        navController.navigate("tag-manage")
                    },
                    onChangeReminderSet = {
                        reminderSetRemember.value = it
                    },
                    onClickTask = {
                        taskID.value = it
                        navController.navigate("reminder-edit")
                    }
                )
            )
        }

        composable("create-reminder") {
            CreateReminderView(
                reminderSetRemember.value,
                ctx,
            ) {
                navController.popBackStack()
            }
        }

        composable("profile") {
            ProfileView(
                {
                    navController.popBackStack()
                },
                signOutFunction,
                )
        }

        composable("tag-manage") {
            TagManage(
                reminderSetRemember.value,
                ctx,
                {
                    navController.popBackStack()
                },
                {
                    navController.navigate("tag-create")
                }
            )
        }

        composable("tag-create") {
            TagCreate(
                reminderSetRemember.value,
                ctx,
            ) {
                navController.popBackStack()
            }
        }

        composable("reminder-edit") {
            ReminderDetailView(reminderSet = reminderSetRemember.value, taskID = taskID.value, ctx = ctx) {
                navController.popBackStack()
            }
        }
    }
}
