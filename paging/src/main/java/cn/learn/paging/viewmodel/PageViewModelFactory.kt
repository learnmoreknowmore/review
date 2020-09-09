package cn.learn.paging.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.learn.paging.datasource.Repository

/**
 * AndroidViewModelFactory
 */
class PageViewModelFactory(private val repository: Repository,
                           private val savedStateHandle: SavedStateHandle
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(repository, savedStateHandle) as T
    }
}