package com.example.linventaire.DB.Products

import androidx.annotation.NonNull
import androidx.room.*

@Entity(indices = [Index(value = ["unique_number"], unique = true)])
data class Products(
    @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name="idUser") var idUser: Int,
    @ColumnInfo(name="type") @NonNull var type : String,
    @ColumnInfo(name="model")  @NonNull var model: String,
    @ColumnInfo(name="unique_number") @NonNull var unique_number: String,
    @ColumnInfo(name="link") var link: String,
    @ColumnInfo(name="state")  @NonNull var state: Boolean
)