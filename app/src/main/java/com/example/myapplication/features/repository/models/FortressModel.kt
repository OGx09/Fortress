package com.example.myapplication.features.repository.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

data class FortressModel(val platformWebsite: String?, val platformLogo: String?,
                         val platformPassword: String?, val platformName: String?,
                          val buzzWord: String?
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}