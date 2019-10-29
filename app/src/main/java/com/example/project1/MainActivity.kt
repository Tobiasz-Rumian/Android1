package com.example.project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var goToProductListButton: Button
    private lateinit var goToOptionsButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = applicationContext.getSharedPreferences(applicationContext.packageName,
            Context.MODE_PRIVATE)
        goToProductListButton = findViewById(R.id.goToProductListButton)
        goToOptionsButton = findViewById(R.id.goToOptionsButton)

    }

    override fun onStart() {
        super.onStart()
        goToProductListButton.setBackgroundColor(sharedPreferences.getInt("buttonBackgroundColor", Color.parseColor("#FCFCFC")))
        goToProductListButton.setTextColor(sharedPreferences.getInt("buttonTextColor", Color.parseColor("#FFFFFF")))
        goToOptionsButton.setBackgroundColor(sharedPreferences.getInt("buttonBackgroundColor", Color.parseColor("#FCFCFC")))
        goToOptionsButton.setTextColor(sharedPreferences.getInt("buttonTextColor", Color.parseColor("#FFFFFF")))
    }
    fun goToProductList(view: View){
        startActivity(Intent(this,ProductListActivity::class.java))
    }
    fun goToOptions(view: View){
        startActivity(Intent(this,OptionsActivity::class.java))
    }
}
