package com.example.myapplication.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson


/**
 * TODO: User can either use unique password or set a single password ffor all
 */

@Entity
data class PasswordEntity(@PrimaryKey(autoGenerate = true) val id : Int?,
                          @ColumnInfo(name ="encryptedData") val encryptedData: String?){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}