package com.example.linventaire.Users

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.linventaire.R

class UserListAdapter(private val context: Activity, private val uPseudo: List<String>, private val uEmail: List<String>, private val uStatus: List<Boolean>)
    : ArrayAdapter<String>(context, R.layout.userlist_items, uEmail) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.userlist_items, null, true)
        //Récupération des éléments graphiques
        val pseudo = rowView.findViewById(R.id.user_pseudo) as TextView
        val email = rowView.findViewById(R.id.user_email) as TextView
        val state = rowView.findViewById(R.id.user_status) as TextView

        pseudo.text = uPseudo[position]
        email.text = uEmail[position]

        //Inscrit "Admin" pour tout ceux ayant reçu le statut
        if(uStatus[position]==true){
            state.text = "Admin"
        }


        return rowView
    }
}