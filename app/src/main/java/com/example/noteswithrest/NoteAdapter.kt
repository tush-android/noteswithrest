package com.example.noteswithrest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class NoteAdapter(private val contex:Context):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var notes:MutableList<Note> = mutableListOf()
    private var fulllist:MutableList<Note> = mutableListOf()

    class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<TextView>(R.id.titleTextHeading)
        val content = itemView.findViewById<TextView>(R.id.content)
        val upd = itemView.findViewById<ImageView>(R.id.updateButton)
        val d = itemView.findViewById<ImageView>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content
        holder.upd.setOnClickListener {
            val intent=Intent(contex,update::class.java).apply {
                putExtra("id",note.id)
                putExtra("title",note.title)
                putExtra("content",note.content)
                putExtra("time",note.time)
            }
            contex.startActivity(intent)
        }
        holder.d.setOnClickListener {
                AlertDialog.Builder(contex).apply {
                    setTitle("Delete Note")
                    setMessage("Are You Sure You Want To Delete This Note?")
                    setPositiveButton("Yes"){ _, _ -> deletenote(note.id,position)
                    }
                    setNeutralButton("No",null)
                    show()
                }
        }

    }

    private fun deletenote(id: Int, position: Int) {
        RetrofitClient.instance.deletenote(id).enqueue(object :Callback<ResponceModel>{
            override fun onResponse(call: Call<ResponceModel>, response: Response<ResponceModel>) {
                if(response.isSuccessful && response.body()?.success == true){
                    Toast.makeText(contex,"Note Deleted Successfully...!",Toast.LENGTH_LONG).show()
                    val deletedNote = notes[position]
                    notes.removeAt(position)
                    //fulllist.removeIf { it.id == deletedNote.id }
                    val iterator = fulllist.iterator()
                    while (iterator.hasNext()) {
                        if (iterator.next().id == deletedNote.id) {
                            iterator.remove()
                        }
                    }
                    notifyItemRemoved(position)
                }
                else{
                    Toast.makeText(contex, "Error!!!!!!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponceModel>, t: Throwable) {
                Toast.makeText(contex, "Error:${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun getItemCount(): Int=notes.size
    fun updateNotes(newNotes: List<Note>) {
        fulllist.clear()
        fulllist.addAll(newNotes)
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        notes.clear()
        if (query.isEmpty()) {
            notes.addAll(fulllist)
        } else {
            val lowerCaseQuery = query.lowercase()
            notes.addAll(fulllist.filter {
                it.title.lowercase().contains(lowerCaseQuery) ||
                        it.content.lowercase().contains(lowerCaseQuery)
            })
        }
        notifyDataSetChanged()
    }

}