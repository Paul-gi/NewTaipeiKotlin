@file:Suppress("PackageName")

package com.example.newtaipeizookotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newtaipeizookotlin.datalist.ListData
import com.example.newtaipeizookotlin.service.RetrofitManager
import com.example.newtaipeizookotlin.service.ZooApiService
import com.example.newtaipeizookotlin.tools.UtilCommonStr
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListPageCallViewModel : ViewModel() {
    private val mDataList: MutableLiveData<ArrayList<ListData>?> =
        MutableLiveData<ArrayList<ListData>?>()
    private val mIsNoData = MutableLiveData<Boolean>()
   // private val mFFF = MutableLiveData<String>()
    private var mIndex = 0
    private var mNotMoreData = false
    private var mGettingData = false
    private var mCall: Call<JsonObject>? = null

    fun getDataListObserver(): MutableLiveData<ArrayList<ListData>?> {
        return mDataList
    }

    fun getDataFinishState(): MutableLiveData<Boolean> {
        return mIsNoData
    }

//    fun getDataFFFFState(): MutableLiveData<String> {
//        return mFFF
//    }

    fun mCallApi(pTitleName: String) {

        if (mNotMoreData) {
            return
        }
        if (mGettingData) {
            return
        }
        synchronized(this) { mGettingData = true }
        val mZooApiService: ZooApiService =
            RetrofitManager().getInstance().createService(ZooApiService::class.java)


        mCall = when (pTitleName) {
            UtilCommonStr.getInstance().mAnimal -> {
                mZooApiService.getAnimalData(50, mIndex)
            }
            UtilCommonStr.getInstance().mPlant -> {
                mZooApiService.getPlantData(50, mIndex)
            }
            else -> {
                mZooApiService.getPavilionData(pTitleName, 50, mIndex)
            }
        }



        mCall?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                try {
                    val iListData: ArrayList<ListData> = ArrayList<ListData>()
                    assert(response.body() != null)
                    val ix = JSONObject(response.body().toString())
                    val iz = ix.getJSONObject("result").getJSONArray("results")
                    for (i in 0 until iz.length()) {
                        val iData = ListData()
                        iData.setData(iz.getJSONObject(i))
                        iData.selectType(pTitleName, false)
                        iListData.add(iData)
                    }
                    mDataList.postValue(iListData)
                    if (iListData.size == 50) {
                        mIndex += 50
                    } else {
                        mNotMoreData = true
                        mIsNoData.postValue(true)
                    }
                    mGettingData = false
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                mDataList.postValue(null)
            }
        })
    }
}