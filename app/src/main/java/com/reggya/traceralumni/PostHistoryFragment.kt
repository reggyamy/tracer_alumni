package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.FragmentPostHistoryBinding
import com.reggya.traceralumni.ui.adapter.PostsAdapter
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory

class PostHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPostHistoryBinding
    private lateinit var viewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preference = LoginPreference(this.requireContext())
        val userId = preference.getUsername()
        val postsAdapter = PostsAdapter()

        val factory = ViewModelFactory.getInstance(this.requireContext())
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
        viewModel.getPostsByUserId(userId.toString()).observe(viewLifecycleOwner){
            when (it) {
                ApiResponse.success(it.data) -> {
                    postsAdapter.setNewData(it.data?.reversed())
                    postsAdapter.notifyDataSetChanged()
                }
                ApiResponse.empty() -> {
//                    binding.noPostYet.visibility = View.VISIBLE
//                    binding.tvNopost.visibility = View.VISIBLE
                }
                ApiResponse.error(it.message) -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = postsAdapter
        }

    }


}