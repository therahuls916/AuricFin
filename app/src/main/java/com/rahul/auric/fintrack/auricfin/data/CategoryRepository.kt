// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/CategoryRepository.kt
package com.rahul.auric.fintrack.auricfin.data

import com.rahul.auric.fintrack.auricfin.data.local.Category
import com.rahul.auric.fintrack.auricfin.data.local.CategoryDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// --- FIX: Renaming the class back to its original name ---
@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    fun getCategoriesByType(type: String): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type)
    }

    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}