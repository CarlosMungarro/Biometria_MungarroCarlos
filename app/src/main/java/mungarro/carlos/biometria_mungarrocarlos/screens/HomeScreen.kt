package mungarro.carlos.biometria_mungarrocarlos.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mungarro.carlos.biometria_mungarrocarlos.viewmodels.LoginViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    context: Context,
    viewModel: LoginViewModel,
    onLogout: () -> Unit
) {
    val username by viewModel.username.collectAsState(initial = "")
    val biometricsActive by viewModel.biometricsActive.collectAsState(initial = false)

    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {

        // 1.nombre de usuario
        Text(
            text = "Hola, $username",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(24.dp))

        // 3. Toggle biométricos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Autenticación biométrica", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = biometricsActive,
                onCheckedChange = { viewModel.toggleBiometrics(it) }
            )
        }
        Spacer(Modifier.height(24.dp))

        // 2. Botón cerrar sesión
        Button(
            onClick = {
                viewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) { Text("Cerrar sesión") }
    }
}