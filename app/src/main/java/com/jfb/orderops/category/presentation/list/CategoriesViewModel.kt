package com.jfb.orderops.category.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jfb.orderops.category.domain.model.Category
import com.jfb.orderops.category.domain.usecase.ListCategoriesUseCase
import com.jfb.orderops.core.result.AppResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.jfb.orderops.category.domain.usecase.UpdateCategoryUseCase

class CategoriesViewModel(
    private val listCategoriesUseCase: ListCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (val result = listCategoriesUseCase.execute()) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            categories = result.data,
                            isLoading = false
                        )
                    }
                }

                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is AppResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun openEditDialog(category: Category) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                editName = category.name,
                isEditDialogVisible = true
            )
        }
    }

    fun closeEditDialog() {
        _uiState.update {
            it.copy(
                isEditDialogVisible = false,
                selectedCategory = null,
                editName = ""
            )
        }
    }

    fun onEditNameChange(value: String) {
        _uiState.update {
            it.copy(editName = value)
        }
    }

    fun updateCategory() {

        val category = _uiState.value.selectedCategory
            ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            when (
                val result = updateCategoryUseCase.execute(
                    id = category.id,
                    name = _uiState.value.editName
                )
            ) {

                is AppResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditDialogVisible = false,
                            selectedCategory = null,
                            editName = ""
                        )
                    }

                    loadCategories()
                }

                is AppResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }

                is AppResult.Loading -> {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                }
            }
        }
    }
}