package com.bombadu.pixashot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bombadu.pixashot.Constants.GRID_SPAN_COUNT
import com.bombadu.pixashot.Constants.SEARCH_TIME_DELAY
import com.bombadu.pixashot.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ImageViewModel
    private val imageAdapter: ImageAdapter = ImageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        setupRecyclerView()
        subscribeToObservers()

        var job : Job? = null
        binding.imageSearchEditText.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener {
            Log.d("IMAGE_URL", it)
            Toast.makeText(this, "URL: $it", Toast.LENGTH_SHORT).show()
        }


    }

    private fun subscribeToObservers() {
        viewModel.images.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when(result.status) {
                    Status.SUCCESS -> {
                        val urls = result.data?.hits?.map { imageResult -> imageResult.previewURL }
                        imageAdapter.images = urls ?: listOf()
                    }

                    Status.ERROR -> {
                        Log.e("ERROR", "An Unknown error occurred")
                    }

                    Status.LOADING -> {
                        Log.e("ERROR", "An Unknown error occurred")
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {

        binding.recyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(applicationContext, GRID_SPAN_COUNT)
            setHasFixedSize(true)
        }

    }
}