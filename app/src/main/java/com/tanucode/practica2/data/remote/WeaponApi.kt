package com.tanucode.practica2.data.remote

import com.tanucode.practica2.data.remote.model.WeaponDetailDto
import com.tanucode.practica2.data.remote.model.WeaponDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WeaponApi {

    @GET("/weapons/weapons_list")
    suspend fun getWeapons() : List<WeaponDto>


    @GET("/weapons/weapon/{id}")
    suspend fun getWeaponDetail(
        @Path("id")
        id: String?
    ) : WeaponDetailDto
}