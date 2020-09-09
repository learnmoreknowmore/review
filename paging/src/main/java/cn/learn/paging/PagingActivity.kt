package cn.learn.paging

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.viewpager.widget.PagerAdapter
import cn.learn.paging.datasource.Repository
import cn.learn.paging.ui.LoadStateAdapter
import cn.learn.paging.ui.PagingAdapter
import cn.learn.paging.viewmodel.PageViewModel
import cn.learn.paging.viewmodel.PageViewModelFactory
import com.jeremy.retrofitmock.RetrofitMock
import kotlinx.android.synthetic.main.activity_paging.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

class PagingActivity: AppCompatActivity() {
    companion object{
        const val KEY_REPOSITORY_TYPE = "repository_type"
    }
    private val viewModel:PageViewModel by viewModels{
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val repoTypeParam = intent.getIntExtra(KEY_REPOSITORY_TYPE, 0)
                val repoType = Repository.Type.values()[repoTypeParam]
                val repo = ServiceLocator.instance(this@PagingActivity)
                    .getRepository(repoType)
                @Suppress("UNCHECKED_CAST")
                return PageViewModel(repo, handle) as T
            }
        }
//        val repoTypeParam = intent.getIntExtra(KEY_REPOSITORY_TYPE, 0)
//        val repoType = Repository.Type.values()[repoTypeParam]
//        val repo = ServiceLocator.instance(this)
//            .getRepository(repoType)
//        PageViewModelFactory(repository = repo,savedStateHandle = this.s)
    }
    private lateinit var mAdapter:PagingAdapter
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paging)
        RetrofitMock.init(this, "mock_demo.json");
        initAdapter()
        initSwipeToRefresh()
        loadData()
    }

    @InternalCoroutinesApi
    private fun initAdapter() {
        mAdapter = PagingAdapter()
        rv_item.adapter = mAdapter
//        rv_item.adapter = mAdapter.withLoadStateHeaderAndFooter(
//            header = LoadStateAdapter(mAdapter),
//            footer = LoadStateAdapter(mAdapter)
//        )

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            mAdapter.loadStateFlow.collectLatest { loadStates ->
                swipe.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(ExperimentalCoroutinesApi::class)
            viewModel.posts.collectLatest {
                mAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            @OptIn(FlowPreview::class)
            mAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { rv_item.scrollToPosition(0) }

        }
    }

    private fun initSwipeToRefresh() {
        swipe.setOnRefreshListener { mAdapter.refresh() }
    }

    private fun loadData() {
    }

}