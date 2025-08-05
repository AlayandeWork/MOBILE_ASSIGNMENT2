package ca.georgiancollege.assignment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import ca.georgiancollege.assignment1.databinding.MovieItemsBinding
import com.bumptech.glide.Glide

class MovieAdapter(
   private val onEdit: (Movie) -> Unit,
   private val onDelete: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

   private var movieList: List<Movie> = emptyList()

   inner class MovieViewHolder(val binding: MovieItemsBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(movie: Movie) {
         binding.title.text = movie.title
         "Year: ${movie.year}".also { binding.year.text = it }
         "Rating: ${movie.rating}".also { binding.rating.text = it }

         Glide.with(binding.poster.context)
            .load(movie.posterUrl)
            .into(binding.poster)



         binding.buttonEdit.setOnClickListener {
            onEdit(movie)
         }


         binding.buttonDelete.setOnClickListener {
            AlertDialog.Builder(binding.root.context)
               .setTitle("Delete Movie")
               .setMessage("Are you sure you want to delete \"${movie.title}\"?")
               .setPositiveButton("Yes") { _, _ ->
                  onDelete(movie)
               }
               .setNegativeButton("No", null)
               .show()
         }
      }
   }

   fun submitList(newList: List<Movie>) {
      movieList = newList
      notifyDataSetChanged()
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
      val binding = MovieItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MovieViewHolder(binding)
   }

   override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
      holder.bind(movieList[position])
   }

   override fun getItemCount(): Int = movieList.size
}
