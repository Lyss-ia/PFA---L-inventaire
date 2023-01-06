package com.example.linventaire.Products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.ChoicesActivity
import com.example.linventaire.R


class ProductsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productslist)

        //récupération des éléments graphiques
        val previous_button = findViewById<Button>(R.id.previous_button)
        val plus_button = findViewById<Button>(R.id.plus_button)

        val onlineUser = onlineUser()

        //retour a l'accueil
        previous_button.setOnClickListener(){
            val userId = onlineUser.id
            val i1 = Intent(this, ChoicesActivity::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
            finish()
        }

        //seuls les admins peuvent ajouter des produits ou les modifier
        if(onlineUser.status != true){
            plus_button.setVisibility(View.INVISIBLE)
        }
        //Lancement de la fenêtre pour ajouter des produits
        plus_button.setOnClickListener(){
            val userId = onlineUser.id
            val i1 = Intent(this, Popup_AddProducts::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
        }

        listProducts()
    }

    //récupérer l'utilisateur en ligne
    fun onlineUser(): Users {

        val db = DB.getInstance(this)
        val dao = db!!.usersDao()
        //Récupérer l'id du produit actif
        val intent = intent
        val userIdString = intent.getStringExtra("userID")
        val onlineUserid = userIdString!!.toInt()
        val onlineUser = dao.get(onlineUserid)
        return onlineUser
    }

    //méthode pour lister les produits (on récup dans la DB et on place dans un tableau pour la vue)
    private fun listProducts(){
        val db = DB.getInstance(this)
        val dao = db!!.productsDao()
        val productList = dao.get()
        val modelsList = productList.map(::takeModels)//récupérer juste les modèles pour l'affichage
        val numbersList = productList.map(::takeNumbers)//récupérer juste les numéros uniques pour l'affichage
        val statesList = productList.map(::takeStates)//récupérer juste les états pour l'affichage
        val pListView = findViewById<ListView>(R.id.products_list)

        val onlineUser = onlineUser()
        val userId = onlineUser.id

        //On génère une liste grapique via un adapter contenant les données des trois listes créées
        val pListAdapter = ProductListAdapter(this,modelsList,numbersList,statesList)
        pListView.adapter = pListAdapter

        //Click sur un élément de la liste
        pListView.setOnItemClickListener { parent, view, position, id ->
            //On se sert du numéro unique pour reconnaitre le produit qui sera ouvert en popup
            val productUniqueNumber = parent.getItemAtPosition(position)
            val p1 = dao.get(productUniqueNumber.toString())
            val productId = p1.id
            val i1 = Intent(this, Popup_Product::class.java)
            i1.putExtra("productID", productId.toString())
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)

        }
    }

    private fun takeModels(products: Products) = products.model //récupérer les models dans la liste des produits
    private fun takeNumbers(products: Products) = products.unique_number //récupérer les numéros uniques dans la liste des produits
    private fun takeStates(products: Products) = products.state //récupérer les états dans la liste des produits
}

