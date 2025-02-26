package com.shivam.newsappshorts.fragments.home.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenStarted
import com.shivam.newsappshorts.R
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.shivam.newsappshorts.fragments.home.viewmodel.HomeFragmentViewModel
import com.shivam.newsappshorts.databinding.FragmentHomeBinding
import com.shivam.newsappshorts.fragments.bookmarks.viewmodel.BookmarksViewModel
import com.shivam.newsappshorts.fragments.home.adapter.RecyclerviewAdapter
import com.shivam.newsappshorts.fragments.home.model.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var bookmarkList: List<Article>? = null

    private val bookmarkViewModel: BookmarksViewModel by activityViewModels()

    private val viewModel: HomeFragmentViewModel by viewModels()
    private val moviesAdapter: RecyclerviewAdapter by lazy {
        RecyclerviewAdapter(object : RecyclerviewAdapter.Listener {
            override fun onItemClick(item: Article) {
                val args = Bundle()
                args.putString("url", item.url)
                findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, args)

            }

            override fun onItemShareClick(item: Article?) {
                shareWithDynamicLink(requireContext(), item?.title ?: "", item?.url ?: "")
            }

            override fun onItemBookmarkClick(selectedPosition: Int, ivBookmark: ImageView) {

                val listingDataItem: List<Article> = moviesAdapter.snapshot().items

                lifecycleScope.launch {

                    val bookmark = listingDataItem.getOrNull(selectedPosition)?.let {
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

                val listingDataItem: List<Article> = moviesAdapter.snapshot().items

                lifecycleScope.launch {

                    val bookmark = bookmarkViewModel.getBookmark(
                        listingDataItem?.getOrNull(selectedPosition)?.title ?: ""
                    )

                    if (bookmark == null) {
                        ivBookmark.isSelected = false

                    } else {
                        ivBookmark.isSelected = true

                    }
                }

            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        if (_binding == null) {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

            if (!requireContext().isInternetAvailable()) {
                binding.noInternet.visibility = View.VISIBLE
                binding.retry.visibility = View.VISIBLE
            }

            binding.retry.setOnClickListener {
                if (!requireActivity().isInternetAvailable()) {
                    Toast.makeText(
                        requireActivity(),
                        "No Internet Please check again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                }

                binding.noInternet.visibility = View.GONE
                binding.retry.visibility = View.GONE
                getNews()
            }

            initAdapter()

            moviesAdapter.setSection("India")

            binding.searchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    s?.length?.let {
                        if (it >= 3) {
                            getNews(s.toString())
                            moviesAdapter.setSection(s.toString())
                        }

                    }
                }
            })
            getNews()



            bookmarkViewModel.bookmarkIdsLiveData.observe(viewLifecycleOwner) {

                val currentList = moviesAdapter.snapshot().items
                for (item in currentList) {
                    val position = currentList.indexOf(item)
                    if (it.contains(item.title)) {
                        moviesAdapter.notifyItemChanged(position)
                    }
                    val viewHolder =
                        binding.newsRecyclerview.findViewHolderForAdapterPosition(position) as? RecyclerviewAdapter.ItemHomeViewHolder
                    if (viewHolder?.binding?.ivBookmark?.isSelected == true) {
                        if (!it.contains(item.title)) {
                            moviesAdapter.notifyItemChanged(position)
                        }
                    }
                }
            }


            return binding.root
        } else {
            return binding.root
        }
    }

    private fun getNews(query: String = "India") {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getData("84c8ffe674244229b62799d33774504a", query).collectLatest {
                    Log.d("MyTag", "Data submitting")
                    moviesAdapter.submitData(it)
                    Log.d("MyTag", "Data submitted")
                }
            }
        }
    }

    private fun initAdapter() {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.newsRecyclerview.adapter = moviesAdapter
        binding.newsRecyclerview.layoutManager = linearLayoutManager
        binding.newsRecyclerview.setHasFixedSize(true)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun shareWithDynamicLink(context: Context, title: String, link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Read: $title")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check this out: $link\n\nShared via ${context.getString(R.string.app_name)}"
            )
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via ${context.getString(R.string.app_name)}"))
    }


    fun Context.isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

}