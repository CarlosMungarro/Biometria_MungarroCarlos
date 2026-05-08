package mungarro.carlos.biometria_mungarrocarlos.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import mungarro.carlos.biometria_mungarrocarlos.datastore.DataStoreManager

class LoginViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {
    

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome

    val isLoggedIn = dataStoreManager.isLoggedInFlow
    val username = dataStoreManager.usernameFlow
    val biometricsActive = dataStoreManager.biometricsFlow

    fun login(email: String, password: String) {
        val emailTrimmed = email.trim()
        val passwordTrimmed = password.trim()

        if (emailTrimmed.isNotEmpty() && passwordTrimmed.isNotEmpty()) {
            viewModelScope.launch {
                dataStoreManager.saveSession(emailTrimmed)
                _navigateToHome.value = true
            }
        }
    }

    fun loginWithBiometrics() {
        viewModelScope.launch {
            dataStoreManager.loginWithBiometrics()
            _navigateToHome.value = true
        }
    }

    fun logout() {
        viewModelScope.launch {
            val biometrics = dataStoreManager.biometricsFlow.first()
            dataStoreManager.logout(biometrics)
            _navigateToHome.value = false
        }
    }

    fun toggleBiometrics(active: Boolean) {
        viewModelScope.launch {
            dataStoreManager.activateBiometrics(active)
        }
    }

    fun resetNavigation() {
        _navigateToHome.value = false
    }
}

class LoginViewModelFactory(
    private val dataStoreManager: DataStoreManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(dataStoreManager) as T
    }
}