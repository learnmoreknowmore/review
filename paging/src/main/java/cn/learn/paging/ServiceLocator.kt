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

package cn.learn.paging

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import cn.learn.paging.datasource.Repository
import cn.learn.paging.datasource.inDb.DbRedditPostRepository
import cn.learn.paging.db.RedditDb
import cn.learn.paging.api.RedditApi
import cn.learn.paging.datasource.inMemory.byItem.InMemoryByItemRepository
import cn.learn.paging.datasource.inMemory.byPage.InMemoryByPageKeyRepository

/**
 * Super simplified service locator implementation to allow us to replace default implementations
 * for testing.
 */
interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                            app = context.applicationContext as Application,
                            useInMemoryDb = false)
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getRepository(type: Repository.Type): Repository

    fun getRedditApi(): RedditApi
}

/**
 * default implementation of ServiceLocator that uses production endpoints.
 */
open class DefaultServiceLocator(val app: Application, val useInMemoryDb: Boolean) : ServiceLocator {
    private val db by lazy {
        RedditDb.create(app, useInMemoryDb)
    }

    private val api by lazy {
        RedditApi.create()
    }

    override fun getRepository(type: Repository.Type): Repository {
        return when (type) {
            Repository.Type.IN_MEMORY_BY_ITEM -> InMemoryByItemRepository(
                    redditApi = getRedditApi()
            )
            Repository.Type.IN_MEMORY_BY_PAGE -> InMemoryByPageKeyRepository(
                    redditApi = getRedditApi()
            )
            Repository.Type.DB -> DbRedditPostRepository(
                db = db,
                api = getRedditApi()
            )
        }
    }

    override fun getRedditApi(): RedditApi = api
}