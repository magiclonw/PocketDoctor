package com.magiclon.pocketdoctor.tools

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


/**
 * Created by yxm on 16-6-23.
 */
class DensityUtil private constructor() {

    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        /**
         * dp转px
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dpVal, context.resources.displayMetrics).toInt()
        }

        /**
         * sp转px
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    spVal, context.resources.displayMetrics).toInt()
        }

        /**
         * px转dp
         */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }
    }
}