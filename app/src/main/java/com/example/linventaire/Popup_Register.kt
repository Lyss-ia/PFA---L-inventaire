package com.example.linventaire

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import kotlinx.android.synthetic.main.activity_popup_register.*


class Popup_Register : Popup() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_popup_register)
        //Récupérer les vues
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_register_background)
        val popup_view = findViewById<CardView>(R.id.popup_register_with_border)

        openPopup(popup_background, popup_view)
        verifyData()
    }

    //Vérifie les données entrées dans les champs
    private fun verifyData(){

        //Récupérer les vues
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_register_background)
        val popup_view = findViewById<CardView>(R.id.popup_register_with_border)


        popup_register_btn.setOnClickListener() {

            val password = findViewById<EditText>(R.id.register_password_users).text.toString().trim()
            val email = findViewById<EditText>(R.id.register_email_users).text.toString().trim()
            val pseudo = findViewById<EditText>(R.id.register_pseudo_users).text.toString().trim()

            if(isValidEmail(email) && password.length>3 &&pseudo.length>3 ){
                write()
            }

            else{
                Toast.makeText(this, "Données invalides", Toast.LENGTH_SHORT).show()
                buttonPressed(popup_background, popup_view)
            }
        }
    }

    //Vérification de l'email
    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //Ecriture dans la base de donnée
    private fun write() {

        val pseudo_users = findViewById<EditText>(R.id.register_pseudo_users)
        val email_users = findViewById<EditText>(R.id.register_email_users)
        val password_users = findViewById<EditText>(R.id.register_password_users)
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_register_background)
        val popup_view = findViewById<CardView>(R.id.popup_register_with_border)

        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        val u1 = Users(0,
            pseudo_users.text.toString().trim(),
            email_users.text.toString().trim(),
            password_users.text.toString().trim(),
            false
        )

        val u2 = dao.get(u1.email)

        if(u2 == null){
            dao.insertUser(u1)
        }
        else{
            Toast.makeText(this, "Il semblerait que vous ayez déjà un compte !", Toast.LENGTH_SHORT).show()
            buttonPressed(popup_background, popup_view)
            return
        }

        //récupérer les infos de l'utilisateur inscrit
        val userList = dao.get()
        val lastUser = userList.last()
        val userId = lastUser.id
        Toast.makeText(this, "Bienvenue " + lastUser.pseudo, Toast.LENGTH_SHORT).show()


        if(userId==1){
            //Création de l'admin
            var uAdmin = dao.get(1)

            uAdmin = Users(1,
                pseudo_users.text.toString().trim(),
                email_users.text.toString().trim(),
                password_users.text.toString().trim(),
                true)
            dao.updateUser(uAdmin)
        }

        //Passer les infos à la page suivante
        val i1 = Intent(this, ChoicesActivity::class.java)
        i1.putExtra("userID", userId.toString())
        startActivityForResult(i1, 0)
        finish()
    }
}