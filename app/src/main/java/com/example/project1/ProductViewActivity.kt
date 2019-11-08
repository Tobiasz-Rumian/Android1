package com.example.project1

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.models.Product
import com.example.project1.provider.MainContentProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ProductViewActivity : AppCompatActivity() {
    private val subscriptions = ArrayList<Disposable>()
    private lateinit var product: Product
    private lateinit var titleTextBox: EditText
    private lateinit var priceTextBox: EditText
    private lateinit var amountTextBox: EditText
    private lateinit var isPurchasedCheckBox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)
        titleTextBox = findViewById(R.id.titleTextBox)
        priceTextBox = findViewById(R.id.priceTextBox)
        amountTextBox = findViewById(R.id.amountTextBox)
        isPurchasedCheckBox = findViewById(R.id.isPurchasedCheckBox)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
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
                                titleTextBox.setText(product.title)
                                priceTextBox.setText(product.price.toString())
                                amountTextBox.setText(product.amount.toString())
                                isPurchasedCheckBox.isChecked = product.purchased
                                deleteButton.visibility = View.VISIBLE
                            }
                        }
                    })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.forEach(Disposable::dispose)
    }

    fun onSave(view: View) {
        val title = titleTextBox.text.takeIf { it.isNotEmpty() }?.toString() ?: "Something"
        val price = priceTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toDouble() ?: 0.0
        val amount = amountTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toInt() ?: 0
        val purchased = isPurchasedCheckBox.isChecked
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
                            cr.insert(MainContentProvider.CONTENT_URI, values)
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
