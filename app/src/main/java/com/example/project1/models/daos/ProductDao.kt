package com.example.project1.models.daos

import androidx.room.*
import com.example.project1.models.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE uid LIKE :id")
    fun findById(id: Int): Product

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)
}