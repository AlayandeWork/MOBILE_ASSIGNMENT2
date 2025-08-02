package ca.georgiancollege.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ca.georgiancollege.assignment1.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

// FILE: LoginActivity.kt
class LoginActivity : AppCompatActivity() {

   private lateinit var binding: ActivityLoginBinding
   private lateinit var auth: FirebaseAuth

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)

      auth = FirebaseAuth.getInstance()

      binding.loginBtn.setOnClickListener {
         val email = binding.emailInput.text.toString().trim()
         val password = binding.passwordInput.text.toString().trim()

         if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
               if (task.isSuccessful) {
                  Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                  startActivity(Intent(this, MovieListActivity::class.java))
                  finish()
               } else {
                  AlertDialog.Builder(this)
                     .setTitle("Login Failed")
                     .setMessage(task.exception?.message ?: "Unknown error")
                     .setPositiveButton("OK", null)
                     .show()
               }
            }
      }

      binding.goToRegisterBtn.setOnClickListener {
         startActivity(Intent(this, RegisterActivity::class.java))
      }
   }
}
