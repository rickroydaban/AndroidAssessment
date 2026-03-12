package rickaban.projects.androidassessment.app.nav

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.ui.NavDisplay
import rickaban.projects.androidassessment.login.LoginScreen
import rickaban.projects.androidassessment.pagetwo.PageTwoScreen
import rickaban.projects.androidassessment.pininput.PinInputScreen

@SuppressLint("SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph() {
    val navigator = rememberAppNavigator(initialRoute = Login)
    val context = LocalContext.current

    val entries: List<NavEntry<NavKey>> = navigator.entries.map { key ->
        when (key) {
            is Login -> NavEntry(key) {
                LoginScreen(
                    onLoginSuccess = {
                        navigator.navigateTo(PageTwo)
                    }
                )
            }
            is PageTwo -> NavEntry(key) {
                PageTwoScreen(
                    onNextPage = {
                        navigator.navigateTo(PinInput)
                    },
                    onExitApp = {
                        (context as? ComponentActivity)?.finish()
                    }
                )
            }
            is PinInput -> NavEntry(key) {
                PinInputScreen(
                    onPinSubmit = {
                        //no specific instructions for this yet
                    }
                )
            }
            else -> NavEntry(key) {
                Text("Unknown")
            }
        }
    }

    val decoratedEntries = rememberDecoratedNavEntries(entries)
    Scaffold { padding ->
        NavDisplay(
            entries = decoratedEntries,
            modifier = Modifier.padding(padding),
            onBack = {
                if (navigator.isAtRoot()) {
                    (context as? ComponentActivity)?.finish()
                } else {
                    navigator.goBack() // Exactly what you asked for!
                }
            }
        )
    }
}