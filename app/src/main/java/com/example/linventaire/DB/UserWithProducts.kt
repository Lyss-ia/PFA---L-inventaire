package com.example.linventaire.DB

import androidx.room.*
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.DB.Users.Users

data class UserWithProducts(
    @Embedded val user: Users,
    @Relation(
        parentColumn = "id",
        entityColumn = "idUser"
    )
    val products: List<Products>
)