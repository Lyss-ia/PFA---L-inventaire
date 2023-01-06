package com.example.linventaire.DB.Users

import androidx.room.*
import com.example.linventaire.DB.UserWithProducts

@Dao
interface UsersDao {
    @Query("SELECT * FROM Users")
    fun get(): List<Users>
    @Query("SELECT * FROM Users WHERE email = :email")
    fun get(email: String): Users
    @Query("SELECT * FROM Users WHERE id = :id")
    fun get(id: Int): Users
    @Insert
    fun insertUser(vararg listCategories: Users)
    @Update
    fun updateUser(task: Users)
    @Delete
    fun deleteUser(task: Users)
    //relation entre les deux entités
    @Transaction
    @Query("SELECT * FROM Users")
    fun getUserWithProducts(): List<UserWithProducts>
}