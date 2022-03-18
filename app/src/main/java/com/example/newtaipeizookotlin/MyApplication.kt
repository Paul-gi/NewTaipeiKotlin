package com.example.newtaipeizookotlin

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.newtaipeizookotlin.Fragments.DetailPageFragment
import com.example.newtaipeizookotlin.Fragments.HomePageFragment
import com.example.newtaipeizookotlin.Fragments.ListPageFragment

class MyApplication : Application() {

    private var mNowFragment: Fragment? = null
    private lateinit var mParentFragmentManager: FragmentManager
    fun setMyFragmentManager(pParentFragmentManager: FragmentManager) {
        mParentFragmentManager = pParentFragmentManager
    }

    fun goToNextPage(pAddFragment: Fragment, pBundle: Bundle?) {
        mNowFragment = pAddFragment

        val iNowFragment = mNowFragment
        Log.d("aaa", "mNowFragment = ${mNowFragment?.tag}")

        for (iNowTag in mParentFragmentManager.fragments) {
            Log.d("aaa", "iNowTag = ${iNowTag.tag}\n")
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
                    pAddFragment.javaClass,
                    pBundle,
                    pAddFragment.javaClass.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
    }
}