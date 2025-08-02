package ca.georgiancollege.assignment1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.georgiancollege.assignment1.Movie
import ca.georgiancollege.assignment1.MovieAPIConnection

class MainViewModel : ViewModel() {
   private val _movies = MutableLiveData<List<Movie>>()
   val movies: LiveData<List<Movie>> get() = _movies

   fun search(query: String) {
      MovieAPIConnection.searchMovies(query) { results ->
         _movies.postValue(results)
      }
   }
}