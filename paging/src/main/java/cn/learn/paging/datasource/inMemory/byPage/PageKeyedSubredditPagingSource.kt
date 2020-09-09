/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.learn.paging.datasource.inMemory.byPage

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Prepend
import androidx.paging.PagingSource.LoadResult.Page
import cn.learn.paging.api.RedditApi
import cn.learn.paging.datasource.inMemory.byItem.ItemKeyedSubredditPagingSource
import cn.learn.paging.vo.Post
import retrofit2.HttpException
import java.io.IOException

/**
 * A [PagingSource] that uses the before/after keys returned in page requests.
 *
 * @see ItemKeyedSubredditPagingSource
 */
class PageKeyedSubredditPagingSource(
    private val redditApi: RedditApi,
    private val subredditName: String
) : PagingSource<String, Post>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            val data = redditApi.getTop(
//                subreddit = subredditName,
//                after = if (params is Append) params.key else null,
//                before = if (params is Prepend) params.key else null,
//                limit = params.loadSize
            ).data

            Page(
                data = data.children.map { it.data },
                prevKey = data.before,
                nextKey = data.after
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
