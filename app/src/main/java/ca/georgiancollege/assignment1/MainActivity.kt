package ca.georgiancollege.assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ca.georgiancollege.assignment1.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private val viewModel: MainViewModel by viewModels()
   private lateinit var adapter: MovieAdapter
   private val db = FirebaseFirestore.getInstance()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)


      adapter = MovieAdapter(
         onEdit = { movie ->
            val intent = Intent(this, AddEditMovieActivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            startActivity(intent)
         },
         onDelete = { movie ->
            db.collection("movies").document(movie.id).delete()
               .addOnSuccessListener {
                  Toast.makeText(this, "Deleted ${movie.title}", Toast.LENGTH_SHORT).show()
                  viewModel.search(binding.editSearch.text.toString().trim())
               }
               .addOnFailureListener {
                  Toast.makeText(this, "Failed to delete movie", Toast.LENGTH_SHORT).show()
               }
         }
      )

      binding.recyclerView.layoutManager = LinearLayoutManager(this)
      binding.recyclerView.adapter = adapter


      binding.buttonSearch.setOnClickListener {
         val query = binding.editSearch.text.toString().trim()
         if (query.isNotEmpty()) {
            viewModel.search(query)


            binding.recyclerView.visibility = View.VISIBLE
            binding.buttonClear.visibility = View.VISIBLE
            binding.textNoResults.visibility = View.GONE

            binding.editSearch.animate().translationY(-300f).setDuration(300).start()
            binding.buttonSearch.animate().translationY(-300f).setDuration(300).start()
         } else {
            Toast.makeText(this, "Enter a search term", Toast.LENGTH_SHORT).show()
         }
      }

      binding.buttonClear.setOnClickListener {
         binding.editSearch.setText("")
         adapter.submitList(emptyList())
         binding.recyclerView.visibility = View.GONE
         binding.buttonClear.visibility = View.GONE
         binding.textNoResults.visibility = View.GONE

         binding.editSearch.animate().translationY(0f).setDuration(300).start()
         binding.buttonSearch.animate().translationY(0f).setDuration(300).start()
      }


      viewModel.movies.observe(this) { movies ->
         adapter.submitList(movies)
         if (movies.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.textNoResults.visibility = View.VISIBLE
         } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.textNoResults.visibility = View.GONE
         }
      }
   }

   override fun onResume() {
      super.onResume()
      val query = binding.editSearch.text.toString().trim()
      if (query.isNotEmpty()) {
         viewModel.search(query)
      }
   }
}
