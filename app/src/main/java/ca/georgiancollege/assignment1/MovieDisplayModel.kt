package ca.georgiancollege.assignment1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class MovieDisplayModel : ViewModel() {
   private val db = FirebaseFirestore.getInstance()
   private val _movies = MutableLiveData<List<Movie>>()
   val movies: LiveData<List<Movie>> = _movies

   fun fetchMovies() {
      db.collection("movies")
         .get()
         .addOnSuccessListener { result ->
            val movieList = result.map { doc ->
               Movie(
                  id = doc.id,
                  title = doc.getString("title") ?: "",
                  year = doc.getString("year") ?: "",
                  posterUrl = doc.getString("posterUrl") ?: "",
                  rating = doc.getString("rating") ?: ""
               )
            }
            _movies.value = movieList
         }
         .addOnFailureListener {
            _movies.value = emptyList()
         }
   }
}
