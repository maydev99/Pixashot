package com.bombadu.pixashot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment: Fragment(), SavedImagesAdapter.ItemClickListener {
    // TODO: Rename and change types of parameters
    private lateinit var adapter: SavedImagesAdapter
    lateinit var viewModel: ImageViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
        adapter = SavedImagesAdapter(this)
        observeTheData()
    }

    private fun observeTheData() {
        val noSavedImagesDialog = view?.findViewById<TextView>(R.id.no_saved_images_text_view)
        viewModel.savedData.observe(viewLifecycleOwner,
            { list->
                list.let {
                    if (it.isNullOrEmpty()) {
                        noSavedImagesDialog?.visibility = View.VISIBLE
                    } else {
                        noSavedImagesDialog?.visibility = View.INVISIBLE
                        adapter.submitList(it)
                    }

                }
            })

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.saved_recycler_view)
        val manager = LinearLayoutManager(requireContext())
        recyclerView!!.layoutManager = manager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.getItemAt(viewHolder.adapterPosition)
                    .let { viewModel.deleteImage(it) }
                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)
    }



    override fun onItemClick(position: Int) {
        val imageData = adapter.getItemAt(position)
        val bundle = Bundle()
        val intent = Intent(requireContext(), ImageDetailActivity::class.java)
        with(bundle) {
            putInt("id", imageData.id!!)
            putString("url", imageData.url)
            putString("comment", imageData.comments)
            putString("name", imageData.name)
            putBoolean("editing", true)
        }
        intent.putExtras(bundle)
        startActivity(intent)

    }
}