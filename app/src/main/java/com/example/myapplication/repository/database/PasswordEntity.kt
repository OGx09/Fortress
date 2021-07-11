package com.example.myapplication.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myapplication.repository.models.FortressModel
import com.google.gson.Gson


/**
 * TODO: User can either use unique password or set a single password ffor all
 */

@Entity
data class PasswordEntity(@PrimaryKey(autoGenerate = true) val id : Int?,
                          @ColumnInfo(name ="encryptedData") var encryptedData: String?=null,
                          @ColumnInfo var websiteName: String,
                          @ColumnInfo var website: String,
                          @ColumnInfo var otherInfo: String,
                          @Ignore var fortressModel: FortressModel){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}