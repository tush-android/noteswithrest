package com.example.noteswithrest

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noteswithrest.databinding.ActivityMainBinding
import com.example.noteswithrest.databinding.ActivityUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class update : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var noteId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteId = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        val time=intent.getStringExtra("time")

        if(noteId !=-1 && title != null && content != null){
            binding.updatetitleEditText.setText(title)
            binding.updatecontentEditText.setText(content)
        }
        binding.updatesaveButton.setOnClickListener {
            val updatetitle=binding.updatetitleEditText.text.toString().trim()
            val updatecontent=binding.updatecontentEditText.text.toString().trim()
            val time=java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss",java.util.Locale.getDefault()).format(java.util.Date())
            if(updatetitle.isNotEmpty() && updatecontent.isNotEmpty()){
                if(noteId != -1){
                    RetrofitClient.instance.updatenotes(noteId!!,updatetitle,updatecontent,time).enqueue(object :Callback<ResponceModel>{
                        override fun onResponse(
                            call: Call<ResponceModel>,
                            response: Response<ResponceModel>
                        ) {
                            if(response.isSuccessful && response.body() ?.success == true){
                                Toast.makeText(this@update, "Note Updated", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else
                            {
                                Toast.makeText(this@update, "Error in updating data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<ResponceModel>, t: Throwable) {
                            Toast.makeText(this@update, "Error:${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            else{
                Toast.makeText(this@update, "please Fill The Data First....!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}