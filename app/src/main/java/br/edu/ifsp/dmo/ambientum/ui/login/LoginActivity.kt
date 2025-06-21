package br.edu.ifsp.dmo.ambientum.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.ambientum.databinding.ActivityLoginBinding
import br.edu.ifsp.dmo.ambientum.ui.main.MainActivity
import br.edu.ifsp.dmo.ambientum.ui.start.StartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        configListeners()
    }

    private fun configListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val pswd = binding.inputPassword.text.toString()

            firebaseAuth
                .signInWithEmailAndPassword(email, pswd)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val exception = task.exception
                        if(exception is FirebaseAuthException) {
                            when(exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> {
                                    Toast.makeText(this, "The email address is badly formatted.", Toast.LENGTH_SHORT).show()
                                }
                                "ERROR_INVALID_CREDENTIAL" -> {
                                    Toast.makeText(this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "An unexpected error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}