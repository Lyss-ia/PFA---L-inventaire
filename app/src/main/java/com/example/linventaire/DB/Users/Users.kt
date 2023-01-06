package com.example.linventaire.DB.Users

import androidx.annotation.NonNull
import androidx.room.*

@Entity(indices = [Index(value = ["email"], unique = true) ])
data class Users(
    @ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name="pseudo") @NonNull var pseudo : String,
    @ColumnInfo(name="email")  @NonNull var email: String,
    @ColumnInfo(name="password") @NonNull var password: String,
    @ColumnInfo(name="status") @NonNull var status: Boolean
)