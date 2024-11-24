package com.example.vinilos_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.VolleyError
import com.example.vinilos_app.models.CollectorDetail
import com.example.vinilos_app.repository.CollectorsRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(private val repository: CollectorsRepository) : ViewModel() {

    private val _collectorDetail = MutableLiveData<CollectorDetail>()
    val collectorDetail: LiveData<CollectorDetail> get() = _collectorDetail

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    /**
     * Carga los detalles de un colector específico por su ID.
     */
    fun loadCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            try {
                val detail = repository.getCollectorDetail(collectorId)
                _collectorDetail.postValue(detail)
            } catch (e: VolleyError) {
                _error.postValue("Error loading collector details: ${e.message}")
            } catch (e: Exception) {
                _error.postValue("Unexpected error: ${e.message}")
            }
        }
    }
}
