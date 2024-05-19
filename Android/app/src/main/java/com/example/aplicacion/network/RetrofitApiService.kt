package com.example.aplicacion.network

import ApiService
import com.example.aplicacion.model.Task

class RetrofitApiService {
    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    suspend fun getTasks(): List<Task> {
        return try {
            val response = apiService.getTasks()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createTask(task: Task): Task? {
        return try {
            val response = apiService.createTask(task)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateTask(id: Long, task: Task): Task? {
        return try {
            val response = apiService.updateTask(id, task)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteTask(id: Long): Boolean {
        return try {
            val response = apiService.deleteTask(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
