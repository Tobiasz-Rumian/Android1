package com.example.project1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.adapters.ProductArrayAdapter
import com.example.project1.models.Product
import java.math.BigDecimal
import kotlin.random.Random


class ProductListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var addButton: Button
    private lateinit var deleteButton: Button
    private var products = ArrayList<Product>()
    private lateinit var adapter: ProductArrayAdapter
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        products.add(Product("Piano", BigDecimal.valueOf(200), 20))
        products.add(Product("Bread", BigDecimal.valueOf(2.5), 5))
        products.add(Product("Phone", BigDecimal.valueOf(150.50), 1))
        listView = findViewById(R.id.listOfProducts)
        addButton = findViewById(R.id.addProductButton)
        deleteButton = findViewById(R.id.removeProductButton)
        adapter = ProductArrayAdapter(this, R.layout.product_row, products)
        listView.adapter = adapter
        sharedPreferences = applicationContext.getSharedPreferences(applicationContext.packageName,
            Context.MODE_PRIVATE)

    }

    override fun onStart() {
        super.onStart()
        addButton.setBackgroundColor(sharedPreferences.getInt("buttonBackgroundColor", Color.parseColor("#FCFCFC")))
        addButton.setTextColor(sharedPreferences.getInt("buttonTextColor", Color.parseColor("#FFFFFF")))
        deleteButton.setBackgroundColor(sharedPreferences.getInt("buttonBackgroundColor", Color.parseColor("#FCFCFC")))
        deleteButton.setTextColor(sharedPreferences.getInt("buttonTextColor", Color.parseColor("#FFFFFF")))
    }

    fun onAddProduct(view: View) {
        products.add(
            Product(
                "New Product nr." + products.size,
                BigDecimal.valueOf(Random.nextLong(0,1000)),
                Random.nextInt(0,100)
            )
        )
        adapter.notifyDataSetChanged()
    }

    fun onRemoveProduct(view: View) {
        products.removeAt(products.size - 1)
        adapter.notifyDataSetChanged()
    }
}
