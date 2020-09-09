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

package cn.learn.paging.datasource.inDb

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cn.learn.paging.datasource.Repository
import cn.learn.paging.db.RedditDb
import cn.learn.paging.vo.Post
import cn.learn.paging.api.RedditApi
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation that uses a database backed [androidx.paging.PagingSource] and
 * [androidx.paging.RemoteMediator] to load pages from network when there are no more items cached
 * in the database to load.
 */
class DbRedditPostRepository(val db: RedditDb, val api: RedditApi) : Repository {

    override fun getPageList(keyword: String, pageSize: Int): Flow<PagingData<Post>> = Pager(
        config = PagingConfig(pageSize),
        remoteMediator = PageKeyedRemoteMediator(db, api, keyword)
    ){
        db.posts().postsBySubreddit(keyword)
    }.flow
}
