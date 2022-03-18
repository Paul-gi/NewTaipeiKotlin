package com.example.newtaipeizookotlin.Fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.newtaipeizookotlin.MyApplication
import com.example.newtaipeizookotlin.tools.ProgressDialogCustom
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

abstract class BaseFragment<dataBinding : ViewDataBinding> : Fragment() {
    private var mTampDataBinding: dataBinding? = null
    protected val mDataBinding: dataBinding get() = mTampDataBinding!!
    abstract val mLayout: Int
    private var myApplication = MyApplication()
    protected var mProgressDialogCustom: ProgressDialogCustom? = null
    protected var mPageTitleStr = "Title"
    private var mPageCodeInt = -1
    private var mFromFirebase = false
    protected var mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
    private var mBundle = Bundle()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myApplication = requireActivity().application as MyApplication

        FirebaseMessaging.getInstance().subscribeToTopic("news")
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("firebaseToken", token.toString())
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val iDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            mLayout,
            container,
            false
        )
        mTampDataBinding = iDataBinding as dataBinding?
        initView()
        return iDataBinding.root
    }

    protected open fun initView() {
        mProgressDialogCustom = ProgressDialogCustom(requireContext())
        getBundle()
    }

    private fun getBundle() {
        val iBundle = arguments
        if (iBundle != null) {
            mFromFirebase = iBundle.getBoolean("MainFormFirebase")
            if (mFromFirebase) {
                mPageTitleStr = iBundle.getString("MainPageTitle") ?: ""
                mPageCodeInt = iBundle.getInt("MainPageCode") ?: -1
            } else {
                mPageTitleStr = iBundle.getString("NormalTitle") ?: ""
            }
        }
    }

    protected open fun onBackToPage() {
        parentFragmentManager.popBackStack()
    }

    protected open fun goToNextPage(pFragment: Fragment, pPageTitle: String) {
        mBundle.putString("NormalTitle", pPageTitle)
        myApplication.goToNextPage(pFragment, mBundle)
    }
}