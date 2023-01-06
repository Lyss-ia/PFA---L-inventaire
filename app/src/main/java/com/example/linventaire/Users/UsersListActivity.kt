package com.example.linventaire.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.ChoicesActivity
import com.example.linventaire.R

class UsersListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userslist)

        //Récupérer le bouton retour
        val previous_button = findViewById<Button>(R.id.users_previous_button)

        previous_button.setOnClickListener() {
            //Renvoyer l'id du super admin à la page des choix
            val userId = 1
            val i1 = Intent(this, ChoicesActivity::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
            finish()
        }

        listUsers()

    }

    //méthode pour lister les utilisateurs (on récup dans la DB et on place dans un tableau pour la vue)
    private fun listUsers() {
        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        val usersList = dao.get()
        val pseudoList = usersList.map(::takePseudo)//récupérer juste les pseudo pour l'affichage
        val emailList = usersList.map(::takeEmail)//récupérer juste les emails pour l'affichage
        val statusList = usersList.map(::takeStatus)//récupérer juste les models pour l'affichage
        val uListView = findViewById<ListView>(R.id.users_listview)

        //On génère la liste des utilisateurs grâce à un adapter
        val uListAdapter = UserListAdapter(this, pseudoList, emailList, statusList)
        uListView.adapter = uListAdapter

        //Click sur un élément de la liste
        uListView.setOnItemClickListener { parent, view, position, id ->
            val userEmail = parent.getItemAtPosition(position)
            val u1 = dao.get(userEmail.toString())//Reconnait l'utilisateur à l'email unique
            val userId = u1.id
            val i1 = Intent(this, Popup_User::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
        }

    }

    private fun takePseudo(users: Users) = users.pseudo //récupérer les pseudos dans la liste des utilisateurs
    private fun takeEmail(users: Users) = users.email //récupérer les email dans la liste des utilisateurs
    private fun takeStatus(users: Users) = users.status //récupérer les status dans la liste des utilisateur
}