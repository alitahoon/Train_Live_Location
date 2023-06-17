package com.example.trainlivelocation.utli

import android.util.Property
import android.widget.TextView

class TextProperty : Property<TextView, String>(String::class.java, "text") {
    override fun get(`object`: TextView): String {
        return `object`.text.toString()
    }

    override fun set(`object`: TextView, value: String) {
        `object`.text = value
    }
}
