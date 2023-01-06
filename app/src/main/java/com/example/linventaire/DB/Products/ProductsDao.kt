package com.example.linventaire.DB.Products

import androidx.room.*

@Dao
interface ProductsDao {
    @Query("SELECT * FROM Products")
    fun get(): List<Products>
    @Query("SELECT * FROM Products WHERE id = :id")
    fun get(id: Int): Products
    @Query("SELECT * FROM Products WHERE unique_number = :unique_number")
    fun get(unique_number: String): Products
    @Insert
    fun insertProduct(vararg listCategories: Products)
    @Update
    fun updateProduct(task: Products)
    @Delete
    fun deleteProduct(task: Products)
}