package com.example.project1

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.regex.Pattern

class OptionsActivity : AppCompatActivity() {

    private lateinit var buttonBackgroundColor: EditText
    private lateinit var buttonTextColor: EditText
    private lateinit var saveButton: Button
    private lateinit var errorMessage: TextView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        buttonBackgroundColor = findViewById(R.id.backgroundColorInput)
        buttonTextColor = findViewById(R.id.textColorInput)
        saveButton = findViewById(R.id.saveButton)
        errorMessage = findViewById(R.id.errorMessage)
        sharedPreferences = applicationContext.getSharedPreferences(applicationContext.packageName,
            Context.MODE_PRIVATE)
    }

    override fun onStart() {
        super.onStart()
        saveButton.setBackgroundColor(sharedPreferences.getInt("buttonBackgroundColor",Color.parseColor("#FFFFFF")))
        saveButton.setTextColor(sharedPreferences.getInt("buttonTextColor",Color.parseColor("#000000")))
    }

    fun saveChanges(view: View) {
        errorMessage.visibility = View.GONE
        val hexPattern: Pattern = Pattern.compile("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})\$")
        val backgroundColorText = buttonBackgroundColor.text.toString()
        val textColorText = buttonTextColor.text.toString()
        if (hexPattern.matcher(backgroundColorText).matches()) {
            val color = Color.parseColor(backgroundColorText)
            saveButton.setBackgroundColor(color)
            sharedPreferences.edit().putInt("buttonBackgroundColor",color).apply()
        } else {
            errorMessage.visibility = View.VISIBLE
        }
        if (hexPattern.matcher(textColorText).matches()) {
            val color = Color.parseColor(textColorText)
            saveButton.setTextColor(color)
            sharedPreferences.edit().putInt("buttonTextColor",color).apply()
        } else {
            errorMessage.visibility = View.VISIBLE
        }
    }
}
