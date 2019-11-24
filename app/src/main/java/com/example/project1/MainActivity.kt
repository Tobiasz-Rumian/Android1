package com.example.project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.example.project1.database.AppDatabase
import com.example.project1.databinding.ActivityMainBinding
import com.example.project1.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    companion object DatabaseSetup {
        var database: AppDatabase? = null
        var database2: FirebaseDatabase? = null
        const val TABLE_NAME: String = "Products"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = applicationContext.getSharedPreferences(
            applicationContext.packageName,
            Context.MODE_PRIVATE
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        database =
            Room.databaseBuilder(this, AppDatabase::class.java, getString(R.string.database_name))
                .build()
        database2 = FirebaseDatabase.getInstance()
        FirebaseAuth.getInstance().addAuthStateListener { fireBaseAuth ->
            if (fireBaseAuth.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.goToProductListButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        binding.goToProductListButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
        binding.goToOptionsButton.setBackgroundColor(
            sharedPreferences.getInt(
                "buttonBackgroundColor",
                Color.parseColor("#FFFFFF")
            )
        )
        binding.goToOptionsButton.setTextColor(
            sharedPreferences.getInt(
                "buttonTextColor",
                Color.parseColor("#000000")
            )
        )
    }

    fun goToProductList(view: View) {
        startActivity(Intent(this, ProductListActivity::class.java))
    }

    fun goToOptions(view: View) {
        startActivity(Intent(this, OptionsActivity::class.java))
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
    }
}