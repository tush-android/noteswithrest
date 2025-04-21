package com.example.noteswithrest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteswithrest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private var noteList = mutableListOf<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        fetchnotes()
        setContentView(binding.root)
        binding.addbutton.setOnClickListener {
            val intent=Intent(this@MainActivity,Addnoteactivity::class.java)
            startActivity(intent)
        }
        binding.s1.addTextChangedListener {
            val query=it.toString()
            noteAdapter.filter(query)
        }
        binding.l1.layoutManager=LinearLayoutManager(this@MainActivity)
        fetchnotes()
    }
    override fun onResume() {
        super.onResume()
        fetchnotes()  // Refresh list when returning from EditActivity
    }

    private fun fetchnotes() {
        RetrofitClient.instance.getallnotes().enqueue(object :Callback<List<Note>>{
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if(response.isSuccessful){
                    val notes=response.body() ?: emptyList()
                    noteAdapter = NoteAdapter(this@MainActivity)
                    binding.l1.adapter = noteAdapter
                    noteAdapter.updateNotes(notes.toMutableList())
                }
                else
                {
                    Toast.makeText(this@MainActivity, "Failure", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failure: ${t.message}", Toast.LENGTH_LONG).show()
            }

        })
    }
}