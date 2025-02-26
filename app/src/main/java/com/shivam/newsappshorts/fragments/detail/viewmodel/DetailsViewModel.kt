package com.shivam.newsappshorts.fragments.detail.viewmodel

import androidx.lifecycle.ViewModel
import com.shivam.newsappshorts.repository.MyNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel@Inject constructor(private val repository: MyNewsRepository): ViewModel() {

}