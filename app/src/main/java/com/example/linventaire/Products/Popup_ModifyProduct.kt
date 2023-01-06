package com.example.linventaire.Products

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.Popup
import com.example.linventaire.R
import kotlinx.android.synthetic.main.activity_popup_addproducts.*

class Popup_ModifyProduct : Popup() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_modify_product)
        //Récupérer les vues
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        openPopup(popup_background, popup_view)
        updateProduct()
    }

    private fun updateProduct() {
        //Récupérer les éléments graphiques
        val product_type = findViewById<EditText>(R.id.products_type)
        val product_model = findViewById<EditText>(R.id.products_model)
        val product_uniqueNumber = findViewById<EditText>(R.id.products_uniquenumber)
        val product_link = findViewById<EditText>(R.id.products_link)
        val onlineUser = onlineUser()
        val popup_background = findViewById<ConstraintLayout>(R.id.popup_products_background)
        val popup_view = findViewById<CardView>(R.id.popup_products_with_border)

        products_popup_ok_btn.setOnClickListener() {

            val db = DB.getInstance(this)
            val dao = db!!.productsDao()
            val intent = intent
            val productIdString = intent.getStringExtra("productID")//récupération du numéro unique
            val productId = productIdString!!.toInt()

            //Récupérer le produit via l'id
            var p1 = dao.get(productId)

            //si les champs son rempli, changer les données, sinon les conserver
            if (isCompleted(product_type.text.toString().trim())) {
                p1.type = product_type.text.toString().trim()
            }

            if (isCompleted(product_model.text.toString().trim())) {
                p1.model = product_model.text.toString().trim()
            }

            if (isCompleted(product_uniqueNumber.text.toString().trim())) {
                p1.unique_number = product_uniqueNumber.text.toString().trim()
            }

            if (isCompleted(product_link.text.toString().trim())) {
                p1.link = product_link.text.toString().trim()
            }

            p1 = Products(
                productId,
                onlineUser.id,
                p1.type,
                p1.model,
                p1.unique_number,
                p1.link,
                p1.state
            )

            val p2 = dao.get(p1.unique_number)

            if(p2 != null){
                dao.updateProduct(p1)
                Toast.makeText(this, "Produit modifié avec succès", Toast.LENGTH_SHORT).show()
            }
            else{

                Toast.makeText(this, "Le produit que vous essayez de modifier semble déjà exister", Toast.LENGTH_SHORT).show()
                buttonPressed(popup_background, popup_view)
            }

            //Passage de l'utilisateur actif à la liste qu'on va rouvrir
            val userId = onlineUser.id
            val i1 = Intent(this, Popup_Product::class.java)
            i1.putExtra("productID", productId.toString())
            i1.putExtra("userID", userId.toString())
            startActivityForResult(i1, 0)
            finish()
        }

    }

    //Vérification des champs
    private fun isCompleted(data: String): Boolean {
        return !TextUtils.isEmpty(data)
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