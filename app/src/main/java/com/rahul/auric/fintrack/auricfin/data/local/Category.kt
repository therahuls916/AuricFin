// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/local/Category.kt
package com.rahul.auric.fintrack.auricfin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String // "expense" or "income"
)