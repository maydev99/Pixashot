package com.bombadu.pixashot.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.pixashot.R
import com.bombadu.pixashot.Status
import com.bombadu.pixashot.ui.adapters.ImageAdapter
import com.bombadu.pixashot.util.Constants.GRID_SPAN_COUNT
import com.bombadu.pixashot.util.Constants.SEARCH_TIME_DELAY
import com.bombadu.pixashot.viewmodel.ImageViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var viewModel: ImageViewModel
    private val imageAdapter: ImageAdapter = ImageAdapter()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageSearchEditText = view.findViewById<TextInputEditText>(R.id.image_search_edit_text)
        recyclerView = view.findViewById(R.id.recycler_view)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        setupRecyclerView()
        subscribeToObservers()


        var job: Job? = null
        imageSearchEditText.addTextChangedListener { editable ->
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
            //Log.d("IMAGE_URL", it)
            //Toast.makeText(this, "URL: $it", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            val intent = Intent(view.context, ImageDetailActivity::class.java)
            bundle.putString("url", it)
            bundle.putBoolean("editing", false)
            intent.putExtras(bundle)
            startActivity(intent)
        }


    }

    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner, {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val urls =
                            result.data?.hits?.map { imageResult -> imageResult.largeImageURL }
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
        recyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
            setHasFixedSize(true)
        }
    }


}