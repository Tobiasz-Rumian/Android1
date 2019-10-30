package com.example.project1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.project1.models.Product
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class ProductViewActivity : AppCompatActivity() {
    private val database = MainActivity.database!!
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
                Observable.just(database)
                    .subscribeOn(Schedulers.io())
                    .subscribe { db ->
                        run {
                            product = db.productDao().findById(productId)
                            runOnUiThread {
                                titleTextBox.setText(product.title)
                                priceTextBox.setText(product.price.toString())
                                amountTextBox.setText(product.amount.toString())
                                isPurchasedCheckBox.isChecked = product.purchased
                                deleteButton.visibility = View.VISIBLE
                                Log.d("XXX", product.toString())
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
        val title = titleTextBox.text.takeIf { it.isNotEmpty() }?.toString()?:"Something"
        val price = priceTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toDouble()?:0.0
        val amount = amountTextBox.text.takeIf { it.isNotEmpty() }?.toString()?.toInt()?:0
        val purchased =isPurchasedCheckBox.isChecked
        if (::product.isInitialized) {
            product.title = title
            product.price = price
            product.amount = amount
            product.purchased = purchased
            subscriptions.add(
                Observable.just(database)
                    .subscribeOn(Schedulers.io())
                    .subscribe { db ->
                        run {
                            db.productDao().update(product)
                            finish()
                        }
                    })
        } else {
            product = Product(title, price, amount, purchased)
            subscriptions.add(
                Observable.just(database)
                    .subscribeOn(Schedulers.io())
                    .subscribe { db ->
                        run {
                            db.productDao().insert(product)
                            finish()
                        }
                    })
        }
    }

    fun onDelete(view: View) {
        subscriptions.add(
            Observable.just(database)
                .subscribeOn(Schedulers.io())
                .subscribe { db -> run {
                    db.productDao().delete(product)
                    finish()
                } })
    }
}
