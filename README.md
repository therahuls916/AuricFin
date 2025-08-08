# AuricFin - Modern Expense Tracker App

AuricFin is a fully-featured expense tracker application for Android, built using the latest in modern Android development technology. It allows users to effortlessly track their income and expenses, categorize transactions, and gain insights into their spending habits through interactive charts and summaries.

---

### 📸 Screenshots & Demo

*(This is the most important part of your README! Take screenshots or record a short GIF of your app running on an emulator or device. Upload them to your GitHub repository and then replace the placeholder text below with the images.)*

**How to add images:**
1.  In your GitHub repository, click "Add file" -> "Upload files".
2.  Upload your screenshot files.
3.  Click on an uploaded image file to get its URL.
4.  Replace the `(Your Screenshot)` text below with `![Description of Image](URL_to_your_image.png)`.

| Home Screen | Transactions (with Filters) | Reports Screen |
| :---: | :---: | :---: |
| *(Your Screenshot Here)* | *(Your Screenshot Here)* | *(Your Screenshot Here)* |
| **Add/Edit Transaction** | **Spending Breakdown** | **Manage Categories** |
| *(Your Screenshot Here)* | *(Your Screenshot Here)* | *(Your Screenshot Here)* |

---

### ✨ Features

*   **C.R.U.D. Transactions:** Easily add, edit, and delete income and expense transactions.
*   **Advanced Filtering & Sorting:** The main transaction list can be dynamically filtered by type (income/expense) and category, and sorted by date.
*   **Category Management:** Assign categories to transactions for better organization. Comes with a default set of categories and allows users to add or delete their own.
*   **Data Persistence:** All data is saved locally on the device using a robust Room database.
*   **Interactive Dashboard:** The home screen provides an at-a-glance overview of total balance, monthly expenses, and recent activity.
*   **Rich Data Visualization:**
    *   A pie chart on the home screen shows spending distribution by category.
    *   A dedicated reports screen features a line chart for spending trends and a bar chart for category-wise breakdowns.
*   **Modern UI:** A clean, intuitive user interface built entirely with Jetpack Compose and Material 3 design principles, including a modal bottom sheet for edit/delete actions.

---

### 🛠️ Tech Stack & Architecture

This project is a showcase of modern Android architecture and best practices.

*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://developer.android.com/kotlin/flow) for reactive data streams. The `combine` operator is used for efficiently creating the filtered transaction list.
*   **Database:** [Room](https.developer.android.com/training/data-storage/room) for robust, local data persistence.
*   **Dependency Injection:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) for managing dependencies throughout the app.
*   **Navigation:** [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for navigating between screens.
*   **Charting:** [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) (wrapped in a theme-aware, Compose-compatible component).

---

### 🚀 Setup and Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/your-username/AuricFin.git
    ```
2.  Open the project in Android Studio (latest stable version recommended).
3.  Let Gradle sync the dependencies.
4.  Build and run the app on an emulator or a physical device.

---

### 💡 Future Improvements

*   **Custom Budgets:** Allow users to set monthly spending limits for specific categories.
*   **Enhanced Reporting:** Activate the weekly and yearly filters for all reports.
*   **Search Functionality:** Implement the search feature on the Transactions screen.
*   **Data Export:** Add functionality to export transaction data to a CSV file.