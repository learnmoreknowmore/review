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

package cn.learn.paging.datasource.inMemory.byItem

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Append
import androidx.paging.PagingSource.LoadParams.Prepend
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import cn.learn.paging.vo.Post
import cn.learn.paging.api.RedditApi
import retrofit2.HttpException
import java.io.IOException

/**
 * A [PagingSource] that uses the "name" field of posts as the key for next/prev pages.
 *
 * Note that this is not the correct consumption of the Reddit API but rather shown here as an
 * alternative implementation which might be more suitable for your backend.
 *
 * @see [com.android.example.paging.pagingwithnetwork.reddit.repository.inMemory.byPage.PageKeyedSubredditPagingSource]
 */
class ItemKeyedSubredditPagingSource(
    private val redditApi: RedditApi,
    private val subredditName: String
) : PagingSource<String, Post>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Post> {
        return try {
            val items = redditApi.getTop(
//                    subreddit = subredditName,
//                    after = if (params is Append) params.key else null,
//                    before = if (params is Prepend) params.key else null,
//                    limit = params.loadSize
            ).data.children.map { it.data }

            Page(
                    data = items,
                    prevKey = items.firstOrNull()?.name,
                    nextKey = items.lastOrNull()?.name
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getRefreshKey(state: PagingState<String, Post>): String? {
        /**
         * The name field is a unique identifier for post items.
         * (no it is not the title of the post :) )
         * https://www.reddit.com/dev/api
         */
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.name
        }
    }
}
