package com.tanucode.practica2.data

import com.tanucode.practica2.data.remote.WeaponApi
import retrofit2.Retrofit

class WeaponsRepository (
    private val retrofit : Retrofit
){
    private val weaponapi = retrofit.create(WeaponApi::class.java)

    suspend fun getWeapons() = weaponapi.getWeapons()

    suspend fun getWeaponDetail(id: String?) = weaponapi.getWeaponDetail(id)

}