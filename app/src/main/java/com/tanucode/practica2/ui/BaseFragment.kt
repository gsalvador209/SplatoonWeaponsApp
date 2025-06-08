package com.tanucode.practica2.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tanucode.practica2.R
import com.tanucode.practica2.utils.NetworkConnectionLiveData


abstract class BaseFragment(layoutId : Int) : Fragment(layoutId) {
    private var netSnackbar : Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se observa la conexión y muestra el snackbar
        NetworkConnectionLiveData(requireContext())
            .observe(viewLifecycleOwner){ isConnected ->
                if (isConnected) {
                    netSnackbar?.dismiss()
                    onNetworkdRestorded()
                } else {
                    netSnackbar = Snackbar.make(
                        view,
                        getString(R.string.connection_lost),
                        Snackbar.LENGTH_INDEFINITE
                    ).also { it.show() }
                }
        }
    }

    //Se invoca una vez cuando se recupera la red
    protected open fun onNetworkdRestorded() {}
}