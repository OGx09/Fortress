package com.example.myapplication.data

import com.google.gson.Gson

data class FortressModel(var platformPassword: String? = null, var userName: String?,
                          var otherInfo: String?){

    override fun toString(): String {
        return Gson().toJson(this)
    }
}