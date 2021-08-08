package com.example.myapplication.data

import com.google.gson.Gson

data class FortressModel(var platformPassword: String? = null, var userName: String? = null,
                          var otherInfo: String? = null){

    override fun toString(): String {
        return Gson().toJson(this)
    }
}