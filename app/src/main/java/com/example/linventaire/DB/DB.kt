package com.example.linventaire.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.DB.Products.ProductsDao
import com.example.linventaire.DB.Users.Users
import com.example.linventaire.DB.Users.UsersDao

@Database(entities = [(Users::class), (Products::class)], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun productsDao(): ProductsDao

    companion object {
        private var INSTANCE: DB? = null
        //Singleton
        fun getInstance(context: Context): DB? {
            if (INSTANCE == null) {
                synchronized(DB::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        DB::class.java, "db").allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}

