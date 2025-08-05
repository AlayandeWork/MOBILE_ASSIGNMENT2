package ca.georgiancollege.assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth


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

         if (email.isEmpty()) {
            Toast.makeText(this, "Email address cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
               if (task.isSuccessful) {
                  Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show()
                  startActivity(Intent(this, MainActivity::class.java))
                  finish()
               } else {
                  AlertDialog.Builder(this)
                     .setTitle("Failed")
                     .setMessage(task.exception?.message ?: "There was an unknown error")
                     .setPositiveButton("Success", null)
                     .show()
               }
            }
      }

      binding.goToRegisterBtn.setOnClickListener {
         startActivity(Intent(this, RegisterActivity::class.java))
      }
   }
}
