package com.example.newtaipeizookotlin.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.databinding.ListPageFragmentBinding
import com.example.newtaipeizookotlin.viewmodel.ListPageCallViewModel
import com.example.newtaipeizookotlin.adapter.ListDataAdapter
import com.example.newtaipeizookotlin.datalist.ListData

class ListPageFragment : BaseFragment<ListPageFragmentBinding>() {
    override val mLayout: Int
        get() = R.layout.list_page_fragment


    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var mGridLayoutManager: GridLayoutManager? = null
    private var mFinish = false
    private var mPageState = true

    private val mCallViewModel: ListPageCallViewModel by lazy {
        ViewModelProvider(this).get(ListPageCallViewModel::class.java)
    }

    private val mListDataAdapter: ListDataAdapter by lazy {
        ListDataAdapter(object : ListDataAdapter.ListDataItf {
            override fun getData(pListData: ListData?) {

            }

        }, requireContext(), mPageTitleStr, mPageState, myApplication)
    }



    override fun initView() {
        super.initView()
        getBundle()
        mLinearLayoutManager = LinearLayoutManager(this.activity)
        mDataBinding.mRecycleView.layoutManager = mLinearLayoutManager
        mDataBinding.mToolbarLayout.mToolbar.title = mPageTitleStr
        mDataBinding.mToolbarLayout.mBackBtn.setOnClickListener {
            if (!mFromFirebase) {
                onBackToPage(this)
            } else {
                val iBundle = Bundle()
                iBundle.putBoolean("FormDepartment", mFormDepartment)
                iBundle.putBoolean("FormFirebase", mFromFirebase)
                myApplication.onBackPage(this, iBundle)
            }
        }

        mDataBinding.mRecycleView.adapter = mListDataAdapter
        mDataBinding.mToolbarLayout.mChange.setOnClickListener {
            if (!mPageState) {
                mGridLayoutManager = GridLayoutManager(activity, 1)
                mDataBinding.mRecycleView.layoutManager = mLinearLayoutManager
                mPageState = true
            } else {
                mGridLayoutManager = GridLayoutManager(activity, 2)
                mDataBinding.mRecycleView.layoutManager = mGridLayoutManager
                mPageState = false
            }
            mListDataAdapter.setPageState(mPageState)
            mDataBinding.mRecycleView.adapter = mListDataAdapter
        }


        //================================RecycleView 到底刷新的部分＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        mDataBinding.mRecycleView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (!mDataBinding.mRecycleView.canScrollVertically(1)) {
                if (!mFinish) {
                    mProgressDialogCustom!!.show(parentFragmentManager, "")
                    callApiThread()
                } else {
                    Toast.makeText(activity, "到底了", Toast.LENGTH_SHORT).show()
                }
            }
        }
        mCallViewModel.getDataFinishState().observe(viewLifecycleOwner) { aBoolean ->
            mFinish = aBoolean
        }
        mCallViewModel.getDataListObserver().observe(viewLifecycleOwner) { pCallData ->
            if (pCallData != null) {
                mListDataAdapter.setData(pCallData)
                mProgressDialogCustom!!.dismiss()
            }
        }
        callApiThread()
    }

    private fun callApiThread() {
        Thread { mCallViewModel.mCallApi(mPageTitleStr) }.start()
    }
}