package com.example.newtaipeizookotlin.Fragments

import android.os.Bundle
import com.example.newtaipeizookotlin.MyApplication
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.HomeFragmentBinding

class HomePageFragment : BaseFragment<HomeFragmentBinding>() {
    private var myApplication = MyApplication()

    override val mLayout: Int
        get() = R.layout.home_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireActivity().application as MyApplication
        // myApplication.goToNextPage(this.javaClass.simpleName)
    }


    override fun initView() {
        super.initView()
        mDataBinding.mDepartmentButton.setOnClickListener {
            myApplication.goToNextPage(DetailSelectPageFragment(), null)
        }

        mDataBinding.mAnimalButton.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), null)
        }

        mDataBinding.mPlantButton.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), null)
        }
    }
}