# TrackIt Android 📱

A full-stack **Job & Internship Application Tracker** Android app built with Kotlin and Jetpack Compose. Connected to a live Spring Boot backend deployed on Railway.

## 📸 Features

- 🔐 **Authentication** — Register & Login with JWT
- 📊 **Dashboard** — Live stats (Total, Applied, Shortlisted, Interview, Offers, Rejected)
- ➕ **Add Applications** — Track company, role, job type, and notes
- ✏️ **Edit Status** — Update application status as you progress
- 🗑️ **Delete** — Remove applications you no longer need
- 🌙 **Dark/Light Theme** — Toggle between themes
- 🔒 **Secure** — JWT token stored locally with DataStore

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Programming Language |
| Jetpack Compose | UI Framework |
| Retrofit + OkHttp | API Communication |
| ViewModel + StateFlow | State Management |
| DataStore | Local Token Storage |
| Navigation Compose | Screen Navigation |
| Material 3 | Design System |
| Coroutines | Async Operations |

## 📁 Project Structure

```
app/src/main/java/com/trackit/trackit/
├── model/           # Data classes
├── network/         # Retrofit API setup
├── repository/      # API call handlers
├── ui/
│   ├── screens/     # Login, Register, Dashboard screens
│   ├── theme/       # Colors, Typography, Theme
│   └── Navigation.kt
├── utils/           # Token Manager
├── viewmodel/       # Auth & Application ViewModels
└── MainActivity.kt
```

## 🚀 Running Locally

### Prerequisites
- Android Studio Meerkat or later
- Android SDK 26+
- Java 21

### Steps

1. Clone the repository
```bash
git clone https://github.com/Subhant-112-Tomar/trackit-android.git
```

2. Open in Android Studio

3. The app connects to the live Railway backend by default:
```kotlin
private const val BASE_URL = "https://trackit-backend-production.up.railway.app/"
```

4. To use local backend, change `BASE_URL` in `RetrofitInstance.kt`:
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/"
```

5. Run on emulator or physical device

## 🔌 Backend API

This app connects to the TrackIt Spring Boot backend:
- **Live API:** `https://trackit-backend-production.up.railway.app`
- **Repository:** [trackit-backend](https://github.com/Subhant-112-Tomar/trackit-backend)

## 📊 Application Status Flow

```
APPLIED → SHORTLISTED → INTERVIEW → OFFER
                                  ↘ REJECTED
```

## 👨‍💻 Author

**Subhant Tomar**
- GitHub: [@Subhant-112-Tomar](https://github.com/Subhant-112-Tomar)
- Email: subhanttom002@gmail.com
