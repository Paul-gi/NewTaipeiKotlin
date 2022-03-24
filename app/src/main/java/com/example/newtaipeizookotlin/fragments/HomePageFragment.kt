package com.example.newtaipeizookotlin.fragments

import android.view.View
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.HomeFragmentBinding

class HomePageFragment : BaseFragment<HomeFragmentBinding>() {
    override val mLayout: Int
        get() = R.layout.home_fragment

    private var mFcmFromDepartment = false

    override fun initView() {
        super.initView()
        getBundle()

        if (mFcmFromDepartment) {
            openDepartmentSelectPage()
        }

        mProgressDialogCustom?.dismiss()

        mDataBinding.mAllAreaNavigationIC.mDepartmentButton.setOnClickListener {
            openDepartmentSelectPage()
        }

        mDataBinding.mAllAreaNavigationIC.mAnimalButton.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), mUtilCommonStr.mAnimal)
        }

        mDataBinding.mAllAreaNavigationIC.mPlantButton.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), mUtilCommonStr.mPlant)
        }

        mDataBinding.mDepartmentSelect.mToolbarLayout.mBackBtn.setOnClickListener {
            openHomePage()
        }

        mDataBinding.mDepartmentSelect.mInDoorAreaBtn.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), mUtilCommonStr.mInSideArea)
        }

        mDataBinding.mDepartmentSelect.mOutDoorAreaBtn.setOnClickListener {
            myApplication.goToNextPage(ListPageFragment(), mUtilCommonStr.mOutSideArea)
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

    override fun getBundle() {
        val iBundleBox = arguments
        if (iBundleBox != null) {
            mFcmFromDepartment = iBundleBox.getBoolean("FormDepartment")
        }
    }
}