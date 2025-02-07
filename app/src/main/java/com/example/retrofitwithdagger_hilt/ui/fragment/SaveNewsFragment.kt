package com.example.retrofitwithdagger_hilt.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwithdagger_hilt.R
import com.example.retrofitwithdagger_hilt.adapter.NewsAdapter
import com.example.retrofitwithdagger_hilt.databinding.FragmentSaveNewsBinding
import com.example.retrofitwithdagger_hilt.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SaveNewsFragment : Fragment(R.layout.fragment_save_news) {

    private lateinit var binding: FragmentSaveNewsBinding
    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var adapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSaveNewsBinding.bind(view)
        setUpRecyclerView()

         val onItemTouchHelper = object: ItemTouchHelper.SimpleCallback(
             ItemTouchHelper.UP or ItemTouchHelper.DOWN,
             ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

         ) {
             override fun onMove(
                 recyclerView: RecyclerView,
                 viewHolder: RecyclerView.ViewHolder,
                 target: RecyclerView.ViewHolder
             ): Boolean {
                 return true
             }

             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 val position = viewHolder.layoutPosition
                 val article = adapter.differ.currentList[position]
                 viewModel.deleteNews(article)
                 Snackbar.make(view, "Successfully deleted", Snackbar.LENGTH_SHORT).apply {
                     setAction("Undo delete") {
                         viewModel.saveNews(article)
                     }
                     show()
                 }
             }
         }

        ItemTouchHelper(onItemTouchHelper).apply {
            attachToRecyclerView(binding.rcvSaveNews)
        }

        adapter.onItemClickListener {
            val action = SaveNewsFragmentDirections.actionSaveNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.getSaveNews().observe(viewLifecycleOwner){saveNewsResponse ->
            adapter.differ.submitList(saveNewsResponse)
        }
    }

    private fun setUpRecyclerView(){
        binding.rcvSaveNews.apply {
            adapter = this@SaveNewsFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}