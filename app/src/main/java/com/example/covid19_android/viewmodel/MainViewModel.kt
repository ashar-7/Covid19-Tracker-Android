package com.example.covid19_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid19_android.Resource
import com.example.covid19_android.model.CasesData
import com.example.covid19_android.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val repository = MainRepository.getInstance()
    private val allLiveData = MutableLiveData<Resource<CasesData>>()
    private val countriesLiveData = MutableLiveData<Resource<List<CasesData>>>()

    init { fetchAll() }

    fun fetchAll() = viewModelScope.launch {
        allLiveData.value = Resource.Loading()
        allLiveData.value = repository.getAll()

        countriesLiveData.value = Resource.Loading()
        countriesLiveData.value = repository.getCountriesData()
    }

    fun getAllLiveData(): LiveData<Resource<CasesData>> = allLiveData

    fun getCountriesData(): LiveData<Resource<List<CasesData>>> = countriesLiveData
}
