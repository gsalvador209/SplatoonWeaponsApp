package com.tanucode.practica2.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tanucode.practica2.R
import com.tanucode.practica2.application.WeaponsRFAPP
import com.tanucode.practica2.constants.Constants
import com.tanucode.practica2.data.WeaponsRepository
import com.tanucode.practica2.databinding.FragmentWeaponListBinding
import com.tanucode.practica2.ui.adapters.WeaponsAdapter
import kotlinx.coroutines.launch


class WeaponListFragment : Fragment() {

    private var _binding : FragmentWeaponListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: WeaponsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeaponListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as WeaponsRFAPP).repository

        lifecycleScope.launch {
            try{
                val weapons = repository.getWeapons()
                binding.rvWeapons.apply{
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = WeaponsAdapter(weapons){ selectedWeapon ->
                        Log.d(Constants.LOGTAG,
                            context.getString(R.string.Clicked, selectedWeapon.name))

                        selectedWeapon.id?.let{ id ->
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(
                                    R.id.fragment_container,
                                    WeaponDetailFragment.newInstance(id) //Aqui se usa el mét.odo del companion object
                                )
                                .addToBackStack(null)
                                .commit()

                        }

                    }



                }


            }catch(e : Exception){
                e.printStackTrace()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.conexion_error),
                    Toast.LENGTH_LONG
                ).show()
            }finally {
                binding.pbLoading.visibility = View.GONE
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}