package com.example.project1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.project1.adapters.ProductArrayAdapter
import com.example.project1.database.AppDatabase
import com.example.project1.models.Product
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlin.random.Random
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class ProductListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private var products = ArrayList<Product>()
    private lateinit var adapter: ProductArrayAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        listView = findViewById(R.id.listOfProducts)
        addButton = findViewById(R.id.addProductButton)
        deleteButton = findViewById(R.id.removeProductButton)

        database = MainActivity.database!!

        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName,
            Context.MODE_PRIVATE
        )

        adapter = ProductArrayAdapter(this, R.layout.product_row, products)
        listView.adapter = adapter

        val populate = Observable.just(database)
            .subscribeOn(Schedulers.io())
            .subscribe { db ->
                run {
                    if(db.productDao().getAll().isEmpty()){
                    db.productDao().insert(Product("Piano", 200.0, 20))
                    db.productDao().insert(Product("Bread", 2.5, 5))
                    db.productDao().insert(Product("Phone", 150.50, 1))
                    }
                    products.addAll(db.productDao().getAll())
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }

                }
            }
    }

    override fun onStart() {
        super.onStart()
        addButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        addButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
        deleteButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        deleteButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun onAddProduct(view: View) {
        products.add(
            Product(
                "New Product nr." + products.size,
                Random.nextDouble(0.0, 1000.0),
                Random.nextInt(0, 100)
            )
        )
        adapter.notifyDataSetChanged()
    }

    fun onRemoveProduct(view: View) {
        products.removeAt(products.size - 1)
        adapter.notifyDataSetChanged()
    }

}
