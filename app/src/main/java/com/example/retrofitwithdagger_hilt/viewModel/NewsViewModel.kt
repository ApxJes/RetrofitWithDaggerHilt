package com.example.retrofitwithdagger_hilt.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitwithdagger_hilt.model.Article
import com.example.retrofitwithdagger_hilt.model.NewsResponse
import com.example.retrofitwithdagger_hilt.repository.NewsRepository
import com.example.retrofitwithdagger_hilt.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel
@Inject constructor(
    private val repository: NewsRepository
): ViewModel(){
    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPageSize = 1

    var searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPageSize = 1

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, breakingNewsPageSize)
        breakingNews.postValue(handleBreakingNews(response))
    }

    private fun handleBreakingNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.getSearchNews(searchQuery, searchNewsPageSize)
        searchNews.postValue(handleSearchNews(response))
    }

    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveNews(article: Article) = viewModelScope.launch {
        repository.saveNews(article)
    }

    fun getSaveNews() = repository.getSaveNews()


    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }
}