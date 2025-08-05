package ca.georgiancollege.assignment2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment2.databinding.ActivityModifyMoviesBinding
import com.google.firebase.firestore.FirebaseFirestore

class ModifyMoviesActivity : AppCompatActivity() {

   private lateinit var binding: ActivityModifyMoviesBinding
   private val db = FirebaseFirestore.getInstance()
   private var movieId: String? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityModifyMoviesBinding.inflate(layoutInflater)
      setContentView(binding.root)

      movieId = intent.getStringExtra("MOVIE_ID")

      if (movieId != null) {
         loadMovieList(movieId!!)
         "Update the Movie".also { binding.submitBtn.text = it }
      } else {
         "Add New Movie".also { binding.submitBtn.text = it }
      }

      binding.submitBtn.setOnClickListener {
         val title = binding.titleInput.text.toString().trim()
         val year = binding.yearInput.text.toString().trim()
         val poster = binding.posterUrlInput.text.toString().trim()
         val rating = binding.ratingInput.text.toString().trim()


         val movieMap = hashMapOf(
            "title" to title,
            "year" to year,
            "posterUrl" to poster,
            "rating" to rating
         )

         if (movieId != null) {
            db.collection("movies").document(movieId!!).set(movieMap)
               .addOnSuccessListener {
                  Toast.makeText(this, "The Movie has been updated", Toast.LENGTH_SHORT).show()
                  finish()
               }
         } else {
            db.collection("movies").add(movieMap)
               .addOnSuccessListener {
                  Toast.makeText(this, "A New Movie has been added", Toast.LENGTH_SHORT).show()
                  finish()
               }
         }
      }

      binding.cancelBtn.setOnClickListener {
         finish()
      }
   }

   private fun loadMovieList(id: String) {
      db.collection("movies").document(id).get()
         .addOnSuccessListener { doc ->
            binding.titleInput.setText(doc.getString("title"))
            binding.yearInput.setText(doc.getString("year"))
            binding.posterUrlInput.setText(doc.getString("posterUrl"))
            binding.ratingInput.setText(doc.getString("rating"))
         }
   }
}
