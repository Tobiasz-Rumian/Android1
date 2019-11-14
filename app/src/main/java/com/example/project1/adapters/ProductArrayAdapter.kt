package com.example.project1.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.project1.MainActivity
import com.example.project1.ProductViewActivity
import com.example.project1.R
import com.example.project1.models.Product
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ProductArrayAdapter(context: Context, resource: Int, objects: ArrayList<out Product>) :
    ArrayAdapter<Product>(context, resource, objects) {
    private val products: List<Product> = objects
    private val database = MainActivity.database!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val product = products[position]
        val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.product_row, null)
        val title = view.findViewById(R.id.title) as TextView
        val price = view.findViewById(R.id.price) as TextView
        val amount = view.findViewById(R.id.amount) as TextView
        val isPurchased = view.findViewById(R.id.isPurchased) as CheckBox
        title.text = product.title
        price.text = product.price.toString() + " Zł"
        amount.text = product.amount.toString()
        isPurchased.setOnCheckedChangeListener { _, isChecked ->
            val updatePurchaseState = Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db ->
                    run {
                        val productToUpdate = db.productDao().findById(product.uid)
                        productToUpdate.purchased = isChecked
                        db.productDao().update(productToUpdate)
                    }
                }
        }
        view.setOnClickListener {
            val intent = Intent(context, ProductViewActivity::class.java)
            intent.putExtra("productId", product.uid)
            context.startActivity(intent)
        }
        isPurchased.isChecked = product.purchased
        return view
    }
}