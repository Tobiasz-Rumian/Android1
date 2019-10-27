package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun goToProductList(view: View){
        startActivity(Intent(this,ProductListActivity::class.java))
    }
    fun goToOptions(view: View){
        startActivity(Intent(this,OptionsActivity::class.java))
    }
}
