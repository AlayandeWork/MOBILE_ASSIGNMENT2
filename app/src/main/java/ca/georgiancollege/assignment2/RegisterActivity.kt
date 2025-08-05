package ca.georgiancollege.assignment2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


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

         if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show()
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
