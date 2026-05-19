package com.trackit.trackit.network

import com.trackit.trackit.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    // Applications
    @GET("api/applications")
    suspend fun getApplications(
        @Header("Authorization") token: String
    ): Response<List<ApplicationResponse>>

    @POST("api/applications")
    suspend fun addApplication(
        @Header("Authorization") token: String,
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    @PUT("api/applications/{id}")
    suspend fun updateApplication(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    @DELETE("api/applications/{id}")
    suspend fun deleteApplication(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Void>

    @GET("api/applications/stats")
    suspend fun getStats(
        @Header("Authorization") token: String
    ): Response<StatsResponse>

    // Reminders
    @POST("api/reminders")
    suspend fun addReminder(
        @Header("Authorization") token: String,
        @Body request: ReminderRequest
    ): Response<ReminderResponse>

    @GET("api/reminders")
    suspend fun getReminders(
        @Header("Authorization") token: String
    ): Response<List<ReminderResponse>>

    @DELETE("api/reminders/{id}")
    suspend fun deleteReminder(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<Void>
}