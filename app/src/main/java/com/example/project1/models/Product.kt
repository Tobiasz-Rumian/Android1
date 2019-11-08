package com.example.project1.models

import android.content.ContentValues
import android.database.Cursor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project1.MainActivity


@Entity(tableName = MainActivity.TABLE_NAME)
data class Product(
    @ColumnInfo(name = TITLE) var title: String? = "Untitled",
    @ColumnInfo(name = PRICE) var price: Double = 0.0,
    @ColumnInfo(name = AMOUNT) var amount: Int = 0,
    @ColumnInfo(name = PURCHASED) var purchased: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    companion object {
        fun fromContentValues(values: ContentValues?): Product {
            val product = Product()
            if (values!!.containsKey(UID)) product.uid = values.getAsInteger(UID)
            if (values.containsKey(TITLE)) product.title = values.getAsString(TITLE)
            if (values.containsKey(PRICE)) product.price = values.getAsDouble(PRICE)
            if (values.containsKey(AMOUNT)) product.amount = values.getAsInteger(AMOUNT)
            if (values.containsKey(PURCHASED)) product.purchased =
                values.getAsBoolean(PURCHASED)
            return product
        }
        fun fromCursor(cursor: Cursor?): Product {
            val product = Product()
            if (cursor!!.moveToFirst()) {
                do {
                    product.uid = cursor.getInt(cursor.getColumnIndex(UID))
                    product.title = cursor.getString(cursor.getColumnIndex(TITLE))
                    product.price = cursor.getDouble(cursor.getColumnIndex(PRICE))
                    product.amount = cursor.getInt(cursor.getColumnIndex(AMOUNT))
                    product.purchased = cursor.getInt(cursor.getColumnIndex(PURCHASED))>0
                } while (cursor.moveToNext())
            }
            cursor.close()
            return product
        }
        const val UID = "uid"
        const val TITLE = "title"
        const val PRICE = "price"
        const val AMOUNT = "amount"
        const val PURCHASED = "purchased"
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

