package com.bombadu.pixashot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.pixashot.Constants.GRID_SPAN_COUNT
import com.bombadu.pixashot.Constants.SEARCH_TIME_DELAY
import com.bombadu.pixashot.databinding.ActivityMain2Binding
import com.bombadu.pixashot.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

        viewModel = ViewModelProvider(this).get(ImageViewModel::class.java)

        setupRecyclerView()
        subscribeToObservers()

        var job : Job? = null
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
            bundle.putString("url_key", it)
            intent.putExtras(bundle)
            startActivity(intent)
        }


    }

    private fun subscribeToObservers() {
        viewModel.images.observe(viewLifecycleOwner, Observer {
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
        recyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
            setHasFixedSize(true)
        }
    }


}