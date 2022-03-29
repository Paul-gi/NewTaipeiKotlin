//package com.example.newtaipeizookotlin.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.newtaipeizookotlin.datalist.ListData
//import com.example.newtaipeizookotlin.service.RetrofitManager
//import com.example.newtaipeizookotlin.service.ZooApiService
//import com.example.newtaipeizookotlin.tools.UtilCommonStr
//import com.google.gson.JsonObject
//import org.json.JSONException
//import org.json.JSONObject
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.*
//
//class DetailPageCallViewModel:ViewModel() {
//    private val mDataList: MutableLiveData<ArrayList<ListData>?> =
//        MutableLiveData<ArrayList<ListData>?>()
//    private val mFinish = MutableLiveData<Boolean>()
//    private var mIndex = 0
//    private var mIsNotFinish = false
//    private var mGetData = false
//    private var mCall: Call<JsonObject>? = null
//    private var mUtilCommonStr: UtilCommonStr = UtilCommonStr.getInstance()
//    private var mFromFirebase = MutableLiveData<Boolean>()
//
//
//    fun getDataListObserver(): MutableLiveData<ArrayList<ListData>?> {
//        return mDataList
//    }
//
//    fun getDataFinishState(): MutableLiveData<Boolean> {
//        return mFinish
//    }
//
//    fun getIsFromFirebase(): MutableLiveData<Boolean> {
//        return mFromFirebase
//    }
//
//
//
//    fun mCallApi(pTitleName: String, mPageCodeInt: Int) {
//        var iApiSelectTitle = ""
//
//        if (mIsNotFinish) {
//            return
//        }
//        if (mGetData) {
//            return
//        }
//        synchronized(this) { mGetData = true }
//        val mZooApiService: ZooApiService =
//            RetrofitManager().getInstance()!!.createService(ZooApiService::class.java)
//
//
//        when (pTitleName) {
//            "Animal" -> iApiSelectTitle = mUtilCommonStr.mAnimal
//            "Plant" -> iApiSelectTitle = mUtilCommonStr.mPlant
//            "OutSideArea" -> {
//                iApiSelectTitle = mUtilCommonStr.mOutSideArea
//                mFromFirebase = MutableLiveData<Boolean>()
//            }
//            "InSideArea" -> {
//                iApiSelectTitle = mUtilCommonStr.mInSideArea
//                mFromFirebase = true
//            }
//        }
//
//        mCall = when (pTitleName) {
//            "Animal" -> {
//                mZooApiService.getAnimalData(1, mPageCodeInt)
//            }
//            "Plant" -> {
//                mZooApiService.getPlantData(1, mPageCodeInt)
//            }
//            else -> {
//                mZooApiService.getPavilionData(iApiSelectTitle, 1, mPageCodeInt)
//            }
//        }
//
//        mCall!!.enqueue(object : Callback<JsonObject?> {
//            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
//                try {
//
//                    val iListData: ArrayList<ListData> = ArrayList()
//                    assert(response.body() != null)
//                    val ix = JSONObject(response.body().toString())
//                    val iz = ix.getJSONObject("result").getJSONArray("results")
//                    val iData = ListData()
//                    iData.setData(iz.getJSONObject(0))
//                    iData.selectType(iApiSelectTitle, false)
//                    iListData.add(iData)
//
//                    mPageTitleStr = iApiSelectTitle
//                    mListData.setRawJson(mPageTitleStr, iListData[0].getRawData())
//
//                    initView()
//                    mProgressDialogCustom?.dismiss()
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    if (e.toString() == "org.json.JSONException: Index 0 out of range [0..0)") {
//                        Log.d("FirebaseFcmErrorMessage", e.toString())
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
//                Log.d("callApiError", t.toString())
//            }
//        })
//    }
//}