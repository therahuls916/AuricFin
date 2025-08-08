# 💰 AuricFin - Modern Expense Tracker App (Prototype)
> **Internship Project @ CODTECH IT SOLUTIONS**

| Internship Info      | Details                            |
|----------------------|-------------------------------------|
| **Name**             | Rahul Salunke                      |
| **Intern ID**        | CT04DH562                          |
| **Domain**           | Android Development                |
| **Duration**         | 6 Weeks                            |
| **Mentor**           | Neela Santhosh Kumar               |

---
### 📸 Screenshots

| Home Screen | Add Income | Add Expense |
| :---: | :---: | :---: |
| ![Home Screen](https://github.com/user-attachments/assets/bdc3c89d-ec08-480b-8465-5fb0d64cd844) | ![Add Income Screen](https://github.com/user-attachments/assets/494b3e35-17d6-49e0-b387-4fa236175be9) | ![Add Expense Screen](https://github.com/user-attachments/assets/69b9eef3-559b-4b1e-846b-98b5b4c82096) |
| **Transactions List** | **Category Filter** | **Type Filter** |
| ![Transactions List](https://github.com/user-attachments/assets/d5760c5d-e4e1-43ca-b259-36a3d5f9469c) | ![Category Filter](https://github.com/user-attachments/assets/fd718efd-79fc-4a54-a4da-701128420441) | ![Type Filter](https://github.com/user-attachments/assets/5be82152-7ecc-4904-907a-d11e6e7b6a8d) |
| **Reports Screen** | **Budget Screen** | **Settings Screen** |
| ![Reports Screen](https://github.com/user-attachments/assets/c76dd8a5-703e-4ee0-851f-c4e2f7c7c578) | ![Budget Screen](https://github.com/user-attachments/assets/78914f6e-d315-4a2d-baa6-f62a25308d69) | ![Settings Screen](https://github.com/user-attachments/assets/ef75b7f3-c15c-4248-93fc-f706561d3e46) |
| **Manage Expenses** | **Manage Income** | **Add New Category** |
| ![Manage Expense Categories](https://github.com/user-attachments/assets/1da5d633-6cc2-462d-9611-77b1abb4d66d) | ![Manage Income Categories](https://github.com/user-attachments/assets/2bb7812d-4448-46ad-8284-f7f75e240e67) | ![Add New Category Dialog](https://github.com/user-attachments/assets/e200124f-7634-40db-b679-9e8be79cc44c) |

AuricFin is a modern Android app built with Kotlin and Jetpack Compose that allows users to track their daily expenses and income, categorize them, and visualize spending habits through interactive charts.

---

## 🚀 Features

| Feature | Description |
|--------|-------------|
| 💳 **Transaction C.R.U.D.** | Full Create, Read, Update, and Delete functionality for all transactions. |
| 📊 **Interactive Dashboard** | Home screen with overview cards for balance/expenses and a spending pie chart. |
| 📈 **Advanced Reports** | Dedicated reports screen with a line chart for spending trends and a bar chart for category breakdowns. |
| ⚙️ **Advanced Filtering & Sorting** | Filter transactions by type (Income/Expense) and category, and sort by date. |
| 🏷️ **Category Management** | Users can add and delete their own custom income or expense categories. |
| 💾 **Data Persistence** | All data is saved locally and persists between app launches using a Room database. |
| 📱 **Modern UI/UX** | Built entirely with Jetpack Compose and Material 3, including a modal bottom sheet for quick actions. |

---

## 🛠 Tech Stack

- **Kotlin** (Primary Language)
- **Jetpack Compose** (Modern UI Toolkit)
- **MVVM Architecture**
- **Kotlin Coroutines & Flow**
- **Hilt** (Dependency Injection)
- **Room Database** (Local Persistence)
- **Jetpack Navigation Compose**
- **MPAndroidChart** (For Charts)
- **Material 3 Components**

---
## 🔧 Installation
```bash
git clone https://github.com/therahuls916/AuricFin.git
cd AuricFin```
> Open the project in Android Studio, let Gradle sync, and click ▶️ Run.

---

## 📂 Folder Structure

```plaintext
app/src/main/java/com/rahul/auric/fintrack/auricfin/
├── AuricFinApplication.kt
├── MainActivity.kt
├── data/
│   ├── AppModule.kt
│   ├── CategoryRepository.kt
│   ├── TransactionRepository.kt
│   └── local/
│       ├── AppDatabase.kt
│       ├── Category.kt
│       ├── CategoryDao.kt
│       ├── Transaction.kt
│       └── TransactionDao.kt
└── ui/
    ├── Navigation.kt
    ├── components/
    │   ├── AppBottomNavigationBar.kt
    │   └── CategoryIcon.kt
    ├── screens/
    │   ├── add_transaction/
    │   │   ├── AddTransactionScreen.kt
    │   │   └── AddTransactionViewModel.kt
    │   ├── budget/
    │   │   └── ... (Screen & ViewModel)
    │   ├── edit_transaction/
    │   │   └── ... (Screen & ViewModel)
    │   ├── home/
    │   │   └── ... (Screen & ViewModel)
    │   ├── reports/
    │   │   └── ... (Screen & ViewModel)
    │   ├── settings/
    │   │   └── ... (Screen & ViewModel)
    │   └── transactions/
    │       └── ... (Screen & ViewModel)
    └── theme/
        └── ... (Color.kt, Theme.kt, Type.kt)
```

---

## 🔐 Permissions Used
This application is fully self-contained and operates offline. It **does not require any special permissions** such as Internet, Location, or Storage access, ensuring user privacy.

---

## 🧠 How It Works
* **UI Layer:** The entire UI is built with Jetpack Compose. Screens are stateless Composables that receive data from their ViewModels.
* **State Management:** `StateFlow` is used to expose data from ViewModels to the UI. The UI observes these flows and automatically recomposes when data changes.
* **Data Layer:** The Repository pattern is used to abstract the Room database. ViewModels talk to Repositories, not directly to the database.
* **Reactive Filtering:** On the Transactions screen, a `combine` Flow operator listens to multiple data streams. It automatically re-calculates the final list whenever any filter changes, making the UI incredibly responsive.
* **Database:** Room is used as a local SQLite database to store all transaction and category data.

---

## ✅ Planned Features

* [ ] 💰 Allow users to set monthly spending limits for specific categories.
* [ ] 📅 Activate the weekly and yearly filters for all reports.
* [ ] 🔍 Implement the search feature on the Transactions screen.
* [ ] 📤 Add functionality to export transaction data to a CSV file.

---

## 🤝 Contributing
Want to help? Fork this repo, create a new branch, and open a PR with improvements or features.

---

## 📄 License
This project is licensed under the MIT License - see the `LICENSE` file for details.

---

## 🙌 Acknowledgements
* [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) by Phil Jay
* [Material Design Components](https://m3.material.io/) by Google

---

## 👨‍💻 Developer
**Rahul Salunke**
[GitHub](https://github.com/therahuls916) | [LinkedIn](https://www.linkedin.com/in/rahulasalunke/)