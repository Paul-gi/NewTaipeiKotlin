package com.example.newtaipeizookotlin.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.newtaipeizookotlin.MyApplication
import com.example.newtaipeizookotlin.tools.ProgressDialogCustom
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

abstract class BaseFragment<dataBinding : ViewDataBinding> : Fragment() {
    private var mTampDataBinding: dataBinding? = null
    protected val mDataBinding: dataBinding get() = mTampDataBinding!!
    abstract val mLayout: Int
    protected var myApplication = MyApplication()
    protected var mProgressDialogCustom: ProgressDialogCustom? = null
    protected var mPageTitleStr = "Title"
    protected var mPageCodeInt = -1
    protected var mFromFirebase = false
    protected var mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
    protected var mBundle = Bundle()
    protected var mIsCallApi = false


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
        getBundle()
        initView()
        return iDataBinding.root
    }

    protected open fun initView() {
        mProgressDialogCustom = ProgressDialogCustom(requireContext())
        mProgressDialogCustom!!.show(parentFragmentManager, "")
        this.parentFragment?.let { fragmentOnBackPressed(it, this.requireActivity()) }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    protected open fun getBundle() {
        val iBundleBox = arguments
        if (iBundleBox != null) {
            mBundle = iBundleBox
        }


        mFromFirebase = mBundle.getBoolean("MainFormFirebase")
        if (mFromFirebase) {
            mPageTitleStr = mBundle.getString("MainPageTitle") ?: ""
            mPageCodeInt = mBundle.getInt("MainPageCode") ?: -1
        } else {
            mPageTitleStr = mBundle.getString("NormalTitle") ?: ""
        }
    }

    protected open fun onBackToPage() {
        parentFragmentManager.popBackStack()
    }

    protected open fun goToNextPage(pFragment: Fragment, pPageTitle: String) {
        val iBundle = Bundle()
        iBundle.putString("NormalTitle", pPageTitle)
        myApplication.goToNextPage(pFragment, iBundle)
    }


    protected open fun fragmentOnBackPressed(
        pFragment: Fragment,
        pFragmentActivity: FragmentActivity
    ) {
        pFragmentActivity
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    myApplication.onBackPage(pFragment)
                    /**
                     * if you want onBackPressed() to be called as normal afterwards
                     */
//                    if (isEnabled) {
//                        isEnabled = false
//                        requireActivity().onBackPressed()
//                    }
                }
            })
    }
}