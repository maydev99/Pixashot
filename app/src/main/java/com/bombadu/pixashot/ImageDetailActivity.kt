package com.bombadu.pixashot

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bombadu.pixashot.databinding.ActivityImageDetailBinding
import com.bombadu.pixashot.local.LocalData
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ImageViewModel

    private lateinit var binding: ActivityImageDetailBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        val bundle = intent.extras
        url = bundle?.getString("url_key", null).toString()
        Picasso.get().load(url).into(binding.detailImageView)

        findViewById<TextInputEditText>(R.id.detail_name_edit_text).setOnEditorActionListener { v, actionId, keyEvent ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    saveToLocalDatabase()
                    true
                }

                else -> false
            }
        }


    }

    private fun saveToLocalDatabase() {
        val comment = binding.detailCommentsEditText.text.toString()
        val name = binding.detailNameEditText.text.toString()
        if(comment.isNotEmpty() || name.isNotEmpty()) {
            val newEntry = LocalData(url, comment, name)
            viewModel.insertImageData(newEntry)
            finish()
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Missing Data", Toast.LENGTH_SHORT).show()
        }

    }
}