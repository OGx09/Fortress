package com.example.myapplication.repository

import com.example.myapplication.data.WebsiteLogo
import retrofit2.http.GET
import retrofit2.http.Query

object WebApi{
    const val BEST_ICON ="https://besticon-demo.herokuapp.com/"
}

interface WebsiteLogoService {

    @GET("allicons.json")
    suspend fun getWebsiteLogo(@Query("url") websiteUrl: String): WebsiteLogo
}