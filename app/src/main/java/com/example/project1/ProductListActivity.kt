package com.example.project1

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.adapters.ProductArrayAdapter
import com.example.project1.models.Product
import java.math.BigDecimal
import kotlin.random.Random


class ProductListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var products = ArrayList<Product>()
    private lateinit var adapter: ProductArrayAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        products.add(Product("Piano", BigDecimal.valueOf(200), 20))
        products.add(Product("Bread", BigDecimal.valueOf(2.5), 5))
        products.add(Product("Phone", BigDecimal.valueOf(150.50), 1))
        listView = findViewById(R.id.listOfProducts)
        adapter = ProductArrayAdapter(this, R.layout.product_row, products)
        listView.adapter = adapter
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
