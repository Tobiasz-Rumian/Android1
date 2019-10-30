package com.example.project1.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @ColumnInfo(name = "title") var title: String? = "Untitled",
    @ColumnInfo(name = "price") var price: Double = 0.0,
    @ColumnInfo(name = "amount") var amount: Int = 0
){
    @PrimaryKey(autoGenerate = true) var uid: Int =0
    @ColumnInfo(name = "purchased") var purchased: Boolean = false
}

