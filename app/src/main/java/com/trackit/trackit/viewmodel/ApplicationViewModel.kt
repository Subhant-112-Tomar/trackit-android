package com.trackit.trackit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackit.trackit.model.ApplicationRequest
import com.trackit.trackit.model.ApplicationResponse
import com.trackit.trackit.model.StatsResponse
import com.trackit.trackit.repository.ApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AppState {
    object Idle : AppState()
    object Loading : AppState()
    object Success : AppState()
    data class Error(val message: String) : AppState()
}

class ApplicationViewModel : ViewModel() {

    private val repository = ApplicationRepository()

    private val _applications = MutableStateFlow<List<ApplicationResponse>>(emptyList())
    val applications: StateFlow<List<ApplicationResponse>> = _applications

    private val _stats = MutableStateFlow<StatsResponse?>(null)
    val stats: StateFlow<StatsResponse?> = _stats

    private val _appState = MutableStateFlow<AppState>(AppState.Idle)
    val appState: StateFlow<AppState> = _appState

    fun loadApplications(token: String) {
        viewModelScope.launch {
            _appState.value = AppState.Loading
            repository.getApplications(token).fold(
                onSuccess = {
                    _applications.value = it
                    _appState.value = AppState.Idle
                },
                onFailure = {
                    _appState.value = AppState.Error(it.message ?: "Failed to load")
                }
            )
        }
    }

    fun loadStats(token: String) {
        viewModelScope.launch {
            repository.getStats(token).fold(
                onSuccess = { _stats.value = it },
                onFailure = {}
            )
        }
    }

    fun addApplication(token: String, request: ApplicationRequest) {
        viewModelScope.launch {
            _appState.value = AppState.Loading
            repository.addApplication(token, request).fold(
                onSuccess = {
                    loadApplications(token)
                    _appState.value = AppState.Success
                },
                onFailure = {
                    _appState.value = AppState.Error(it.message ?: "Failed to add")
                }
            )
        }
    }

    fun updateApplication(token: String, id: Long, request: ApplicationRequest) {
        viewModelScope.launch {
            _appState.value = AppState.Loading
            repository.updateApplication(token, id, request).fold(
                onSuccess = {
                    loadApplications(token)
                    loadStats(token)
                    _appState.value = AppState.Success
                },
                onFailure = {
                    _appState.value = AppState.Error(it.message ?: "Failed to update")
                }
            )
        }
    }

    fun deleteApplication(token: String, id: Long) {
        viewModelScope.launch {
            repository.deleteApplication(token, id).fold(
                onSuccess = {
                    loadApplications(token)
                    loadStats(token)
                },
                onFailure = {}
            )
        }
    }

    fun resetState() {
        _appState.value = AppState.Idle
    }
}