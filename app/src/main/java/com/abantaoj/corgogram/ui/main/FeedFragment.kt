package com.abantaoj.corgogram.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.models.Post
import com.parse.FindCallback
import com.parse.ParseQuery

/**
 * A simple [Fragment] subclass.
 * Use the [FeedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FeedFragment : Fragment() {
    private lateinit var recyclerViewAdapter: FeedAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
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
        fetchPosts()
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
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.orderByDescending(Post.KEY_CREATED_AT)
        query.findInBackground { newPosts, e ->
            if (e != null) {
                Log.e(FeedFragment::class.java.simpleName, "Error getting posts")
                return@findInBackground
            }
            posts.addAll(newPosts)
            recyclerViewAdapter.notifyItemRangeInserted(0, posts.size)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedFragment()
    }
}