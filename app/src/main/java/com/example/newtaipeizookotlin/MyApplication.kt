package com.example.newtaipeizookotlin

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.newtaipeizookotlin.fragments.DetailPageFragment
import com.example.newtaipeizookotlin.fragments.HomePageFragment
import com.example.newtaipeizookotlin.fragments.ListPageFragment

class MyApplication : Application() {

    private var mNowFragment: Fragment? = null
    private lateinit var mParentFragmentManager: FragmentManager
    fun setMyFragmentManager(pParentFragmentManager: FragmentManager) {
        mParentFragmentManager = pParentFragmentManager
    }


    fun goToNextPage(pFragment: Fragment, pPageTitle: String) {
        val iBundle = Bundle()
        iBundle.putString("TitleName", pPageTitle)
        goToNextPage(pFragment, iBundle)
    }


    fun goToNextPage(pAddFragment: Fragment, pBundle: Bundle?) {
        mNowFragment = pAddFragment

        val iNowFragment = mNowFragment

        for (iNowTag in mParentFragmentManager.fragments) {
            if (iNowTag.tag != null && iNowTag.tag.toString() == HomePageFragment::class.java.simpleName && !iNowTag.isHidden) {
                mNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == ListPageFragment::class.java.simpleName && !iNowTag.isHidden) {
                mNowFragment = iNowTag
                break
            }
            if (iNowTag.tag != null && iNowTag.tag.toString() == DetailPageFragment::class.java.simpleName && !iNowTag.isHidden) {
                mNowFragment = iNowTag
                break
            }
        }


        if (iNowFragment != null) {
            mParentFragmentManager.beginTransaction()
                .add(
                    R.id.mFragment,
                    pAddFragment.javaClass,
                    pBundle,
                    pAddFragment.javaClass.simpleName
                )
                .hide(mNowFragment!!)
                .addToBackStack(null)
                .commit()
        } else {
            mParentFragmentManager.beginTransaction()
                .add(
                    R.id.mFragment,
                    HomePageFragment().javaClass,
                    pBundle,
                    pAddFragment.javaClass.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
    }


    fun onBackPage(pNowFragment: Fragment, pBundle: Bundle?) {
        val iSizeFromFragments = mParentFragmentManager.fragments.size
        if (iSizeFromFragments > 0) {
            Log.d("bbb", "pNowFragment = ${pNowFragment.tag}")
            var iPreFragment: Fragment = mParentFragmentManager.fragments[0]

            for (iIndex in 0 until iSizeFromFragments) {
                val iTampFragment = mParentFragmentManager.fragments[iIndex]
                Log.d("bbb", "iStackFragment = ${iTampFragment.tag}")

                //因為遇到glide 神奇的問題需要用這種方式遇到跳過處理
                if (iTampFragment.tag == "com.bumptech.glide.manager") {
                    continue
                } else {
                    if (iTampFragment != pNowFragment) {
                        iPreFragment = iTampFragment
                    }
                }
            }


            val iFromFirebase = pBundle?.getBoolean("FormFirebase")
            if (iFromFirebase == true && mParentFragmentManager.fragments.size <= 2) {
                for (iFragment in mParentFragmentManager.fragments) {
                    mParentFragmentManager.beginTransaction()
                        .remove(iFragment)
                        .commit()
                }
                if (pNowFragment.tag != null && pNowFragment.tag.toString() == ListPageFragment::class.java.simpleName) {
                    goToNextPage(HomePageFragment(), pBundle)
                } else if (pNowFragment.tag != null && pNowFragment.tag.toString() == DetailPageFragment::class.java.simpleName) {
                    goToNextPage(ListPageFragment(), pBundle)
                } else {
                    return
                }
            } else {
                if (pNowFragment.tag != null && pNowFragment.tag.toString() == HomePageFragment::class.java.simpleName) {
                    return
                } else {
                    mParentFragmentManager.beginTransaction()
                        .show(iPreFragment)
                        .remove(pNowFragment)
                        .commit()
                }
            }
        }
    }
}