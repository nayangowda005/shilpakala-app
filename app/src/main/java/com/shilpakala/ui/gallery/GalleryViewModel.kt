package com.shilpakala.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shilpakala.data.local.ShilpaKalaDatabase
import com.shilpakala.data.local.entity.PhotoEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class GalleryUiState(
    val photos: List<PhotoEntity> = emptyList(),
    val isLoading: Boolean = true
)

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ShilpaKalaDatabase.getDatabase(application)
    private val photoDao = db.photoDao()

    private val _uiState = MutableStateFlow(GalleryUiState())
    val uiState: StateFlow<GalleryUiState> = _uiState

    init {
        loadPhotos()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            photoDao.getAllPhotos().collect { photos ->
                _uiState.value = _uiState.value.copy(
                    photos = photos,
                    isLoading = false
                )
            }
        }
    }

    fun deletePhoto(id: Int) {
        viewModelScope.launch {
            photoDao.deletePhoto(id)
        }
    }
}