package com.example.project1.models

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project1.MainActivity


@Entity(tableName = MainActivity.TABLE_NAME)
data class Product(
    @ColumnInfo(name = "title") var title: String? = "Untitled",
    @ColumnInfo(name = "price") var price: Double = 0.0,
    @ColumnInfo(name = "amount") var amount: Int = 0,
    @ColumnInfo(name = "purchased") var purchased: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    companion object {
        fun fromContentValues(values: ContentValues?): Product {
            val product = Product()
            if (values!!.containsKey("title")) product.title = values.getAsString("title")
            if (values.containsKey("price")) product.price = values.getAsDouble("price")
            if (values.containsKey("amount")) product.amount = values.getAsInteger("amount")
            if (values.containsKey("purchased")) product.purchased =
                values.getAsBoolean("purchased")
            return product
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("uid: ").append(uid).append("\n")
            .append("title: ").append(title).append("\n")
            .append("price: ").append(price).append("\n")
            .append("amount: ").append(amount).append("\n")
            .append("purchased: ").append(purchased).append("\n")
        return sb.toString()
    }


}

