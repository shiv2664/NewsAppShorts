package com.shivam.newsappshorts.fragments.bookmarks.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.shivam.newsappshorts.R
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivam.newsappshorts.databinding.FragmentBookmarksBinding
import com.shivam.newsappshorts.fragments.bookmarks.adapter.BookmarkAdapter
import com.shivam.newsappshorts.fragments.bookmarks.viewmodel.BookmarksViewModel
import com.shivam.newsappshorts.fragments.home.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class Bookmarks : Fragment() {
    private var _binding: FragmentBookmarksBinding?=null
    private val binding get()=_binding!!

    private val bookmarkViewModel: BookmarksViewModel by activityViewModels()
    private var bookmarkList: List<Article>? = null


    private var adapter: BookmarkAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (_binding==null){
            _binding = FragmentBookmarksBinding.inflate(inflater,container,false)

            adapter = BookmarkAdapter(object : BookmarkAdapter.Listener {

                override fun onItemClick(item: Article) {

                    if (requireContext().isInternetAvailable()) {
                        val args = Bundle()
                        args.putString("url",item.url)
                        findNavController().navigate(R.id.action_bookmark_to_detailsFragment,args)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "No Internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onItemBookmarkClick(selectedPosition: Int, ivBookmark: ImageView) {

                    val listingDataItem: List<Article>? = bookmarkList

                    lifecycleScope.launch {

                        val bookmark = listingDataItem?.get(selectedPosition)?.let {
                                bookmarkViewModel.getBookmark(it.title)

                        }

                        if (bookmark == null) {
                            bookmarkViewModel.insertBookmark(listingDataItem?.get(selectedPosition))
                            ivBookmark.isSelected = true

                        } else {
                            bookmarkViewModel.removeBookmark(listingDataItem[selectedPosition].title)
                            ivBookmark.isSelected = false
                        }
                    }


                }

                override fun isBookMark(selectedPosition: Int, ivBookmark: ImageView) {

                    val listingDataItem: List<Article>? =bookmarkList

                    lifecycleScope.launch {

                        val bookmark=bookmarkViewModel.getBookmark(listingDataItem?.get(selectedPosition)?.title?:"")

                        if (bookmark == null) {
                            ivBookmark.setBackgroundResource(R.drawable.baseline_bookmark_border)

                        } else {
                            ivBookmark.setBackgroundResource(R.drawable.baseline_bookmark_24)

                        }
                    }

                }

            })

            bookmarkViewModel.bookmarkFeedLiveData.observe(viewLifecycleOwner) {

                bookmarkList = it


                adapter?.updateData(requireActivity(), it, "bookamrk")

            }

            binding.bookmarks.adapter = adapter
            val linearLayoutManager = LinearLayoutManager(requireActivity())
            binding.bookmarks.layoutManager = linearLayoutManager


            return binding.root
        }else{
            return binding.root
        }

    }

    fun Context.isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}