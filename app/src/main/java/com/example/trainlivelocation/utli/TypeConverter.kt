package com.example.trainlivelocation.utli

import android.animation.TypeEvaluator

class TypeConverter : TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String?, endValue: String?): String {
        // Handle the case when startValue is null
        val actualStartValue = startValue ?: ""
        return endValue ?: actualStartValue
    }
}
