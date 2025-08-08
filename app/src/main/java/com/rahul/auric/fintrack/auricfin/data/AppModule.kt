// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/AppModule.kt
package com.rahul.auric.fintrack.auricfin.data

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rahul.auric.fintrack.auricfin.data.local.AppDatabase
import com.rahul.auric.fintrack.auricfin.data.local.Category
import com.rahul.auric.fintrack.auricfin.data.local.CategoryDao
import com.rahul.auric.fintrack.auricfin.data.local.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // --- FIX: Revert the provider function back to its original name ---
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepository(transactionDao)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        app: Application,
        categoryDaoProvider: Provider<CategoryDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "fintrack_db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val categoryDao = categoryDaoProvider.get()
                        // Insert default expense categories
                        categoryDao.insertCategory(Category(name = "Food", type = "expense"))
                        categoryDao.insertCategory(Category(name = "Transport", type = "expense"))
                        categoryDao.insertCategory(Category(name = "Shopping", type = "expense"))
                        categoryDao.insertCategory(
                            Category(
                                name = "Entertainment",
                                type = "expense"
                            )
                        )
                        categoryDao.insertCategory(Category(name = "Utilities", type = "expense"))
                        categoryDao.insertCategory(Category(name = "Rent", type = "expense"))

                        // Insert default income categories
                        categoryDao.insertCategory(Category(name = "Salary", type = "income"))
                        categoryDao.insertCategory(Category(name = "Freelance", type = "income"))
                        categoryDao.insertCategory(Category(name = "Investment", type = "income"))
                    }
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }
}