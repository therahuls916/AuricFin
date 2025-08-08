// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/screens/settings/ManageCategoriesViewModel.kt
package com.rahul.auric.fintrack.auricfin.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.auric.fintrack.auricfin.data.CategoryRepository // <-- FIX: Import original name
import com.rahul.auric.fintrack.auricfin.data.local.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageCategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository // <-- FIX: Use original name
) : ViewModel() {

    val expenseCategories: StateFlow<List<Category>> =
        categoryRepository.getCategoriesByType("expense")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val incomeCategories: StateFlow<List<Category>> =
        categoryRepository.getCategoriesByType("income")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addCategory(name: String, type: String) {
        viewModelScope.launch {
            val newCategory = Category(name = name, type = type)
            categoryRepository.insertCategory(newCategory)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }
}