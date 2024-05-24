package com.your.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


import com.bumptech.glide.Glide

class PetshopLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_petshop_login)
        
        Glide.with(this).load("https://i.imgur.com/1tMFzp8.png").into(findViewById(R.id.rdwxw504884a))
		
        
        
    }
}
    