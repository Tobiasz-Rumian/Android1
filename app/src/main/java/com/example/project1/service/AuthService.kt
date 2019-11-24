package com.example.project1.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthService {
    public var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser: FirebaseUser? = null

    public fun checkAuthStatus(): FirebaseUser? {
        currentUser = mAuth.currentUser
        return currentUser
    }
}