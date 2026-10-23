# Personal Expense Tracker App - Complete Process & Setup Guide 💸

A comprehensive, end-to-end, step-by-step process for building, configuring, and running the Personal Expense Tracker App using Kotlin, MVVM Architecture, Room Database, and Material Design components.

---

## 📋 Comprehensive Step-by-Step Build Process

### Step 1: Create and Configure Project in Android Studio
- Open **Android Studio** and click **New Project**.
- Select **Empty Views Activity** and name your project `PersonalExpenseTrackerApp`.
- Set Language to **Kotlin**, Minimum SDK to **API 24 (Android 7.0)**, and Target SDK to **SDK 35**.
- Ensure the package name matches your project namespace (e.g., `com.example.personalexpensetrackerapp`).

---

### Step 2: Configure Gradle Dependencies and Plugins
- Open `build.gradle.kts` (Module :app) and ensure required plugins are applied (`kotlin-kapt` or `ksp`).
- Add **Room Database** dependencies:
  - `androidx.room:room-runtime:2.6.1`
  - `androidx.room:room-ktx:2.6.1`
  - `kapt("androidx.room:room-compiler:2.6.1")`
- Add **Lifecycle & ViewModel** dependencies:
  - `androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6`
  - `androidx.lifecycle:lifecycle-livedata-ktx:2.8.6`
- Add **Material Design & Navigation**:
  - `com.google.android.material:material:1.12.0`
- Click **Sync Now** to download and sync all dependencies.

---

### Step 3: Configure Android Manifest (`AndroidManifest.xml`)
- Verify that package attributes are clean and aligned with modern Android namespaces.
- Ensure `MainActivity` is registered with the launcher intent filter:
  - `android.intent.action.MAIN`
  - `android.intent.category.LAUNCHER`
- Declare `AddExpenseActivity` as an activity in the application block.

---

### Step 4: Build Room Database Layer
1. **Entity (`Expense.kt`)**:
   - Annotate with `@Entity(tableName = "expense_table")`.
   - Define primary key: `@PrimaryKey(autoGenerate = true) val id: Int = 0`.
   - Define columns: `val title: String`, `val amount: Double`, `val category: String`.
2. **Data Access Object (`ExpenseDao.kt`)**:
   - Annotate interface with `@Dao`.
   - Add insert function: `@Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insert(expense: Expense)`.
   - Add query function: `@Query("SELECT * FROM expense_table ORDER BY id DESC") fun getAllExpenses(): LiveData<List<Expense>>`.
3. **Database Class (`ExpenseDatabase.kt`)**:
   - Annotate class with `@Database(entities = [Expense::class], version = 1, exportSchema = false)`.
   - Implement Singleton pattern using `@Volatile` instance and `synchronized` block to manage single database connection.

---

### Step 5: Implement Repository and ViewModel
1. **Repository (`ExpenseRepository.kt`)**:
   - Pass `ExpenseDao` into constructor.
   - Expose `val allExpenses: LiveData<List<Expense>> = expenseDao.getAllExpenses()`.
   - Add `suspend fun insert(expense: Expense)` calling `expenseDao.insert(expense)`.
2. **ViewModel (`ExpenseViewModel.kt`)**:
   - Extend `AndroidViewModel(application)`.
   - Instantiate `ExpenseDatabase` and obtain `ExpenseDao`.
   - Instantiate `ExpenseRepository`.
   - Expose `val allExpenses: LiveData<List<Expense>>`.
   - Add `fun insert(expense: Expense)` executing repository insertion inside `viewModelScope.launch(Dispatchers.IO)`.

---

### Step 6: Design UI Layout XML Files
1. **Item Expense Layout (`res/layout/item_expense.xml`)**:
   - Create a `MaterialCardView` containing a `LinearLayout` or `ConstraintLayout`.
   - Add `TextView`s for Expense Title, Amount (`$0.00`), and Category.
2. **Main Layout (`res/layout/activity_main.xml`)**:
   - Use `CoordinatorLayout` as the root view.
   - Add a `MaterialCardView` displaying Total Expenses summary (`tvTotalAmount`).
   - Add `RecyclerView` (`rvExpenses`) configured with `android:layout_height="0dp"`, `android:layout_weight="1"`, `android:clipToPadding="false"`, and `android:paddingBottom="80dp"`.
   - Add a `FloatingActionButton` (`fabAddExpense`) aligned to `bottom|end`.
3. **Add Expense Layout (`res/layout/activity_add_expense.xml`)**:
   - Use `ScrollView` as root with `android:fillViewport="true"`.
   - Add vertical `LinearLayout` with `android:gravity="center_vertical"`.
   - Add `TextInputLayout` (OutlinedBox) and `TextInputEditText` for:
     - Title (`etTitle`)
     - Amount (`etAmount`)
     - Category (`etCategory`)
   - Add "Save Expense" `Button` (`btnSaveExpense`).

---

### Step 7: Create RecyclerView Adapter (`ExpenseAdapter.kt`)
- Extend `RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>()`.
- Implement `ExpenseViewHolder` to bind item views (`tvTitle`, `tvAmount`, `tvCategory`).
- Add `setData(expenses: List<Expense>)` method to update local list and trigger `notifyDataSetChanged()`.
- Implement `onCreateViewHolder`, `onBindViewHolder`, and `getItemCount`.

---

### Step 8: Implement Activity Logic
1. **Main Screen (`MainActivity.kt`)**:
   - Initialize `RecyclerView` with `LinearLayoutManager` and attach `ExpenseAdapter`.
   - Obtain `ExpenseViewModel` via `ViewModelProvider`.
   - Observe `expenseViewModel.allExpenses`:
     - Pass expense list to `expenseAdapter.setData(it)`.
     - Dynamically sum amounts (`it.sumOf { expense -> expense.amount }`) and display formatted result in `tvTotalAmount`.
   - Set click listener on `fabAddExpense` to launch `AddExpenseActivity`.
2. **Add Expense Screen (`AddExpenseActivity.kt`)**:
   - Bind input fields (`etTitle`, `etAmount`, `etCategory`) and save button (`btnSaveExpense`).
   - Obtain `ExpenseViewModel` instance.
   - On button click:
     - Extract text and validate fields are non-empty.
     - Parse amount string to `Double`.
     - Instantiate new `Expense` object.
     - Call `expenseViewModel.insert(expense)`.
     - Show confirmation Toast and call `finish()` to return to `MainActivity`.

---

### Step 9: Build and Run
- Go to **Build > Clean Project**, then **Build > Rebuild Project** in Android Studio.
- Ensure no compilation or unresolved reference errors exist.
- Click the green **Run ' ▶ '** button to launch the app on an emulator or physical Android device.
- Add expenses via the FAB button and verify live update of the recent expenses list and total expense sum.
