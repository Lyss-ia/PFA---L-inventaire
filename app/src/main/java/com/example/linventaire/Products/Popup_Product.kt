package com.example.linventaire.Products

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.Popup
import com.example.linventaire.R
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class Popup_Product : Popup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_popup_product)

        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        openPopup(popup_background, popup_view)
        readProduct()
        deleteProduct()
    }

    fun readProduct(){
        //récupérer les éléments graphiques
        val title = findViewById<AppCompatTextView>(R.id.popup_products_title)
        val state = findViewById<TextView>(R.id.state_product)
        val model = findViewById<TextView>(R.id.model_product)
        val type = findViewById<TextView>(R.id.type_product)
        val nunique = findViewById<TextView>(R.id.nunique_product)
        val link = findViewById<TextView>(R.id.link_product)
        val addedby = findViewById<TextView>(R.id.addedby_product)
        val modify_Button = findViewById<Button>(R.id.popup_product_btn)
        val qr = findViewById<ImageView>(R.id.qr_code_product)
        val delete_Button = findViewById<Button>(R.id.popup_delete_btn)

        val onlineUser = onlineUser()

        //Récupérer l'id du produit actif
        val intent = intent
        val productIdString = intent.getStringExtra("productID")
        val productId = productIdString!!.toInt()

        //sélectionner le produit concerné
        val db = DB.getInstance(this)
        val dao = db!!.productsDao()
        val product = dao.get(productId)

        //Préparer l'utilisateur qui a créé le produit
        val dao2 = db.usersDao()
        val userProduct = dao2.get(product.idUser)

        //Remplir les champs avec les données correspondantes
        if(product.state == true) {
            state.text = "Réservé"
        }
        else
        {
            state.text = "Disponible"
        }
        title.text = product.model
        model.text = "Modèle : " + product.model
        type.text = "Produit : " + product.type
        nunique.text = "N° Unique : " + product.unique_number
        link.text = "Site du fabriquant : " + product.link
        addedby.text = "Ajouté par : " + userProduct.pseudo

        //Création du qr code en fonction du numéro unique du produit
        val encoder = BarcodeEncoder()
        val bitmap = encoder.encodeBitmap(product.unique_number, BarcodeFormat.QR_CODE, 150, 150)
        qr.setImageBitmap(bitmap)

        //Vérifier que l'utilisateur soit admin pour lui donner des accès
        if(onlineUser.status != true){
            modify_Button.visibility = View.INVISIBLE
            delete_Button.visibility = View.INVISIBLE
        }

        //Renvoi des données à la page de modification
        modify_Button.setOnClickListener(){
            val userId = onlineUser.id
            val i1 = Intent(this, Popup_ModifyProduct::class.java)
            i1.putExtra("productID", productId.toString())
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
            finish()
        }
    }

    fun deleteProduct(){
        //Récupérer le bouton de suppression
        val delete_Button = findViewById<Button>(R.id.popup_delete_btn)

        val onlineUser = onlineUser()

        delete_Button.setOnClickListener(){
            //Récupérer l'id du produit actif
            val intent = intent
            val productIdString = intent.getStringExtra("productID")
            val productId = productIdString!!.toInt()

            //sélectionner le produit concerné
            val db = DB.getInstance(this)
            val dao = db!!.productsDao()
            val product = dao.get(productId)

            dao.deleteProduct(product)
            Toast.makeText(this, "Produit supprimé avec succès", Toast.LENGTH_SHORT).show()


            //Renvoyer les données à la liste
            val userId = onlineUser.id
            val i1 = Intent(this, ProductsListActivity::class.java)
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
            finish()
        }
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



}