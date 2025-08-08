// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/local/CategoryDao.kt
package com.rahul.auric.fintrack.auricfin.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    // --- FIX: Add the delete function ---
    @Delete
    suspend fun deleteCategory(category: Category)

    // Gets all categories of a specific type (e.g., all "expense" categories)
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name ASC")
    fun getCategoriesByType(type: String): Flow<List<Category>>

    // Gets all categories
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>
}