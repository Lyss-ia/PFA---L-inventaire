package com.example.linventaire.Products

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.Popup
import com.example.linventaire.R
import kotlinx.android.synthetic.main.activity_popup_addproducts.*

class Popup_AddProducts : Popup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_popup_addproducts)

        //Récupérer les vues
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        openPopup(popup_background, popup_view)
        verifyData()

    }

    private fun verifyData(){
        //Récupérer les éléments graphiques
        val product_type = findViewById<EditText>(R.id.products_type)
        val product_model = findViewById<EditText>(R.id.products_model)
        val product_uniqueNumber = findViewById<EditText>(R.id.products_uniquenumber)
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        products_popup_ok_btn.setOnClickListener() {
            //on récupère les textes entrés dans les champs avant de les vérifier
            val model = product_model.text.toString().trim()
            val uniqueNumber = product_uniqueNumber.text.toString().trim()
            val type = product_type.text.toString().trim()

            //tous les champs doivent être rempli pour créer un produit
            if (isCompleted(model) && isCompleted(uniqueNumber) && isCompleted(type)) {
                write()
            }
            else {
                Toast.makeText(this, "Données invalides", Toast.LENGTH_SHORT).show()
                buttonPressed(popup_background, popup_view)
            }
        }
    }

    //Vérification des champs
    private fun isCompleted(data: String): Boolean {
        return !TextUtils.isEmpty(data)
    }

    private fun write() {
        //Récupérer les éléments graphiques
        val product_type = findViewById<EditText>(R.id.products_type)
        val product_model = findViewById<EditText>(R.id.products_model)
        val product_uniqueNumber = findViewById<EditText>(R.id.products_uniquenumber)
        val product_link = findViewById<EditText>(R.id.products_link)
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        val onlineUser = onlineUser()

        val db = DB.getInstance(this)
        val dao = db!!.productsDao()
        val p1 = Products(0,
            onlineUser.id,
            product_type.text.toString().trim(),
            product_model.text.toString().trim(),
            product_uniqueNumber.text.toString().trim(),
            product_link.text.toString().trim(),
            false
        )

        val p2 = dao.get(p1.unique_number)

        if(p2 == null){
            dao.insertProduct(p1)
            Toast.makeText(this, "Produit ajouté avec succès", Toast.LENGTH_SHORT).show()
        }
        else{

            Toast.makeText(this, "Le produit que vous essayez d'ajouter semble déjà exister", Toast.LENGTH_SHORT).show()
            buttonPressed(popup_background, popup_view)
        }



        //On relance la liste en y envoyant toujours l'utilisateur actif
        val userId = onlineUser.id
        val i1 = Intent(this, ProductsListActivity::class.java)
        i1.putExtra("userID", userId.toString())
        startActivityForResult(i1, 0)
        finish()
    }


    //récupérer l'utilisateur en ligne
    private fun onlineUser(): Users {
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