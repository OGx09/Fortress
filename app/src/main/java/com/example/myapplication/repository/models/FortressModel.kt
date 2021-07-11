package com.example.myapplication.repository.models

import com.google.gson.Gson

data class FortressModel(val platformWebsite: String?, val platformLogo: String?,
                         val platformPassword: String?, val platformName: String?,
                          val otherInfo: String?
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}