package com.example.linventaire.Users

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.Popup
import com.example.linventaire.R

class Popup_User : Popup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_user)
        overridePendingTransition(0, 0)

        //Récupérer la vue
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_users_background)
        val popup_view = findViewById<CardView>(R.id.popup_users_with_border)

        openPopup(popup_background, popup_view)
        updateUser()
        deleteUser()
    }

    //changement de status pour un utilisateur
    private fun updateUser() {

        //Récupération des éléments graphiques nécessaires
        val name = findViewById<TextView>(R.id.pseudo_user)
        val email = findViewById<TextView>(R.id.email_user)
        val admin = findViewById<CheckBox>(R.id.admin_user)
        val title = findViewById<TextView>(R.id.popup_users_title)
        val modify_button = findViewById<Button>(R.id.popup_user_btn)

        //Récupérer l'id de l'utilisateur actif
        val intent = intent
        val userIdString = intent.getStringExtra("userID")
        val userId = userIdString!!.toInt()

        //sélectionner l'utilisateur concerné
        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        val user = dao.get(userId)


        //Remplir les champs avec les données correspondantes
        title.text = user.pseudo
        name.text = "Pseudo : " + user.pseudo
        email.text = "Email : " + user.email
        if (user.status) {
            admin.isChecked = true
        }

        //Bouton de sélection de l'admin
        modify_button.setOnClickListener() {

            if(user.id != 1){
                if (admin.isChecked()) {
                    val userMod = Users(user.id, user.pseudo, user.email, user.password, true)
                    dao.updateUser(userMod)
                } else {
                    val userMod = Users(user.id, user.pseudo, user.email, user.password, false)
                    dao.updateUser(userMod)
                }
                Toast.makeText(this, "Modification apportée avec succès", Toast.LENGTH_SHORT).show()
            }

            else{
                Toast.makeText(this, "Modification impossible", Toast.LENGTH_SHORT).show()
            }

            val i1 = Intent(this, UsersListActivity::class.java)
            startActivity(i1)
            finish()
        }
    }

    fun deleteUser() {
        //Récupérer le bouton supprimer
        val delete_Button = findViewById<Button>(R.id.popup_delete_btn)

        delete_Button.setOnClickListener() {
            //Récupérer l'id de l'utilisateur actif
            val intent = intent
            val userIdString = intent.getStringExtra("userID")
            val userId = userIdString!!.toInt()

            //sélectionner l'utilisateur concerné
            val db = DB.getInstance(this)
            val dao = db!!.usersDao()
            val user = dao.get(userId)

            if(user.id == 1){
                Toast.makeText(this, "L'utilisateur ne peut pas être supprimé", Toast.LENGTH_SHORT).show()
            }

            else{
                dao.deleteUser(user)
                Toast.makeText(this, "Utilisateur supprimé avec succès", Toast.LENGTH_SHORT).show()
            }

            val i1 = Intent(this, UsersListActivity::class.java)
            startActivity(i1)
            finish()
        }
    }
}