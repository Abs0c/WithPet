package com.example.porject

import android.accounts.AuthenticatorDescription
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "notesTable")
class Note (
    @ColumnInfo(name = "title")val noteTitle:String,
    @ColumnInfo(name= "description")val noteDescription: String,
    @ColumnInfo(name = "timestamp")val timestamp: String,
    //@ColumnInfo(name = "mood")val mood: String,
    ){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}