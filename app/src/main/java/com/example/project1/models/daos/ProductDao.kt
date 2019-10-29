package com.example.project1.models.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.project1.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE uid LIKE :id")
    fun findById(id: Int): Product

    @Insert
    fun insert(product: Product)

    @Delete
    fun delete(product: Product)
}