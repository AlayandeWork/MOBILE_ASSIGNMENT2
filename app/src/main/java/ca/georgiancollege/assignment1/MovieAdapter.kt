package ca.georgiancollege.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment1.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter(
   private val onEdit: (Movie) -> Unit,
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
         binding.year.text = "Year: ${movie.year}"
         binding.rating.text = "Rating: ${movie.rating}"

         Glide.with(binding.poster.context)
            .load(movie.posterUrl)
            .into(binding.poster)

         // Edit button click
         binding.buttonEdit.setOnClickListener {
            onEdit(movie)
         }

         // Delete button click
         binding.buttonDelete.setOnClickListener {
            AlertDialog.Builder(binding.root.context)
               .setTitle("Delete Movie")
               .setMessage("Are you sure you want to delete \"${movie.title}\"?")
               .setPositiveButton("Delete") { _, _ ->
                  onDelete(movie)
               }
               .setNegativeButton("Cancel", null)
               .show()
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
