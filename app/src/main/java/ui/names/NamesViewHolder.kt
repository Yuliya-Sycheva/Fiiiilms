package ui.names

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itproger.spr_15_clean_architecture_films.R
import domain.models.Person

class NamesViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_person, parent, false)) {

    var cover: ImageView = itemView.findViewById(R.id.cover)
    var name: TextView = itemView.findViewById(R.id.name)
    var description: TextView = itemView.findViewById(R.id.description)

    fun bind(person: Person) {
        Glide.with(itemView)
            .load(person.image)
            .circleCrop()
            .into(cover)

        name.text = person.title
        description.text = person.description
    }
}