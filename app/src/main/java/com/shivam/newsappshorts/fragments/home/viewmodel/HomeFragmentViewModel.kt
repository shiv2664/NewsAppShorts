package com.shivam.newsappshorts.fragments.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.shivam.newsappshorts.repository.MyNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: MyNewsRepository): ViewModel() {

    fun getData(apiKey:String,query: String) = repository.getData(apiKey,query).cachedIn(viewModelScope)

}