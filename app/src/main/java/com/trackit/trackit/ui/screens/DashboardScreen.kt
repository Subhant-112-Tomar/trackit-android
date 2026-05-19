package com.trackit.trackit.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trackit.trackit.model.ApplicationRequest
import com.trackit.trackit.model.ApplicationResponse
import com.trackit.trackit.utils.TokenManager
import com.trackit.trackit.viewmodel.ApplicationViewModel
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ApplicationViewModel,
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token by tokenManager.token.collectAsState(initial = null)
    val applications by viewModel.applications.collectAsState()
    val stats by viewModel.stats.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedApplication by remember { mutableStateOf<ApplicationResponse?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(token) {
        token?.let {
            viewModel.loadApplications(it)
            viewModel.loadStats(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "TrackIt",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkTheme)
                                Icons.Default.LightMode
                            else
                                Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = "Add") },
                text = { Text("Add Application") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                stats?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard("Total", it.total.toString(),
                            MaterialTheme.colorScheme.primary, Modifier.weight(1f))
                        StatCard("Applied", it.applied.toString(),
                            MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                        StatCard("Offers", it.offers.toString(),
                            MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard("Shortlisted", it.shortlisted.toString(),
                            MaterialTheme.colorScheme.tertiary, Modifier.weight(1f))
                        StatCard("Interview", it.interviews.toString(),
                            MaterialTheme.colorScheme.secondary, Modifier.weight(1f))
                        StatCard("Rejected", it.rejected.toString(),
                            MaterialTheme.colorScheme.error, Modifier.weight(1f))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Applications",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${applications.size} total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            if (applications.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📋", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No applications yet!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Tap the button below to add one",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            } else {
                items(applications, key = { it.id }) { app ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        ApplicationCard(
                            application = app,
                            onDelete = {
                                token?.let { t -> viewModel.deleteApplication(t, app.id) }
                            },
                            onEdit = { selectedApplication = app }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    if (showAddDialog) {
        AddApplicationDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { request ->
                token?.let { viewModel.addApplication(it, request) }
                showAddDialog = false
            }
        )
    }

    selectedApplication?.let { app ->
        EditApplicationDialog(
            application = app,
            onDismiss = { selectedApplication = null },
            onUpdate = { request ->
                token?.let { t -> viewModel.updateApplication(t, app.id, request) }
                selectedApplication = null
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StatCard(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = color)
            Text(label, fontSize = 11.sp, color = color.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ApplicationCard(
    application: ApplicationResponse,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(application.companyName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(application.role, color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp)
                }
                StatusChip(application.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = application.jobType,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp))
                    }
                }
            }

            application.notes?.let { notes ->
                if (notes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "📝 $notes", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (color, emoji) = when (status) {
        "APPLIED" -> Pair(MaterialTheme.colorScheme.primary, "📤")
        "SHORTLISTED" -> Pair(MaterialTheme.colorScheme.tertiary, "⭐")
        "INTERVIEW" -> Pair(MaterialTheme.colorScheme.secondary, "🎯")
        "OFFER" -> Pair(MaterialTheme.colorScheme.tertiary, "🎉")
        "REJECTED" -> Pair(MaterialTheme.colorScheme.error, "❌")
        else -> Pair(MaterialTheme.colorScheme.outline, "📋")
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = "$emoji $status",
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun AddApplicationDialog(
    onDismiss: () -> Unit,
    onAdd: (ApplicationRequest) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var jobType by remember { mutableStateOf("INTERNSHIP") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Application", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Text("Job Type:", fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    listOf("INTERNSHIP", "JOB").forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = jobType == type, onClick = { jobType = type })
                            Text(type, fontSize = 13.sp)
                        }
                    }
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (companyName.isNotBlank() && role.isNotBlank()) {
                        onAdd(ApplicationRequest(
                            companyName = companyName,
                            role = role,
                            jobType = jobType,
                            status = "APPLIED",
                            appliedDate = null,
                            deadline = null,
                            salary = null,
                            notes = notes.ifBlank { null }
                        ))
                    }
                },
                shape = RoundedCornerShape(12.dp)
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun EditApplicationDialog(
    application: ApplicationResponse,
    onDismiss: () -> Unit,
    onUpdate: (ApplicationRequest) -> Unit
) {
    var companyName by remember { mutableStateOf(application.companyName) }
    var role by remember { mutableStateOf(application.role) }
    var jobType by remember { mutableStateOf(application.jobType) }
    var status by remember { mutableStateOf(application.status) }
    var notes by remember { mutableStateOf(application.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Application", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Company Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Role") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Text("Status:", fontWeight = FontWeight.Medium)
                listOf("APPLIED", "SHORTLISTED", "INTERVIEW", "OFFER", "REJECTED").forEach { s ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = status == s, onClick = { status = s })
                        Text(s, fontSize = 13.sp)
                    }
                }
                Text("Job Type:", fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    listOf("INTERNSHIP", "JOB").forEach { type ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = jobType == type, onClick = { jobType = type })
                            Text(type, fontSize = 13.sp)
                        }
                    }
                }
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdate(ApplicationRequest(
                        companyName = companyName,
                        role = role,
                        jobType = jobType,
                        status = status,
                        appliedDate = application.appliedDate,
                        deadline = application.deadline,
                        salary = application.salary,
                        notes = notes.ifBlank { null }
                    ))
                },
                shape = RoundedCornerShape(12.dp)
            ) { Text("Update") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        shape = RoundedCornerShape(20.dp)
    )
}
