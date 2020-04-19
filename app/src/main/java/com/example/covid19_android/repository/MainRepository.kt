package com.example.covid19_android.repository

import com.example.covid19_android.Resource
import com.example.covid19_android.model.CasesData
import com.example.covid19_android.retrofit.ApiService

class MainRepository(private val apiService: ApiService) {

    suspend fun getAll(): Resource<CasesData> {
        return try {
            Resource.Success(apiService.getAll())
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    suspend fun getCountriesData(): Resource<List<CasesData>> {
        return try {
            Resource.Success(apiService.getCountriesData().sortedBy { it.country })
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    companion object {
        @Volatile private var instance: MainRepository? = null

        fun getInstance(): MainRepository {
            return instance ?: synchronized(this) {
                instance ?: MainRepository(ApiService.create()).also { instance = it }
            }
        }
    }
}
