package com.tanucode.practica2.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.tanucode.practica2.R
import com.tanucode.practica2.auth.LoginActivity
import com.tanucode.practica2.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.currentUser.let{ user ->
            binding.tvEmail.text = user?.email
        }

        binding.ivLogout.setOnClickListener {
            logout()
        }
        binding.tvLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        firebaseAuth.signOut()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }
}