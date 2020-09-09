package cn.learn.paging.viewmodel

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.learn.paging.datasource.Repository

class PagingViewModelFactory(private val application: Application, private val repository: Repository,
                             private val savedStateHandle: SavedStateHandle
): ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(repository, savedStateHandle) as T
    }
}