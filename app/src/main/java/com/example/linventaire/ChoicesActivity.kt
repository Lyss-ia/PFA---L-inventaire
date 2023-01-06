package com.example.linventaire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.Products.ProductsListActivity
import com.example.linventaire.Users.UsersListActivity

class ChoicesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choices)

        //récupérer les éléments graphiques
        val scans_button = findViewById<Button>(R.id.scans_button)
        val products_button = findViewById<Button>(R.id.products_list_button)
        val users_button = findViewById<Button>(R.id.users_list_button)
        val logout = findViewById<Button>(R.id.logout_button)

        val onlineUser = onlineUser()

        //Vers le scanner
        scans_button.setOnClickListener(){
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }

        //Vers la liste de produits
        products_button.setOnClickListener(){
            val userId = onlineUser.id
            val i1 = Intent(this, ProductsListActivity::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
        }

        //Si on est pas le superutilisateur on ne peut pas accéder à la gestion d'utilisateurs
        if(onlineUser.id != 1 ){
            users_button.setVisibility(View.INVISIBLE)
        }

        users_button.setOnClickListener(){
            val userId = onlineUser.id
            val i1 = Intent(this, UsersListActivity::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
        }

        //Déconnection
        logout.setOnClickListener(){
            Toast.makeText(this, "Au revoir " + onlineUser.pseudo + " !", Toast.LENGTH_SHORT).show()

            val intent = Intent(this,FirstPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun onlineUser():Users{

        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        //Récupérer l'id du produit actif
        val intent = intent
        val userIdString = intent.getStringExtra("userID")
        val onlineUserid = userIdString!!.toInt()
        val onlineUser = dao.get(onlineUserid)
        return onlineUser
    }
}