// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/data/local/AppDatabase.kt
package com.rahul.auric.fintrack.auricfin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.util.Date

// 1. Add Category::class to the entities array
@Database(entities = [Transaction::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Abstract function for the TransactionDao (no change here)
    abstract fun transactionDao(): TransactionDao

    // 2. Add the new abstract function for the CategoryDao
    abstract fun categoryDao(): CategoryDao

}

// The Converters class remains unchanged
class Converters {
    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}