package com.example.linventaire

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import kotlinx.android.synthetic.main.activity_popup_login.*

class Popup_Login : Popup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_popup_login)

        //Récupérer la vue
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_login_background)
        val popup_view = findViewById<CardView>(R.id.popup_login_with_border)

        openPopup(popup_background,popup_view)
        verifyData()
    }

    //Vérifie les données entrées dans les champs
    private fun verifyData(){
        //Récupérer la vue
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_login_background)
        val popup_view = findViewById<CardView>(R.id.popup_login_with_border)

        popup_login_btn.setOnClickListener() {
            val password = findViewById<EditText>(R.id.login_password_users).text.toString().trim()
            val email = findViewById<EditText>(R.id.login_email_users).text.toString().trim()

            //Controle des données
            if(password.length>3 && email.length>3 ){
                read()
            }
            else{
                Toast.makeText(this, "Les données entrées sont incorrectes", Toast.LENGTH_SHORT).show()
                buttonPressed(popup_background, popup_view)

            }
        }
    }

    //Lecture la base de donnée
    private fun read() {
        //récupération des éléments graphiques
        val email_users = findViewById<EditText>(R.id.login_email_users)
        val password_users = findViewById<EditText>(R.id.login_password_users)
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_login_background)
        val popup_view = findViewById<CardView>(R.id.popup_login_with_border)

        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        val u1 : Users? = dao.get(email_users.text.toString())

        //Est-ce que l'email (unique dans la DB) existe ?
        if(u1 == null){
            Toast.makeText(this, "L'utilisateur spécifié n'existe pas", Toast.LENGTH_SHORT).show()
            buttonPressed(popup_background, popup_view)
            return
        }

        //Les mots de passe correspondent ?
        if(password_users.text.toString().trim() != u1!!.password)
        {
            Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show()
            buttonPressed(popup_background, popup_view)
            return
        }

        Toast.makeText(this, "Bienvenue " + u1.pseudo, Toast.LENGTH_SHORT).show()


        //Passer les infos à la page suivante
        val userId = u1.id
        val i1 = Intent(this, ChoicesActivity::class.java)
        i1.putExtra("userID", userId.toString())
        startActivityForResult(i1, 0)
        finish()
    }
}