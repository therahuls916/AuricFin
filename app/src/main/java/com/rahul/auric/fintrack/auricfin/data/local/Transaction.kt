// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/local/Transaction.kt
package com.rahul.auric.fintrack.auricfin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: String, // "income" or "expense"
    val category: String, // For now, we store the category name directly
    val date: Date,
    val note: String?
)