package com.example.newtaipeizookotlin.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.ListPageFragmentBinding

class ListPageFragment : BaseFragment<ListPageFragmentBinding>() {
    override val mLayout: Int
        get() = R.layout.list_page_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initView() {
        super.initView()
        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener{
            onBackToPage()
        }
    }
}