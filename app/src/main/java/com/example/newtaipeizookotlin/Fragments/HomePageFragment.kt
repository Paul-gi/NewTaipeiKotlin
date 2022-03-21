package com.example.newtaipeizookotlin.Fragments

import android.view.View
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.HomeFragmentBinding

class HomePageFragment : BaseFragment<HomeFragmentBinding>() {
    override val mLayout: Int
        get() = R.layout.home_fragment


    override fun initView() {
        super.initView()
        mProgressDialogCustom?.dismiss()

        mDataBinding.mAllAreaNavigationIC.mDepartmentButton.setOnClickListener {
            openDepartmentSelectPage()
        }

        mDataBinding.mAllAreaNavigationIC.mAnimalButton.setOnClickListener {
            goToNextPage(ListPageFragment(), mUtilCommonStr.mAnimal)
        }

        mDataBinding.mAllAreaNavigationIC.mPlantButton.setOnClickListener {
            goToNextPage(ListPageFragment(), mUtilCommonStr.mPlant)
        }

        mDataBinding.mDepartmentSelect.mToolbarLayout.mBackBtn.setOnClickListener {
            openHomePage()
        }

        mDataBinding.mDepartmentSelect.mInDoorAreaBtn.setOnClickListener {
            goToNextPage(ListPageFragment(), mUtilCommonStr.mInSideArea)
        }

        mDataBinding.mDepartmentSelect.mOutDoorAreaBtn.setOnClickListener {
            goToNextPage(ListPageFragment(), mUtilCommonStr.mOutSideArea)
        }

    }

    private fun openHomePage() {
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.VISIBLE
        mDataBinding.mDepartmentSelect.root.visibility = View.GONE
    }

    private fun openDepartmentSelectPage() {
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.GONE
        mDataBinding.mDepartmentSelect.root.visibility = View.VISIBLE
    }
}