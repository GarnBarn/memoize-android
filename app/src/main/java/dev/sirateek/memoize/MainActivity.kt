package dev.sirateek.memoize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.sirateek.memoize.ui.theme.MemoizeTheme
import dev.sirateek.memoize.views.MainView

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MemoizeTheme {
                Surface {
                    MainView()
                }
            }
        }
    }
}
