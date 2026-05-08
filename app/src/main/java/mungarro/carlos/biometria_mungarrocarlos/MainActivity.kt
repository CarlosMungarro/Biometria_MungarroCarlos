package mungarro.carlos.biometria_mungarrocarlos

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import mungarro.carlos.biometria_mungarrocarlos.datastore.DataStoreManager
import mungarro.carlos.biometria_mungarrocarlos.screens.HomeScreen
import mungarro.carlos.biometria_mungarrocarlos.screens.LoginScreen
import mungarro.carlos.biometria_mungarrocarlos.ui.theme.Biometria_MungarroCarlosTheme
import mungarro.carlos.biometria_mungarrocarlos.viewmodels.LoginViewModel
import mungarro.carlos.biometria_mungarrocarlos.viewmodels.LoginViewModelFactory

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Biometria_MungarroCarlosTheme {
                val dataStoreManager = remember { DataStoreManager(this) }
                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(dataStoreManager)
                )
                var isLoggedIn by remember { mutableStateOf(false) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoggedIn) {
                        HomeScreen(
                            innerPadding = innerPadding,
                            context = this,
                            viewModel = viewModel,
                            onLogout = { isLoggedIn = false }
                        )
                    } else {
                        LoginScreen(
                            innerPadding = innerPadding,
                            context = this,
                            viewModel = viewModel,
                            onLoginSuccess = { isLoggedIn = true }
                        )
                    }
                }
            }
        }
    }
}