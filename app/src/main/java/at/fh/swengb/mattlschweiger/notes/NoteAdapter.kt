package at.fh.swengb.mattlschweiger.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter(val clickListener: (note: Note) -> Unit): RecyclerView.Adapter<NoteViewHolder>()
{
    private var notesList = listOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val noteItemView = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(noteItemView, clickListener)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int)
    {
        holder.bindItem(notesList[position])
    }

    fun updateList(newlist: List<Note>)
    {
        notesList = newlist
        notifyDataSetChanged()
    }
}

class NoteViewHolder(itemView: View, val clickListener: (note: Note) -> Unit): RecyclerView.ViewHolder(itemView)
{

    fun bindItem(note: Note) {
        itemView.note_textView_title.text = note.title
        itemView.note_textView_text.text = note.text
        itemView.setOnClickListener() {
            clickListener(note)
        }
    }
}