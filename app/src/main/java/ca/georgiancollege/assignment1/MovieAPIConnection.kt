package ca.georgiancollege.assignment1

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.CountDownLatch

object MovieAPIConnection {
   private const val API_KEY = "4cd69b7c"
   private val client = OkHttpClient()

   // Fetch full details for one movie (used for Recycler and Detail screen)
   private fun fetchFullDetails(imdbID: String, callback: (MovieDetail?) -> Unit) {
      val url = "https://www.omdbapi.com/?apikey=$API_KEY&i=$imdbID"
      val request = Request.Builder().url(url).build()

      client.newCall(request).enqueue(object : okhttp3.Callback {
         override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            callback(null)
         }

         override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            val body = response.body?.string() ?: return callback(null)
            try {
               val json = JSONObject(body)
               val detail = MovieDetail(
                  title = json.optString("Title"),
                  studio = json.optString("Production"),
                  year = json.optString("Year"),
                  rating = json.optString("imdbRating"),
                  director = json.optString("Director"),
                  plot = json.optString("Plot"),
                  posterUrl = json.optString("Poster")
               )
               callback(detail)
            } catch (e: Exception) {
               callback(null)
            }
         }
      })
   }

   // Used by Detail Screen (loads movie by ID)
   fun getMovieDetail(imdbID: String, callback: (MovieDetail?) -> Unit) {
      fetchFullDetails(imdbID, callback)
   }

   // Search and load extra fields (studio, rating, director)
   fun searchMovies(query: String, callback: (List<Movie>) -> Unit) {
      val url = "https://www.omdbapi.com/?apikey=$API_KEY&s=$query&type=movie"
      val request = Request.Builder().url(url).build()

      client.newCall(request).enqueue(object : okhttp3.Callback {
         override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
            callback(emptyList())
         }

         override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            val body = response.body?.string() ?: return callback(emptyList())
            try {
               val json = JSONObject(body)
               if (json.getString("Response") == "True") {
                  val searchArray = json.getJSONArray("Search")
                  val movieList = mutableListOf<Movie>()
                  val latch = CountDownLatch(searchArray.length())

                  for (i in 0 until searchArray.length()) {
                     val item = searchArray.getJSONObject(i)
                     val imdbID = item.optString("imdbID")

                     fetchFullDetails(imdbID) { detail ->
                        detail?.let {
                           val movie = Movie(
                              title = it.title,
                              year = it.year,
                              imdbID = imdbID,
                              type = item.optString("Type"),
                              posterUrl = it.posterUrl,
                              rating = it.rating,
                              director = it.director
                           )
                           movieList.add(movie)
                        }
                        latch.countDown()
                     }
                  }

                  Thread {
                     latch.await()
                     callback(movieList)
                  }.start()
               } else {
                  callback(emptyList())
               }
            } catch (e: Exception) {
               callback(emptyList())
            }
         }
      })
   }
}
