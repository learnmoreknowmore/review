package cn.learn.paging.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.paging.PagingData
import cn.learn.paging.datasource.Repository
import cn.learn.paging.vo.Post
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class PageViewModel(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        const val KEY_SUBREDDIT = "subreddit"
        const val DEFAULT_SUBREDDIT = "androiddev"
    }

    init {
        if (!savedStateHandle.contains(KEY_SUBREDDIT)) {
            savedStateHandle.set(KEY_SUBREDDIT, DEFAULT_SUBREDDIT)
        }
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)
    val posts = flowOf(
        clearListCh.consumeAsFlow().map { PagingData.empty<Post>() },
        savedStateHandle.getLiveData<String>(KEY_SUBREDDIT)
            .asFlow()
            .flatMapLatest { repository.getPageList(it, 30) }
    ).flattenMerge(2)

    private fun shouldShowSubreddit(
        subreddit: String
    ) = savedStateHandle.get<String>(KEY_SUBREDDIT) != subreddit

    fun showSubreddit(subreddit: String) {
        if (!shouldShowSubreddit(subreddit)) return

        clearListCh.offer(Unit)

        savedStateHandle.set(KEY_SUBREDDIT, subreddit)
    }
}