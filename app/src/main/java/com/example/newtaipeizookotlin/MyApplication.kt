package com.example.newtaipeizookotlin

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.newtaipeizookotlin.fragments.DetailPageFragment
import com.example.newtaipeizookotlin.fragments.GoogleMapFragment
import com.example.newtaipeizookotlin.fragments.HomePageFragment
import com.example.newtaipeizookotlin.fragments.ListPageFragment

class MyApplication : Application() {

    var mOpenDepartmentSelectPage = false
    lateinit var mParentFragmentManager: FragmentManager


    fun setMyFragmentManager(pParentFragmentManager: FragmentManager) {
        mParentFragmentManager = pParentFragmentManager
    }

    fun goToPage(pFragment: Fragment?=null, pBundle: Bundle?=null) {
        pFragment?.let {
            goToNextPage(it, pBundle)
        } ?: kotlin.run {
            onBackPage()
        }
    }

//    fun goToNextPage(pFragment: Fragment, pPageTitle: String) {
//        val iBundle = Bundle()
//        iBundle.putString("TitleName", pPageTitle)
//        goToNextPage(pFragment, iBundle)
//    }

    //邏輯請更改
    fun goToNextPage(pAddFragment: Fragment, pBundle: Bundle? = null) {

        Log.d("bbb", "GOTOpAddFragment = ${pAddFragment.tag}")
        var iNowFragment = pAddFragment

        for (iNowTag in mParentFragmentManager.fragments) {
            if (iNowTag.tag != null && iNowTag.tag.toString() == HomePageFragment::class.java.simpleName && !iNowTag.isHidden) {
                iNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == ListPageFragment::class.java.simpleName && !iNowTag.isHidden) {
                iNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == DetailPageFragment::class.java.simpleName && !iNowTag.isHidden) {
                iNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == GoogleMapFragment::class.java.simpleName && !iNowTag.isHidden) {
                iNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == com.example.newtaipeizookotlin.fragments.WebViewFragment::class.java.simpleName && !iNowTag.isHidden) {
                iNowFragment = iNowTag
                break
            }
        }
        Log.d("bbb", "GOTOiNowFragment = ${iNowFragment.tag}")

        mParentFragmentManager.beginTransaction()
            .add(
                R.id.mFragment,
                pAddFragment.javaClass,
                pBundle,
                pAddFragment.javaClass.simpleName
            )
            .hide(iNowFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun onBackPage() {
        val iSizeFromFragments = mParentFragmentManager.fragments.size

        if (iSizeFromFragments > 0) {
            var iPreFragment: Fragment = mParentFragmentManager.fragments[0]
            var iNowFragment: Fragment = mParentFragmentManager.fragments[0]

            for (iIndex in 0 until iSizeFromFragments) {
                val iTampFragment = mParentFragmentManager.fragments[iIndex]
                if (!iTampFragment.isHidden && iTampFragment.tag != "com.bumptech.glide.manager") {
                    iNowFragment = iTampFragment
                }

                //因為遇到glide 神奇的問題需要用這種方式遇到跳過處理
                if (iTampFragment.tag == "com.bumptech.glide.manager") {
                    continue
                } else {
                    if (iTampFragment != iNowFragment) {
                        iPreFragment = iTampFragment
                    }
                }
            }
            Log.d("bbb", "BACKiNowFragment = ${iNowFragment.tag}")
            Log.d("bbb", "BACKiPreFragment = ${iPreFragment.tag}")

            if (iNowFragment.tag != null && iNowFragment.tag.toString() == HomePageFragment::class.java.simpleName) {
                if (mOpenDepartmentSelectPage) {
                    goToNextPage(HomePageFragment())
                } else {
                    return
                }
            } else if (mParentFragmentManager.fragments.size <= 2) {
                goToNextPage(HomePageFragment())
            } else {
                mParentFragmentManager.beginTransaction()
                    .show(iPreFragment)
                    .remove(iNowFragment)
                    .commit()
            }
        }
    }
}