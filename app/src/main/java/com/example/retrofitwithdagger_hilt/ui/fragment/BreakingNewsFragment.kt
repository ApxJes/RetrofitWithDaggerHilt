package com.example.retrofitwithdagger_hilt.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwithdagger_hilt.R
import com.example.retrofitwithdagger_hilt.adapter.NewsAdapter
import com.example.retrofitwithdagger_hilt.databinding.FragmentBreakingNewsBinding
import com.example.retrofitwithdagger_hilt.util.Resource
import com.example.retrofitwithdagger_hilt.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var binding: FragmentBreakingNewsBinding

    val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        setUpRecyclerView()

        newsAdapter.onItemClickListener {
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner){responseType ->
            when(responseType){
                is Resource.Success -> {
                    responseType.data?.let { resultResponse ->
                        hidePaginationBar()
                        newsAdapter.differ.submitList(resultResponse.articles)
                    }
                }

                is Resource.Error -> {
                        hidePaginationBar()
                        Toast.makeText(
                            requireContext(),
                            "An error occur ${responseType.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                }

                is Resource.Loading -> showPaginationBar()
            }
        }
    }

    private fun setUpRecyclerView(){
        binding.rcvBreakingNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BreakingNewsFragment.newsAdapter
        }
    }

    private fun showPaginationBar(){
        binding.prgBreakingNewsBar.visibility = View.VISIBLE
    }

    private fun hidePaginationBar(){
        binding.prgBreakingNewsBar.visibility = View.GONE
    }
}