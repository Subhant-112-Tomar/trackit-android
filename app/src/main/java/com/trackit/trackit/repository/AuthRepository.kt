package com.trackit.trackit.repository

import com.trackit.trackit.model.AuthRequest
import com.trackit.trackit.model.AuthResponse
import com.trackit.trackit.network.RetrofitInstance

class AuthRepository {

    private val api = RetrofitInstance.api

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.register(AuthRequest(name, email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = api.login(AuthRequest(email = email, password = password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Invalid email or password"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}