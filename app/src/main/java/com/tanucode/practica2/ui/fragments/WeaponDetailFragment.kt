package com.tanucode.practica2.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.tanucode.practica2.R
import com.tanucode.practica2.application.WeaponsRFAPP
import com.tanucode.practica2.constants.Constants
import com.tanucode.practica2.data.WeaponsRepository
import com.tanucode.practica2.data.remote.model.WeaponDetailDto
import com.tanucode.practica2.databinding.FragmentWeaponDetailBinding
import com.tanucode.practica2.ui.BaseFragment
import kotlinx.coroutines.launch
import org.w3c.dom.Text

private const val ARG_WEAPON_ID  = "id"


class WeaponDetailFragment : BaseFragment(R.layout.fragment_weapon_detail), OnMapReadyCallback {

    private var _binding : FragmentWeaponDetailBinding? = null
    private val binding get() = _binding!! //Su valor solo se asigna cunado _binding se infla

    private var weaponDetailData: WeaponDetailDto? = null
    private var weaponId : String? = null
    private lateinit var  repository: WeaponsRepository

    //Para videos
    private var exoPlayer: ExoPlayer? = null

    //Para maps
    private lateinit var googleMap: GoogleMap
    private var isMapReady = false


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

        MapsInitializer.initialize(requireContext())

        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction()
            .replace(R.id.map,mapFragment)
            .commitNow()
        mapFragment.getMapAsync(this)

        repository = (requireActivity().application as WeaponsRFAPP).repository

        loadWeaponDetail()
    }

    private fun loadWeaponDetail(){
        lifecycleScope.launch { //Cuando se crea la vista se llama al API
            binding.pbLoading.visibility = View.VISIBLE
            try {
                val weaponDetail = repository.getWeaponDetail(weaponId)
                weaponDetailData = weaponDetail

                binding.apply {
                    tvName.text = weaponDetail.name
                    tvClass.text = weaponDetail.weaponClass
                    tvSub.text  = weaponDetail.sub
                    tvSpecial.text = weaponDetail.special

                    //Barras de progreso
                    weaponDetail.range?.let { range ->
                        progressRange.max      = 100
                        progressRange.progress = range
                        tvRange.text      = range.toString()
                    }
                    weaponDetail.damage?.let { damage ->
                        progressDamage.max      = 160
                        progressDamage.progress = damage
                        tvDamage.text      = damage.toString()
                    }

                    tvDamage.text = weaponDetail.damage.toString()

                    Glide.with(requireActivity())
                        .load(weaponDetail.image)
                        .into(ivImage)

                    weaponDetail.video?.let{ videoUrl->
                        initializePlayer(videoUrl)
                    } ?: run {
                        exoView.visibility = View.GONE
                        model.visibility = View.GONE
                        Toast.makeText(requireContext(),
                            getString(R.string.video_unavailable), Toast.LENGTH_SHORT).show()
                    }
                    tryShowMarker()
                }

            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                binding.pbLoading.visibility = View.GONE
            }
            Log.d(Constants.LOGTAG, getString(R.string.id_recived, id.toString()))
        }
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
                    binding.model.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_video),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            player.prepare()

            player.playWhenReady = true
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        isMapReady = true
        tryShowMarker()
    }



    private fun tryShowMarker(){
        val detail = weaponDetailData
        Log.d(Constants.LOGTAG,"isMapReady: $isMapReady")
        Log.d(Constants.LOGTAG,"detail: $detail")
        if (!isMapReady || detail == null) return

        detail.lat?.let { lat ->
            detail.lon?.let { lon ->
                val loc = LatLng(lat, lon)

                val original = BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_pin
                )

                val sizeDp = 64f
                val sizePx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    sizeDp,
                    resources.displayMetrics
                ).toInt()


                val smallMarker: Bitmap = Bitmap.createScaledBitmap(
                    original,
                    sizePx,
                    sizePx,
                    false
                )

                googleMap.addMarker(
                    MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        .title(detail.name)
                )
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f))
            }
        }

    }

    override fun onNetworkdRestorded() {
        loadWeaponDetail()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =  null //Por el ciclo de vida tricky del fragment
        //Es necesario liberar el binding al destruirse
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