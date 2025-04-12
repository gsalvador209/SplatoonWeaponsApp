package com.tanucode.practica2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.tanucode.practica2.constants.Constants
import com.tanucode.practica2.data.WeaponsRepository
import com.tanucode.practica2.data.remote.RetrofitHelper
import com.tanucode.practica2.data.remote.model.WeaponsList
import com.tanucode.practica2.databinding.ActivityMainBinding
import com.tanucode.practica2.ui.fragments.WeaponListFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    WeaponListFragment()
                )
                .commit()
        }


        //*****************************
        //    PARA PRUEBAS INCIALES
        //*****************************
//        val retrofit = RetrofitHelper().getRetrofit()
//        val repository = WeaponsRepository(retrofit)
//
//        lifecycleScope.launch {
//            try{
//                //val weapons = repository.getWeapons()
//                //Log.d(Constants.LOGTAG, "Respuesta $weapons")
//                val weapon = repository.getWeaponDetail("shooter_quickmiddle_01")
//                Log.d(Constants.LOGTAG, "Respuesta $weapon")
//            }
//            catch (e: Exception){
//                e.printStackTrace()
//                Toast.makeText(
//                    this@MainActivity,
//                    "Error en la conexión: ${e.message}",
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//            }
//        }
    }
}