package com.example.project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.project1.adapters.ProductArrayAdapter
import com.example.project1.databinding.ActivityProductListBinding
import com.example.project1.models.Product
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ProductListActivity : AppCompatActivity() {

    private var products = ArrayList<Product>()
    private lateinit var adapter: ProductArrayAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProductListBinding
    private val database = MainActivity.database!!
    private val subscriptions = ArrayList<Disposable>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_list)
        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName,
            Context.MODE_PRIVATE
        )
        adapter = ProductArrayAdapter(this, R.layout.product_row, products)
        binding.listOfProducts.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        binding.addProductButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        binding.addProductButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
    }

    override fun onResume() {
        super.onResume()
        subscriptions.add(Observable.just(database)
            .subscribeOn(Schedulers.io())
            .subscribe { db ->
                run {
                    if (db.productDao().getAll().isEmpty()) {
                        db.productDao().insert(Product("Piano", 200.0, 20))
                        db.productDao().insert(Product("Bread", 2.5, 5))
                        db.productDao().insert(Product("Phone", 150.50, 1))
                    }
                    products.clear()
                    products.addAll(db.productDao().getAll())
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach(Disposable::dispose)
    }

    fun onAddProduct(view: View) {
        startActivity(Intent(this, ProductViewActivity::class.java))
    }

}
