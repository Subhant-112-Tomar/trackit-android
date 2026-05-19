package com.trackit.trackit.repository

import com.trackit.trackit.model.ApplicationRequest
import com.trackit.trackit.model.ApplicationResponse
import com.trackit.trackit.model.StatsResponse
import com.trackit.trackit.network.RetrofitInstance

class ApplicationRepository {

    private val api = RetrofitInstance.api

    suspend fun getApplications(token: String): Result<List<ApplicationResponse>> {
        return try {
            val response = api.getApplications("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch applications"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addApplication(
        token: String,
        request: ApplicationRequest
    ): Result<ApplicationResponse> {
        return try {
            val response = api.addApplication("Bearer $token", request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to add application"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateApplication(
        token: String,
        id: Long,
        request: ApplicationRequest
    ): Result<ApplicationResponse> {
        return try {
            val response = api.updateApplication("Bearer $token", id, request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update application"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteApplication(token: String, id: Long): Result<Unit> {
        return try {
            val response = api.deleteApplication("Bearer $token", id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete application"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getStats(token: String): Result<StatsResponse> {
        return try {
            val response = api.getStats("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch stats"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}