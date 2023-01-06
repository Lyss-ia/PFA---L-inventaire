package com.example.linventaire

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FirstPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstpage)

        //Assigner les éléments graphiques à une variable
        val register_btn = findViewById<Button>(R.id.register_btn)
        val login_btn = findViewById<Button>(R.id.login_btn)

        //Inscription d'un utilisateur
        register_btn.setOnClickListener{
            val intent = Intent(this, Popup_Register::class.java)
            startActivity(intent)
        }

        //Enregistrement d'un utilisateur
        login_btn.setOnClickListener{
            val intent = Intent(this, Popup_Login::class.java)
            startActivity(intent)
        }

    }
}