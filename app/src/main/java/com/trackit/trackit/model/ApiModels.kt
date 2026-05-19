package com.trackit.trackit.model

data class AuthRequest(
    val name: String = "",
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val name: String,
    val email: String
)

data class ApplicationRequest(
    val companyName: String,
    val role: String,
    val jobType: String,
    val status: String,
    val appliedDate: String?,
    val deadline: String?,
    val salary: String?,
    val notes: String?
)

data class ApplicationResponse(
    val id: Long,
    val companyName: String,
    val role: String,
    val jobType: String,
    val status: String,
    val appliedDate: String?,
    val deadline: String?,
    val salary: String?,
    val notes: String?,
    val createdAt: String
)

data class StatsResponse(
    val total: Long,
    val applied: Long,
    val shortlisted: Long,
    val interviews: Long,
    val offers: Long,
    val rejected: Long
)

data class ReminderRequest(
    val applicationId: Long,
    val remindAt: String,
    val message: String
)

data class ReminderResponse(
    val id: Long,
    val applicationId: Long,
    val companyName: String,
    val role: String,
    val remindAt: String,
    val message: String
)