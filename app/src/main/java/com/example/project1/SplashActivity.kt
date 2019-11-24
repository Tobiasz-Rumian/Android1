package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.project1.service.AuthService
import com.example.project1.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AuthService.checkAuthStatus()==null){
            startActivity(Intent(this, LoginActivity::class.java))
        }else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
