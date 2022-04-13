package com.example.newtaipeizookotlin.fragments

import android.os.Bundle
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
            setBundleAndSendToPage(mUtilCommonStr.mAnimal)
        }

        mDataBinding.mAllAreaNavigationIC.mPlantButton.setOnClickListener {
            setBundleAndSendToPage(mUtilCommonStr.mPlant)
        }

        mDataBinding.mDepartmentSelect.mToolbarLayout.mBackBtn.setOnClickListener {
            openHomePage()
        }

        mDataBinding.mDepartmentSelect.mInDoorAreaBtn.setOnClickListener {
            setBundleAndSendToPage(mUtilCommonStr.mInSideArea)
        }

        mDataBinding.mDepartmentSelect.mOutDoorAreaBtn.setOnClickListener {
            setBundleAndSendToPage(mUtilCommonStr.mOutSideArea)
        }

    }

    private fun setBundleAndSendToPage(pStrTamp: String) {
        val iBundle = Bundle()
        iBundle.putString("TitleName", pStrTamp)
        myApplication.goToPage(ListPageFragment(), iBundle)
    }

    private fun openHomePage() {
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.VISIBLE
        mDataBinding.mDepartmentSelect.root.visibility = View.GONE
        myApplication.mOpenDepartmentSelectPage = false
    }

    private fun openDepartmentSelectPage() {
        mDataBinding.mAllAreaNavigationIC.root.visibility = View.GONE
        mDataBinding.mDepartmentSelect.root.visibility = View.VISIBLE
        myApplication.mOpenDepartmentSelectPage = true
    }
}