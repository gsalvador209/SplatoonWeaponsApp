package com.tanucode.practica2.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tanucode.practica2.data.remote.model.WeaponDto
import com.tanucode.practica2.databinding.WeaponElementBinding

class WeaponViewHolder(
    private val binding : WeaponElementBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(weapon: WeaponDto){
        binding.tvName.text = weapon.name

        Glide.with(binding.root.context)
            .load(weapon.image)
            .into(binding.ivImage)

    }

}