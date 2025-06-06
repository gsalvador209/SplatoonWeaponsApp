package com.tanucode.practica2.auth

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.tanucode.practica2.MainActivity
import com.tanucode.practica2.R
import com.tanucode.practica2.databinding.ActivityLoginBinding
import com.tanucode.practica2.utils.message

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    //Variables de inicio de sesión
    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Forza orientación veretical
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        //Instancia de FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) //Verifica que no haya un token activo
            actionLoginSuccesful()

        binding.btnLogin.setOnClickListener {
            if (!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            authenticateUser(email, password)
        }

        binding.btnRegistrarse.setOnClickListener {
            if(!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            createUser(email,password)
        }

        binding.tvRestablecerPassword.setOnClickListener {
            resetPassword()
        }

    }

    private fun actionLoginSuccesful(): Unit {
        startActivity(Intent(this, MainActivity::class.java))
        finish() //Saca el login del backstack
    }

    private fun validateFields() : Boolean{
        //Obtiene los datos del binding
        email = binding.tietEmail.text.toString().trim()
        password = binding.tietContrasena.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = getString(R.string.enter_email)
            binding.tietEmail.requestFocus()
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.tietEmail.error = getString(R.string.email_invalid)
            binding.tietEmail.requestFocus() //
        }
        //Verifica contraseña
        if(password.isEmpty()){
            binding.tietContrasena.error = getString(R.string.password_required)
            binding.tietContrasena.requestFocus()
            return false
        }
        return true


    }

    private fun authenticateUser(email : String, password : String) : Unit{
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {authResult ->
            if(authResult.isSuccessful){
                message(getString(R.string.login_succesful))
            }else{
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }
    }

    private fun handleErrors(task : Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e : Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_WRONG_PASSWORD" -> {
                message(getString(R.string.wrong_email))
            }
            "ERROR_ACCOUNT_EXIST_WITH_DIFFERENT_CREDENTIAL" -> {
                message(getString(R.string.account_exists))
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                message(getString(R.string.email_used))
            }
            "ERROR_USER_TOKEN_EXPIRED"-> {
                message(getString(R.string.expired_session))
            }
            "ERROR_USER_NOT_FOUND" -> {
                message(getString(R.string.user_not_found))
            }
            "ERROR_WEAK_PASSWORD" -> {
                message(getString(R.string.invalid_password))
                binding.tietContrasena.error = getString(R.string.password_requierements)
                binding.tietContrasena.requestFocus()
            }
            "NO_NETWORK" -> {
                message(getString(R.string.lost_connection))
            }
            else -> {
                message(getString(R.string.error_aunthenticate))
            }
        }

    }

    private fun createUser(usr: String, psw: String){
        firebaseAuth.createUserWithEmailAndPassword(usr,psw).addOnCompleteListener { authResult->
            if(authResult.isSuccessful){
                //Registro correcto
                firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                    message(getString(R.string.verification_email_sent))
                }?.addOnFailureListener {
                    message(getString(R.string.verification_email_couldn_t_be_sent))
                }

                message(getString(R.string.user_created_successfully))
                actionLoginSuccesful()
            }else{
                binding.progressBar.visibility = View.GONE
                handleErrors(authResult)
            }
        }

    }

    private fun resetPassword(){
        val etResetEmail = EditText(this)
        etResetEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.reset_password))
            .setMessage(getString(R.string.enter_your_email_to_reset_password))
            .setView(etResetEmail)
            .setPositiveButton(getString(R.string.send)){ _, _ ->
                val mail = etResetEmail.text.toString()
                if(mail.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener {
                        message(getString(R.string.verification_email_sent))
                    }.addOnFailureListener {
                        message(getString(R.string.verification_email_couldn_t_be_sent))
                    }
                }else{
                    message(getString(R.string.enter_a_valid_email))
                }
            }
            .setNegativeButton(getString(R.string.cancel)){ dialog,_ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }



}