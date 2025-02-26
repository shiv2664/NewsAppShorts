package com.shivam.newsappshorts.fragments.home.paging_source

//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.shivam.newsappshorts.utility.network.ApiInterface
//import javax.inject.Inject
//import com.shivam.newsappshorts.fragments.home.model.Article
//import com.shivam.newsappshorts.fragments.home.model.NewsResponse


//class NewsPagingSource @Inject constructor(
//    private val apiService: ApiInterface,
//    private val apiKey: String,
//    private val query: String
//) : PagingSource<Int, Article>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
//        return try {
//            val page = params.key ?: 1
//            val response = apiService.getNewsPage(apiKey, query, page, 10)
//            Log.d("MyTag","${response.code()}")
//
//            LoadResult.Page(
//                data = NewsResponse.articles ?:emptyList(),
//                prevKey = if (page == 1) null else page - 1,
//                nextKey = if (NewsResponse.articles?.isEmpty() == true) null else page + 1
//            )
//        } catch (e: Exception) {
//            Log.d("MyTag","${e.localizedMessage}")
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
//        return state.anchorPosition
//    }
//}
