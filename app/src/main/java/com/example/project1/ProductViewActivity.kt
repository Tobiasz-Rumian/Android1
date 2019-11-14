package com.example.project1

import android.content.*
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.project1.databinding.ActivityProductViewBinding
import com.example.project1.models.Product
import com.example.project1.provider.MainContentProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ProductViewActivity : AppCompatActivity() {
    private val subscriptions = ArrayList<Disposable>()
    private lateinit var product: Product
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProductViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_view)
        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName,
            Context.MODE_PRIVATE
        )
    }

    override fun onStart() {
        super.onStart()
        val productId = intent.getIntExtra("productId", -1)
        if (productId > 0) {
            subscriptions.add(
                Observable.just(contentResolver)
                    .subscribeOn(Schedulers.io())
                    .subscribe { cr ->
                        run {
                            val cursor = cr.query(
                                Uri.parse(MainContentProvider.CONTENT_URI.toString() + "/" + productId),
                                null,
                                null,
                                null,
                                null
                            )
                            product = Product.fromCursor(cursor)
                            runOnUiThread {
                                binding.titleTextBox.setText(product.title)
                                binding.priceTextBox.setText(product.price.toString())
                                binding.amountTextBox.setText(product.amount.toString())
                                binding.isPurchasedCheckBox.isChecked = product.purchased
                                binding.deleteButton.visibility = View.VISIBLE
                            }
                        }
                    })
        }

        binding.saveButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        binding.saveButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
        binding.deleteButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        binding.deleteButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach(Disposable::dispose)
    }

    fun onSave(view: View) {
        val title = binding.titleTextBox.text.takeIf { it.isNotEmpty() }?.toString() ?: "Something"
        val price =
            binding.priceTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toDouble() ?: 0.0
        val amount = binding.amountTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toInt() ?: 0
        val purchased = binding.isPurchasedCheckBox.isChecked
        val values = ContentValues()
        values.put(Product.TITLE, title)
        values.put(Product.PRICE, price)
        values.put(Product.AMOUNT, amount)
        values.put(Product.PURCHASED, purchased)
        subscriptions.add(
            Observable.just(contentResolver)
                .subscribeOn(Schedulers.io())
                .subscribe { cr ->
                    run {
                        if (::product.isInitialized) {
                            values.put(Product.UID, product.uid)
                            cr.update(MainContentProvider.CONTENT_URI, values, null, null)
                        } else {
                            val id = ContentUris.parseId(
                                cr.insert(
                                    MainContentProvider.CONTENT_URI,
                                    values
                                )!!
                            )
                            val sendIntent: Intent = Intent().apply {
                                action = "com.example.android2.MyReceiver"
                                component = ComponentName(
                                    "com.example.android2",
                                    "com.example.android2.MyReceiver"
                                )
                                putExtra("id", id.toInt())
                                putExtra("title", title)
                            }
                            sendBroadcast(sendIntent)
                        }
                        finish()
                    }
                })
    }

    fun onDelete(view: View) {
        subscriptions.add(
            Observable.just(contentResolver)
                .subscribeOn(Schedulers.io())
                .subscribe { cr ->
                    run {
                        cr.delete(
                            Uri.parse(MainContentProvider.CONTENT_URI.toString() + "/" + product.uid),
                            null,
                            null
                        )
                        finish()
                    }
                })
    }
}
