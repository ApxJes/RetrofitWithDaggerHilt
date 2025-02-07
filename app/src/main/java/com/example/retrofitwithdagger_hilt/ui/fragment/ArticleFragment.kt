package com.example.retrofitwithdagger_hilt.ui.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.retrofitwithdagger_hilt.R
import com.example.retrofitwithdagger_hilt.adapter.NewsAdapter
import com.example.retrofitwithdagger_hilt.databinding.FragmentArticleBinding
import com.example.retrofitwithdagger_hilt.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    val args: ArticleFragmentArgs by navArgs()

    private val viewModel: NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        val article = args.article
        article.url?.let {
            binding.webView.apply {
                webViewClient = WebViewClient()
                loadUrl(it)
            }
        }

        binding.fltBtnSave.setOnClickListener {
            viewModel.saveNews(article)
            Snackbar.make(
                view,
                "Successfully saved",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}