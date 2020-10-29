package com.abantaoj.corgogram.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.models.Post
import com.parse.ParseQuery


/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    private lateinit var recyclerViewAdapter: FeedAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    private val posts: MutableList<Post> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupRefreshLayout(view)
        fetchPosts()
    }

    private fun setupRefreshLayout(view: View) {
        refreshLayout = view.findViewById(R.id.fragmentFeedRefreshLayout)
        refreshLayout.apply {
            setColorSchemeColors(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))
            setOnRefreshListener { fetchPosts() }
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerViewAdapter = FeedAdapter(requireContext(), posts)

        view.findViewById<RecyclerView>(R.id.fragmentFeedRecyclerView).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerViewAdapter
        }
    }

    private fun fetchPosts() {
        refreshLayout.isRefreshing = true

        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.orderByDescending(Post.KEY_CREATED_AT)
        query.findInBackground { newPosts, e ->
            if (e != null) {
                Log.e(FeedFragment::class.java.simpleName, "Error getting posts")
                refreshLayout.isRefreshing = false
                return@findInBackground
            }
            posts.clear()
            posts.addAll(newPosts)
            recyclerViewAdapter.notifyDataSetChanged()
            refreshLayout.isRefreshing = false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}