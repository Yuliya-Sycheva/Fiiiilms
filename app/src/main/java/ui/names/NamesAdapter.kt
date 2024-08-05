package ui.names

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import domain.models.Movie
import domain.models.Person
import ui.movies.MovieViewHolder

class NamesAdapter () : RecyclerView.Adapter<NamesViewHolder>() {

    var persons = ArrayList<Person>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamesViewHolder =
        NamesViewHolder(parent)

    override fun onBindViewHolder(holder: NamesViewHolder, position: Int) {
        holder.bind(persons.get(position))
    }

    override fun getItemCount(): Int = persons.size
}