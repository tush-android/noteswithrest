package com.example.noteswithrest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noteswithrest.databinding.ActivityAddnoteactivityBinding
import com.example.noteswithrest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Addnoteactivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddnoteactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddnoteactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val content = binding.contentEditText.text.toString().trim()
            val time = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val call = RetrofitClient.instance.insertnote(title, content, time)
                call.enqueue(object : Callback<ResponceModel> {
                    override fun onResponse(call: Call<ResponceModel>, response: Response<ResponceModel>) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(this@Addnoteactivity, "Note Inserted Successfully", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            Toast.makeText(this@Addnoteactivity, "Insert Failed!", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponceModel>, t: Throwable) {
                        Toast.makeText(this@Addnoteactivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            } else {
                Toast.makeText(this@Addnoteactivity, "Please fill both Title and Content!", Toast.LENGTH_LONG).show()
            }
        }
    }
}