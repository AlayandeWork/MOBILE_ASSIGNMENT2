package ca.georgiancollege.assignment1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import ca.georgiancollege.assignment1.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMovieDetailBinding
   private val viewModel: MovieDetailViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMovieDetailBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val imdbID = intent.getStringExtra("imdbID")
      if (imdbID == null) {
         Toast.makeText(this, "Missing movie ID", Toast.LENGTH_SHORT).show()
         finish()
         return
      }

      // Show loading layout initially
      binding.loadingLayout.visibility = View.VISIBLE
      binding.contentLayout.visibility = View.GONE

      viewModel.loadDetails(imdbID)

      viewModel.movieDetail.observe(this) { detail ->
         if (detail == null) {
            Toast.makeText(this, "Failed to load movie details", Toast.LENGTH_SHORT).show()
            finish()
         } else {
            binding.loadingLayout.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE

            binding.textTitle.text = detail.title
            binding.textDirector.text = "Director: ${detail.director}"
            binding.textYear.text = "Year: ${detail.year}"
            binding.textRating.text = "Rating: ${detail.rating}"
            binding.textPlot.text = detail.plot

            Glide.with(this)
               .load(detail.posterUrl)
               .placeholder(R.drawable.placeholder)
               .error(R.drawable.placeholder)
               .into(binding.imagePoster)
         }
      }

      binding.buttonBack.setOnClickListener {
         finish()
      }
   }
}
