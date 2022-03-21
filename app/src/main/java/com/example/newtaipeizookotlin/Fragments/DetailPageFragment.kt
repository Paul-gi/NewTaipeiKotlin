package com.example.newtaipeizookotlin.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newtaipeizookotlin.GoogleMapActivity
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.WebViewActivity
import com.example.newtaipeizookotlin.databinding.MainDetailFragmentBinding
import com.example.taipeizookotlin.Adapter.GoogleMapGeoAdapter
import com.example.taipeizookotlin.DataList.ListData
import com.example.taipeizookotlin.DataList.LocationPositionData
import com.example.taipeizookotlin.Room.AppDataBase
import com.example.taipeizookotlin.Room.User
import com.example.taipeizookotlin.Service.RetrofitManager
import com.example.taipeizookotlin.Service.ZooApiService
import com.example.taipeizookotlin.Util.UtilCommonStr
import com.example.taipeizookotlin.Util.UtilTools
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DetailPageFragment : BaseFragment<MainDetailFragmentBinding>() {

    override val mLayout: Int
        get() = R.layout.main_detail_fragment

    private var mUtilTools: UtilTools = UtilTools()
    private var mListData: ListData = ListData()
    private var mLocationPositionData: LocationPositionData = LocationPositionData()
    private var mLocationPositionListData: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()
    private var mGeoListData: ArrayList<LocationPositionData> = ArrayList()
    private var mGoogleMapGeoAdapter: GoogleMapGeoAdapter = GoogleMapGeoAdapter()


    override fun initView() {
        super.initView()
        if (mFromFirebase) {
            callDetailApi()
        }

        when (mPageTitleStr) {
            UtilCommonStr.getInstance().mAnimal -> {
                initAnimalView()
            }
            UtilCommonStr.getInstance().mPlant -> {
                initPlantView()
            }
            else -> {
                initDepartment()
            }
        }
        mDataBinding.mTitleBar.mToolbar.title = mPageTitleStr
        mDataBinding.mTitleBar.mChange.visibility = View.GONE
        initBelowView()
        mProgressDialogCustom?.dismiss()
    }


    override fun getBundle() {
        super.getBundle()
        if (!mFromFirebase) {
            mListData.setRawJson(mPageTitleStr, mBundle.getString("ListDataAdapterListData") ?: "")
        }
    }

    private fun setRoom() {
        Thread {
            val mApDataBase: AppDataBase? = this.let { AppDataBase.getInstance(requireContext()) }
            mApDataBase?.userDao()?.insertUser(
                User().apply {
                    pageName = mPageTitleStr
                    clickPosition = mPageCodeInt
                    EnglishName = mListData.getEnglishName()
                    ChineseName = mListData.getChineseName()
                }
            )
        }.start()
    }


    private fun initDepartment() {
        mDataBinding.mDepartmentDetail.root.visibility = View.VISIBLE
        mDataBinding.mAnimalDetail.root.visibility = View.GONE
        mDataBinding.mPlantDetail.root.visibility = View.GONE
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.visibility = View.GONE
        mUtilTools.setData(mListData.getKeyEName(), mDataBinding.mDepartmentDetail.mEName)
        mUtilTools.setData(mListData.getKeyKeyInfo(), mDataBinding.mDepartmentDetail.mEInfo)
        mUtilTools.setData(mListData.getKeyMemo(), mDataBinding.mDepartmentDetail.mEMemo)
        mUtilTools.setData(
            mListData.getKeyDistribution(),
            mDataBinding.mDepartmentDetail.mADistribution
        )
        mDataBinding.mDepartmentDetail.mDistributionView.visibility = View.GONE
        mDataBinding.mDepartmentDetail.mADistribution.visibility = View.GONE
    }

    private fun initAnimalView() {
        mDataBinding.mDepartmentDetail.root.visibility = View.GONE
        mDataBinding.mAnimalDetail.root.visibility = View.VISIBLE
        mDataBinding.mPlantDetail.root.visibility = View.GONE
        mUtilTools.setData(mListData.getChineseName(), mDataBinding.mAnimalDetail.mANameCh)
        mUtilTools.setData(mListData.getEnglishName(), mDataBinding.mAnimalDetail.mANameEn)
        mUtilTools.setData(mListData.getKeyBehavior(), mDataBinding.mAnimalDetail.mABehavior)
        mUtilTools.setData(
            mListData.getKeyDistribution(),
            mDataBinding.mAnimalDetail.mADistribution
        )
        mUtilTools.setData(mListData.getKeyClass(), mDataBinding.mAnimalDetail.mAClass)
        mUtilTools.setData(mListData.getKeyFamily(), mDataBinding.mAnimalDetail.mAFamily)
        mUtilTools.setTextScrollView(mDataBinding.mAnimalDetail.mABehavior)
    }

    private fun initPlantView() {
        mDataBinding.mDepartmentDetail.root.visibility = View.GONE
        mDataBinding.mAnimalDetail.root.visibility = View.GONE
        mDataBinding.mPlantDetail.root.visibility = View.VISIBLE
        Log.d("qqq", mListData.getRawData())
        mUtilTools.setData(mListData.getChineseName(), mDataBinding.mPlantDetail.mANameCh)
        mUtilTools.setData(mListData.getEnglishName(), mDataBinding.mPlantDetail.mANameEn)
        mUtilTools.setData(mListData.getKeyAlsoKnown(), mDataBinding.mPlantDetail.mAAlsoKnown)
        mUtilTools.setData(mListData.getKeyBrief(), mDataBinding.mPlantDetail.ABrief)
        mUtilTools.setData(mListData.getKeyGenus(), mDataBinding.mPlantDetail.mAGenus)
        mUtilTools.setData(mListData.getKeyFeature(), mDataBinding.mPlantDetail.mAFeature)
        mUtilTools.setData(mListData.getKeyFamily(), mDataBinding.mPlantDetail.mAFamily)
        mUtilTools.setData(
            mListData.getKeyFunctionApplication(),
            mDataBinding.mPlantDetail.mAFunctionApplication
        )
    }

    private fun initBelowView() {
        try {
            mUtilTools.setPictureGone(
                requireContext(),
                mListData.keyUrl01()!!,
                mDataBinding.mBelowDetail.mAPic01URL,
                mDataBinding.mBelowDetail.mAPic01ALT,
                mDataBinding.mBelowDetail.mImageTitle
            )
            mUtilTools.setPictureGone(
                requireContext(),
                mListData.keyUrl02()!!,
                mDataBinding.mBelowDetail.mAPic02URL,
                mDataBinding.mBelowDetail.mAPic02ALT
            )
            mUtilTools.setPictureGone(
                requireContext(),
                mListData.keyUrl03()!!,
                mDataBinding.mBelowDetail.mAPic03URL,
                mDataBinding.mBelowDetail.mAPic03ALT
            )
            mUtilTools.setPictureGone(
                requireContext(),
                mListData.keyUrl04()!!,
                mDataBinding.mBelowDetail.mAPic04URL,
                mDataBinding.mBelowDetail.mAPic04ALT
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mUtilTools.setData(mListData.getKeyAlt01(), mDataBinding.mBelowDetail.mAPic01ALT)
        mUtilTools.setData(mListData.getKeyAlt03(), mDataBinding.mBelowDetail.mAPic03ALT)
        mUtilTools.setData(mListData.getKeyAlt02(), mDataBinding.mBelowDetail.mAPic02ALT)
        mUtilTools.setData(mListData.getKeyAlt04(), mDataBinding.mBelowDetail.mAPic04ALT)
        mUtilTools.setGeo(mListData, mLocationPositionData, mGeoListData)
        mUtilTools.setLocation(mListData, mLocationPositionData, mLocationPositionListData)
        mDataBinding.mBelowDetail.mLocation.setOnClickListener {
            val iIntent = Intent()
            val iBundle = Bundle()
            iBundle.putSerializable("mLocationPositionListData", mLocationPositionListData)
            iIntent.setClass(requireActivity(), GoogleMapActivity::class.java)
            iIntent.putExtras(iBundle)
            startActivity(iIntent)
        }
        mGoogleMapGeoAdapter.setData(mGeoListData)
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.layoutManager =
            LinearLayoutManager(requireContext())
        mDataBinding.mBelowDetail.mGeoRecycleViewGeo.adapter = mGoogleMapGeoAdapter
        if (mListData.getKeyVedio().equals("")) {
            mDataBinding.mBelowDetail.mAVedioURL.visibility = View.GONE
            mDataBinding.mBelowDetail.mVdoView.visibility = View.GONE
        } else {
            //jumpVedioUrl
            mDataBinding.mBelowDetail.mAVedioURL.setOnClickListener {
                //點擊外開影片連結
//                Intent pIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((mListData.getKeyVedio())));
//                startActivity(pIntent);
                val intent = Intent()
                val bundle = Bundle()
                bundle.putString("getUrl", mListData.getKeyVedio())
                intent.setClass(requireContext(), WebViewActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        mDataBinding.mTitleBar.mBackBtn.setOnClickListener {
            setRoom()
        }
    }


    private fun callDetailApi() {
        mCallApi(mPageTitleStr, mPageCodeInt)
    }

    private fun mCallApi(pTitle: String, pPageCode: Int) {
        val mCallDetail: Call<JsonObject>?
        val mZooApiService: ZooApiService =
            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)
        val mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
        var iApiSelectTitle = ""


        when (pTitle) {
            "Animal" -> iApiSelectTitle = mUtilCommonStr.mAnimal
            "Plant" -> iApiSelectTitle = mUtilCommonStr.mPlant
            "OutSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mOutSideArea
                mFromFirebase = true
            }
            "InSideArea" -> {
                iApiSelectTitle = mUtilCommonStr.mInSideArea
                mFromFirebase = true
            }
        }

        mCallDetail = when (pTitle) {
            "Animal" -> {
                mZooApiService.getAnimalData(1, pPageCode)
            }
            "Plant" -> {
                mZooApiService.getPlantData(1, pPageCode)
            }
            else -> {
                mZooApiService.getPavilionData(iApiSelectTitle, 1, pPageCode)
            }
        }



        mCallDetail.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {

                    val iListData: ArrayList<ListData> = ArrayList()
                    assert(response.body() != null)
                    val ix = JSONObject(response.body().toString())
                    val iz = ix.getJSONObject("result").getJSONArray("results")
                    val iData = ListData()
                    iData.setData(iz.getJSONObject(0))
                    iData.selectType(iApiSelectTitle, false)
                    iListData.add(iData)

                    mPageTitleStr = iApiSelectTitle
                    mListData.setRawJson(mPageTitleStr, iListData[0].getRawData())

                    initView()
                    mProgressDialogCustom?.dismiss()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    if (e.toString() == "org.json.JSONException: Index 0 out of range [0..0)") {
                        Log.d("FirebaseFcmErrorMessage", e.toString())
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.d("callApiError", t.toString())
            }
        })
    }
}