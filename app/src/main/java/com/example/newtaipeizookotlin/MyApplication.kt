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

//    private var mIndex = 0
    private var mNowFragment: Fragment? = null
    private lateinit var mParentFragmentManager: FragmentManager
    private var iFirebasePageTitle = ""
    private var iFirebasePageCode = -1
    fun setMyFragmentManager(pParentFragmentManager: FragmentManager) {
        mParentFragmentManager = pParentFragmentManager
    }

    fun goToNextPage(pAddFragment: Fragment, pBundle: Bundle?) {

        iFirebasePageTitle = pBundle?.getString("FirebasePageTitle") ?: ""
        iFirebasePageCode = pBundle?.getInt("FirebasePageCode") ?: -1

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

        //todo 試圖改造上面的for迴圈但是失敗
//        for (iNowFragmentTag in mParentFragmentManager!!.fragments) {
//            when (iNowFragmentTag.tag.toString()) {
//                HomePageFragment::class.java.simpleName -> break
//                ListPageFragment::class.java.simpleName -> break
//                DetailPageFragment::class.java.simpleName -> break
//            }
//            if (iNowFragmentTag.tag != null && !iNowFragmentTag.isHidden) {
//                mNowFragment = iNowFragmentTag
//            }
//        }


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
//        Log.d("aaa", "${pAddFragment.javaClass.simpleName},mIndex=$mIndex")
//        mIndex++
    }
}