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
import java.math.BigDecimal
import kotlin.random.Random
import android.widget.Toast
import android.app.Activity
import android.os.AsyncTask
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.lang.ref.WeakReference


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

        database =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "products").build()
        products = database.productDao().getAll() as ArrayList<Product>


        database.productDao().insert(Product(1, "Piano", 200.0, 20))
        database.productDao().insert(Product(2, "Bread", 2.5, 5))
        database.productDao().insert(Product(3, "Phone", 150.50, 1))
//        products.add(Product("Piano", BigDecimal.valueOf(200), 20))
//        products.add(Product("Bread", BigDecimal.valueOf(2.5), 5))
//        products.add(Product("Phone", BigDecimal.valueOf(150.50), 1))

        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName,
            Context.MODE_PRIVATE
        )

        adapter = ProductArrayAdapter(this, R.layout.product_row, products)
        listView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        addButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FCFCFC")
            )
        )
        addButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#FFFFFF")
            )
        )
        deleteButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FCFCFC")
            )
        )
        deleteButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#FFFFFF")
            )
        )
    }

    fun onAddProduct(view: View) {
        products.add(
            Product(
                products.size,
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

    private class AgentAsyncTask(
        activity: Activity,
        private val email: String,
        private val phone: String,
        private val license: String
    ) : AsyncTask<Void, Void, Int>() {

        //Prevent leak
        private val weakActivity: WeakReference<Activity> = WeakReference(activity)

        override fun doInBackground(vararg params: Void): Int? {
            val agentDao = MyApp.DatabaseSetup.getDatabase().agentDao()
            return agentDao.agentsCount(email, phone, license)
        }

        override fun onPostExecute(agentsCount: Int?) {
            val activity = weakActivity.get() ?: return

            if (agentsCount > 0) {
                //2: If it already exists then prompt user
                Toast.makeText(activity, "Agent already exists!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(activity, "Agent does not exist! Hurray :)", Toast.LENGTH_LONG)
                    .show()
                activity.onBackPressed()
            }
        }
    }

}
