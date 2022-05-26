package com.example.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.helper.Session
import com.example.network.ApiService
import com.example.network.ListStory
import retrofit2.awaitResponse

class StoryPagingSource(private val apiService: ApiService, context: Context) :
    PagingSource<Int, ListStory>() {
    private var session = Session(context)

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStory>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStories("Bearer ${session.getToken().toString()}", page, params.loadSize).awaitResponse().body()?.listStory

            LoadResult.Page(
                data = responseData!!,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}