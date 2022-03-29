@file:Suppress("PackageName")

package com.example.newtaipeizookotlin.tools

class UtilCommonStr {

    companion object {
        private var mUtilCommonStr: UtilCommonStr? = null

        fun getInstance(): UtilCommonStr {

            mUtilCommonStr?.let {
                return it
            } ?: kotlin.run {
                val iU = UtilCommonStr()
                mUtilCommonStr = iU
                return iU
            }
        }
    }

    val mAnimal = "動物簡介"
    val mPlant = "植物簡介"
    val mInSideArea = "室內區"
    val mOutSideArea = "戶外區"

    val mFirebasePageTitle = "FirebasePageTitle"
}