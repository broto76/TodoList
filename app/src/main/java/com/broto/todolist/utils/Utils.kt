package com.broto.todolist.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    activity.currentFocus.let {
        inputManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}