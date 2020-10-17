package com.techbayportal.wataya.util.widget

import android.view.View
import android.content.Context
import com.techbayportal.wataya.R
import android.view.LayoutInflater
import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("StaticFieldLeak")
object Dialog {

    var mBuilder: AlertDialog.Builder? = null
    var dialog: AlertDialog? = null

    lateinit var mView: View

    fun build(mContext: Context, message: String, btnOne: String, btnOnelistener: View.OnClickListener, isCancleable: Boolean): AlertDialog {
        mBuilder = AlertDialog.Builder(mContext)

        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mView = inflater.inflate(R.layout.dialog, null)
        var messge = mView.findViewById(R.id.tv_error_message) as AppCompatTextView
        var btnConfirm = mView.findViewById(R.id.btnConfirm) as AppCompatButton

        messge.text = message
        btnConfirm.text = btnOne

        btnConfirm.setOnClickListener(btnOnelistener)

        mBuilder!!.setView(mView)
        mBuilder!!.setCancelable(isCancleable)
        dialog = mBuilder!!.create()
        return dialog!!
    }

}