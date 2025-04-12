package com.tanucode.practica2.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tanucode.practica2.data.remote.model.WeaponDto
import com.tanucode.practica2.databinding.WeaponElementBinding

class WeaponsAdapter(
    private val weapons : List<WeaponDto>,
    private val onItemClick : (WeaponDto) -> Unit
) : RecyclerView.Adapter<WeaponViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeaponViewHolder {
        val binding = WeaponElementBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeaponViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WeaponViewHolder,
        position: Int
    ) {
        val weapon = weapons[position]

        holder.bind(weapon)

        holder.itemView.setOnClickListener {
            onItemClick(weapon)
        }
    }

    override fun getItemCount(): Int = weapons.size
}