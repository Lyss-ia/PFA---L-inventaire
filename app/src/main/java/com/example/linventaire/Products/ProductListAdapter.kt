package com.example.linventaire.Products


import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.linventaire.R

class ProductListAdapter(private val context: Activity, private val models: List<String>, private val uniqueNumbers: List<String>, private val states: List<Boolean>)
    : ArrayAdapter<String>(context, R.layout.productslist_item, uniqueNumbers) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.productslist_item, null, true)

        //Récupération des éléments graphiques
        val model = rowView.findViewById(R.id.product_model) as TextView
        val number = rowView.findViewById(R.id.product_number) as TextView
        val state = rowView.findViewById(R.id.product_state) as TextView

        model.text = models[position]
        number.text = uniqueNumbers[position]
        //Code couleur pour l'état de location d'un produit
        if(states[position]==true){
            var stateColor = ContextCompat.getColor(context, R.color.red)
            state.setBackgroundColor(stateColor)
        }
        else{
            var stateColor = ContextCompat.getColor(context, R.color.green)
            state.setBackgroundColor(stateColor)
        }


        return rowView
    }
}