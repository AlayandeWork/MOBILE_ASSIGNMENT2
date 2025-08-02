package ca.georgiancollege.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ca.georgiancollege.assignment1.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

// FILE: RegisterActivity.kt
class RegisterActivity : AppCompatActivity() {

   private lateinit var binding: ActivityRegisterBinding
   private lateinit var auth: FirebaseAuth

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityRegisterBinding.inflate(layoutInflater)
      setContentView(binding.root)

      auth = FirebaseAuth.getInstance()

      binding.registerBtn.setOnClickListener {
         val email = binding.emailInput.text.toString().trim()
         val password = binding.passwordInput.text.toString().trim()

         if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
               if (task.isSuccessful) {
                  Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                  startActivity(Intent(this, LoginActivity::class.java))
                  finish()
               } else {
                  Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
               }
            }
      }

      binding.cancelBtn.setOnClickListener {
         startActivity(Intent(this, LoginActivity::class.java))
         finish()
      }
   }
}
