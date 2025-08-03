package ca.georgiancollege.assignment1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ca.georgiancollege.assignment1.databinding.ActivityAddEditMovieBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddEditMovieActivity : AppCompatActivity() {

   private lateinit var binding: ActivityAddEditMovieBinding
   private val db = FirebaseFirestore.getInstance()
   private var movieId: String? = null

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityAddEditMovieBinding.inflate(layoutInflater)
      setContentView(binding.root)

      movieId = intent.getStringExtra("MOVIE_ID")

      if (movieId != null) {
         title = "Edit Movie"
         loadMovieData(movieId!!)
         binding.submitBtn.text = "Update"
      } else {
         title = "Add Movie"
         binding.submitBtn.text = "Add"
      }

      binding.submitBtn.setOnClickListener {
         val title = binding.titleInput.text.toString().trim()
         val year = binding.yearInput.text.toString().trim()
         val poster = binding.posterUrlInput.text.toString().trim()
         val rating = binding.ratingInput.text.toString().trim()

         if (title.isEmpty() || year.isEmpty()) {
            Toast.makeText(this, "Please enter the title and year", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }

         val movieMap = hashMapOf(
            "title" to title,
            "year" to year,
            "posterUrl" to poster,
            "rating" to rating
         )

         if (movieId != null) {
            db.collection("movies").document(movieId!!).set(movieMap)
               .addOnSuccessListener {
                  Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show()
                  finish()
               }
         } else {
            db.collection("movies").add(movieMap)
               .addOnSuccessListener {
                  Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show()
                  finish()
               }
         }
      }

      binding.cancelBtn.setOnClickListener {
         finish()
      }
   }

   private fun loadMovieData(id: String) {
      db.collection("movies").document(id).get()
         .addOnSuccessListener { doc ->
            binding.titleInput.setText(doc.getString("title"))
            binding.yearInput.setText(doc.getString("year"))
            binding.posterUrlInput.setText(doc.getString("posterUrl"))
            binding.ratingInput.setText(doc.getString("rating"))
         }
   }
}
