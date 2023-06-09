package com.example.porject

import android.accounts.AuthenticatorDescription
import android.graphics.Path
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "notesTable")
class Note (
    @ColumnInfo(name = "title")val noteTitle:String,
    @ColumnInfo(name = "petname")val petName:String,
    @ColumnInfo(name= "description")val noteDescription: String,
    @ColumnInfo(name = "timestamp")val timestamp: String,
    @ColumnInfo(name = "image")val noteImage: ByteArray
    ){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}