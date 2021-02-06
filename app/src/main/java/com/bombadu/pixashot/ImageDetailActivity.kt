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
    private var id: Int = -1
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        getBundle()



        findViewById<TextInputEditText>(R.id.detail_name_edit_text).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    saveToLocalDatabase()
                    true
                }

                else -> false
            }
        }


    }

    private fun getBundle() {
        val bundle = intent.extras
        isEditing = bundle!!.getBoolean("editing")

        if(!isEditing) {
            url = bundle.getString("url", null).toString()
            Picasso.get().load(url).into(binding.detailImageView)
        } else {
            id = bundle.getInt("id")
            url = bundle.getString("url", null).toString()
            with(binding) {
                detailNameEditText.setText(bundle.getString("name"))
                detailCommentsEditText.setText(bundle.getString("comment"))
            }
            Picasso.get().load(url).into(binding.detailImageView)

        }

    }


    private fun saveToLocalDatabase() {
        val comment = binding.detailCommentsEditText.text.toString()
        val name = binding.detailNameEditText.text.toString()
        if(comment.isNotEmpty() || name.isNotEmpty()) {
            val newEntry = LocalData(url, comment, name)

            if (id != -1) {
                newEntry.id = id
            }

            viewModel.insertImageData(newEntry)
            finish()
            Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Missing Data", Toast.LENGTH_SHORT).show()
        }

    }
}