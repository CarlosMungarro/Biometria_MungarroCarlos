package mungarro.carlos.biometria_mungarrocarlos.screens

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import mungarro.carlos.biometria_mungarrocarlos.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    innerPadding: PaddingValues,
    context: Context,
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var authStatus by remember { mutableStateOf("Esperando autenticación") }
    var biometricAvailable by remember { mutableStateOf(false) }


    val navigateToHome by viewModel.navigateToHome.collectAsState()
    val biometricsActive by viewModel.biometricsActive.collectAsState(initial = false)

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            onLoginSuccess()
            viewModel.resetNavigation()
        }
    }

    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricAvailable = true
                authStatus = "Biométricos disponibles."
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                authStatus = "Sin sensor biométrico."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                authStatus = "Sensor no disponible."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                authStatus = "Registra tus biométricos en ajustes."
        }
    }

    val activity = context as FragmentActivity
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = remember {
        BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    authStatus = "Error: $errString"
                }
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    viewModel.loginWithBiometrics()
                }
                override fun onAuthenticationFailed() {
                    authStatus = "Huella no reconocida, intenta de nuevo."
                }
            }
        )
    }

    val promptInfo = remember {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación biométrica")
            .setSubtitle("Usa tu huella o rostro para iniciar")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    Column(modifier = Modifier
        .padding(innerPadding)
        .padding(16.dp)
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        //  Llama a viewModel.login()
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Iniciar sesión") }

        //  Solo visible si biométricos están activos y disponibles
        if (biometricsActive && biometricAvailable) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { biometricPrompt.authenticate(promptInfo) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Entrar con huella") }
        }

        Spacer(Modifier.height(16.dp))
        Text(authStatus)
    }
}