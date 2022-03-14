package com.example.newtaipeizookotlin.Fragments

import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.DepartmentSelectpageFragmentBinding

class DetailSelectPageFragment : BaseFragment<DepartmentSelectpageFragmentBinding>() {
    override val mLayout: Int
        get() = R.layout.department_selectpage_fragment


    override fun initView() {
        super.initView()
        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener{
            onBackToPage()
        }
    }

}