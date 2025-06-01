package com.tanucode.practica2.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.tanucode.practica2.R
import com.tanucode.practica2.application.WeaponsRFAPP
import com.tanucode.practica2.constants.Constants
import com.tanucode.practica2.data.WeaponsRepository
import com.tanucode.practica2.databinding.FragmentWeaponDetailBinding
import kotlinx.coroutines.launch

private const val ARG_WEAPON_ID  = "id"


class WeaponDetailFragment : Fragment() {

    private var _binding : FragmentWeaponDetailBinding? = null
    private val binding get() = _binding!! //Su valor solo se asigna cunado _binding se infla

    //Id para identificar cada arma, No es un número
    private var weaponId : String? = null

    //Repositorio para las funciones del API
    private lateinit var  repository: WeaponsRepository

    //Para videos
    private var exoPlayer: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args -> //Buena práctica nombrarlo así
            weaponId = args.getString(ARG_WEAPON_ID) //El parámetro se recibe propiamente
            //Es nullable porque podría levantarse el fragment sin parámetro
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Se infla el xml
        _binding = FragmentWeaponDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = (requireActivity().application as WeaponsRFAPP).repository

        lifecycleScope.launch { //Cuando se crea la vista se llama al API
            try {
                val weaponDetail = repository.getWeaponDetail(weaponId)
                binding.apply {
                    tvName.text = weaponDetail.name
                    tvClass.text = weaponDetail.weaponClass
                    tvSub.text  = weaponDetail.sub
                    tvSpecial.text = weaponDetail.special
                    tvRange.text = weaponDetail.range.toString()
                    tvDamage.text = weaponDetail.damage.toString()

                    Glide.with(requireActivity())
                        .load(weaponDetail.image)
                        .into(ivImage)

                    weaponDetail.video?.let{ videoUrl->
                        initializePlayer(videoUrl)
                    } ?: run {
                        exoView.visibility = View.GONE
                        Toast.makeText(requireContext(), "Video no disponible", Toast.LENGTH_SHORT).show()
                    }

                }

            }catch (e: Exception){
                //Error de conexión
            }finally {
                binding.pbLoading.visibility = View.GONE
            }
            Log.d(Constants.LOGTAG, getString(R.string.id_recived, id.toString()))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =  null //Por el ciclo de vida tricky del fragment
        //Es necesario liberar el binding al destruirse
    }

    private fun initializePlayer(videoUrl: String){
        exoPlayer = ExoPlayer.Builder(requireContext()).build().also { player ->
            binding.exoView.player = player

            player.repeatMode = Player.REPEAT_MODE_ONE

            val mediaItem = MediaItem.fromUri(videoUrl)
            player.setMediaItem(mediaItem)

            player.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    binding.exoView.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error al reproducir el video",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            player.prepare()

            player.playWhenReady = true
        }
    }

    companion object{
        @JvmStatic
        fun newInstance(id: String) =
            WeaponDetailFragment().apply { //Se instancia un elemento sin id
                arguments = Bundle().apply {
                    putString(ARG_WEAPON_ID,id) //Asi se pasa como un bundle
                    //se esta enviando un nuevo WeaponDetal con el id
                }
            }
    }

}