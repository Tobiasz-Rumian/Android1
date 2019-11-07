package com.example.project1.models.daos

import android.database.Cursor
import androidx.room.*
import com.example.project1.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAll(): List<Product>

    @Query("SELECT * FROM products WHERE uid LIKE :id")
    fun findById(id: Int): Product

    @Insert
    fun insert(product: Product): Long

    @Query("SELECT * FROM Products WHERE uid = :id")
    fun getItemsWithCursor(id: Long): Cursor

    @Update
    fun update(product: Product): Int

    @Delete
    fun delete(product: Product): Int

    @Query("DELETE FROM products WHERE uid = :id")
    fun deleteById(id: Long): Int
}