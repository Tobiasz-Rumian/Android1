package com.example.project1.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.project1.MainActivity
import com.example.project1.models.Product


class MainContentProvider : ContentProvider() {
    companion object {
        val AUTHORITY = "com.example.project1.provider.MainContentProvider"
        private val PRODUCTS_TABLE = "products"
        val CONTENT_URI: Uri = Uri.parse(
            "content://" + AUTHORITY + "/" +
                    PRODUCTS_TABLE
        )
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (context != null) {
            val userId = ContentUris.parseId(uri)
            val cursor =
                MainActivity.database?.productDao()?.getItemsWithCursor(userId)
            cursor?.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (context != null) {
            val id = MainActivity.database?.productDao()?.insert(Product.fromContentValues(values))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id!!)
            }
        }

        throw  IllegalArgumentException("Failed to insert row into " + uri)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<out String>?
    ): Int {
        if (context != null) {
            val count = MainActivity.database?.productDao()
                ?.update(Product.fromContentValues(contentValues))
            context!!.contentResolver.notifyChange(uri, null)
            return count!!
        }
        throw IllegalArgumentException("Failed to update row into " + uri)
    }

    override fun delete(uri: Uri, s: String?, strings: Array<out String>?): Int {
        if (context != null) {
            val count = MainActivity.database?.productDao()?.deleteById(ContentUris.parseId(uri))
            context!!.contentResolver.notifyChange(uri, null)
            return count!!
        }
        throw IllegalArgumentException("Failed to delete row into " + uri)
    }

    override fun getType(p0: Uri): String? {
        return CONTENT_URI.toString()
    }
}