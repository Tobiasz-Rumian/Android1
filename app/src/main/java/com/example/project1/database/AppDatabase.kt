package com.example.project1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project1.models.Product
import com.example.project1.models.daos.ProductDao

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}