package com.techbayportal.wataya.mvp.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techbayportal.wataya.di.injection.Injectable

abstract class BaseFragment : Fragment(), Injectable {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}