package com.example.retrofitwithdagger_hilt.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitwithdagger_hilt.R
import com.example.retrofitwithdagger_hilt.adapter.NewsAdapter
import com.example.retrofitwithdagger_hilt.databinding.FragmentSearchNewsBinding
import com.example.retrofitwithdagger_hilt.util.Resource
import com.example.retrofitwithdagger_hilt.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var binding: FragmentSearchNewsBinding

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var adapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchNewsBinding.bind(view)
        setUpRecyclerView()

        var job: Job? = null
        binding.edtSearchNews.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(300L)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }

        adapter.onItemClickListener { article ->
            val action = SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(article)
            findNavController().navigate(action)
        }

        viewModel.searchNews.observe(viewLifecycleOwner) { searchNewsResult ->
            when (searchNewsResult) {
                is Resource.Success -> {
                    searchNewsResult.data?.let { resultResponse ->
                        hidePaginationBar()
                        adapter.differ.submitList(resultResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hidePaginationBar()
                    Toast.makeText(
                        requireContext(),
                        "An error occur ${searchNewsResult.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> showPaginationBar()
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvSearchNews.apply {
            adapter = this@SearchNewsFragment.adapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun showPaginationBar() {
        binding.prgSearchNewsBar.visibility = View.VISIBLE
    }

    private fun hidePaginationBar() {
        binding.prgSearchNewsBar.visibility = View.GONE
    }
}