package ca.georgiancollege.assignment1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ca.georgiancollege.assignment1.MovieDetail
import ca.georgiancollege.assignment1.MovieAPIConnection

class MovieDetailViewModel : ViewModel() {
   private val _movieDetail = MutableLiveData<MovieDetail?>()
   val movieDetail: LiveData<MovieDetail?> get() = _movieDetail

   fun loadDetails(imdbID: String) {
      MovieAPIConnection.getMovieDetail(imdbID) {
         _movieDetail.postValue(it)
      }
   }
}

