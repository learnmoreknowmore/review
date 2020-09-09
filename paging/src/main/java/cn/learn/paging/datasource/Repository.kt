package cn.learn.paging.datasource

import androidx.paging.PagingData
import cn.learn.paging.vo.Post
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getPageList(keyword: String, pageSize: Int): Flow<PagingData<Post>>
    enum class Type {
        IN_MEMORY_BY_ITEM,
        IN_MEMORY_BY_PAGE,
        DB
    }
}