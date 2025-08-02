package ca.georgiancollege.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment1.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter(
   private val onEdit: (Movie) -> Unit,   // ✅ Correct
   private val onDelete: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {


   private var movieList: List<Movie> = emptyList()

   fun submitList(newList: List<Movie>) {
      movieList = newList
      notifyDataSetChanged()
   }

   inner class MovieViewHolder(val binding: ItemMovieBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(movie: Movie) {
         binding.title.text = movie.title
         binding.year.text = movie.year
         binding.rating.text = movie.rating
         Glide.with(binding.poster.context).load(movie.posterUrl).into(binding.poster)

         // Click to Edit
         binding.root.setOnClickListener {
            onEdit(movie)
         }

         // Long press to Delete
         binding.root.setOnLongClickListener {
            AlertDialog.Builder(binding.root.context)
               .setTitle("Delete Movie")
               .setMessage("Are you sure you want to delete \"${movie.title}\"?")
               .setPositiveButton("Delete") { _, _ -> onDelete(movie) }
               .setNegativeButton("Cancel", null)
               .show()
            true
         }
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
      val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MovieViewHolder(binding)
   }

   override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
      holder.bind(movieList[position])
   }

   override fun getItemCount(): Int = movieList.size
}


