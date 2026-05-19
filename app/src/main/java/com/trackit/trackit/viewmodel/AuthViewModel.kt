package com.trackit.trackit.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.trackit.repository.AuthRepository
import com.trackit.trackit.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun register(context: Context, name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.register(name, email, password)
            result.fold(
                onSuccess = { response ->
                    TokenManager(context).saveUser(
                        response.token,
                        response.name,
                        response.email
                    )
                    _authState.value = AuthState.Success
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Registration failed")
                }
            )
        }
    }

    fun login(context: Context, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            result.fold(
                onSuccess = { response ->
                    TokenManager(context).saveUser(
                        response.token,
                        response.name,
                        response.email
                    )
                    _authState.value = AuthState.Success
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Login failed")
                }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}