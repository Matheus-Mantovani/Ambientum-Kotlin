package br.edu.ifsp.dmo.ambientum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.ambientum.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(firebaseAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        configListeners()
    }

    private fun configListeners() {
        binding.btnSignup.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val pswd = binding.inputPassword.text.toString()
            val cpswd = binding.inputConfirmPassword.text.toString()

            if(validSignUp(email, pswd, cpswd)) {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, pswd)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this, "Your account has been created.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            val exception = task.exception
                            if(exception is FirebaseAuthException) {
                                when(exception.errorCode) {
                                    "ERROR_INVALID_EMAIL" -> {
                                        Toast.makeText(this, "The email address is badly formatted.", Toast.LENGTH_SHORT).show()
                                    }
                                    "ERROR_WEAK_PASSWORD" -> {
                                        Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                                    }
                                    "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                        Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(this, "An error occurred while creating your account. Please try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "An unexpected error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }

    private fun validSignUp(email: String, pswd: String, cpswd: String): Boolean {
        if(email.isBlank() || pswd.isBlank() || cpswd.isBlank()) {
            Toast.makeText(this, "Make sure to fill all fields.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(pswd != cpswd) {
            Toast.makeText(this, "The passwords do not match.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}