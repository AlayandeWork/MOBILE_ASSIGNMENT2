package ca.georgiancollege.assignment1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment1.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private val viewModel: MovieDisplayModel by viewModels()
   private lateinit var adapter: MovieAdapter
   private val db = FirebaseFirestore.getInstance()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      adapter = MovieAdapter(
         onEdit = { movie ->
            val intent = Intent(this, ModifyMoviesActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
         },
         onDelete = { movie ->
            db.collection("movies").document(movie.id).delete()
               .addOnSuccessListener {
                  Toast.makeText(this, "Deleted ${movie.title}", Toast.LENGTH_SHORT).show()
                  viewModel.fetchMovies()
               }
               .addOnFailureListener {
                  Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show()
               }
         }
      )

      binding.recyclerView.layoutManager = LinearLayoutManager(this)
      binding.recyclerView.adapter = adapter

      binding.addMovieBtn.setOnClickListener {
         startActivity(Intent(this, ModifyMoviesActivity::class.java))
      }

      binding.buttonLogout.setOnClickListener {
         FirebaseAuth.getInstance().signOut()

         val intent = Intent(this, LoginActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         startActivity(intent)
         finish()
      }


      viewModel.movies.observe(this) {
         adapter.submitList(it)
      }

      viewModel.fetchMovies()
   }

   override fun onResume() {
      super.onResume()
      viewModel.fetchMovies()
   }


}
