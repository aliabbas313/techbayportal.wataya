package com.techbayportal.wataya.di.module

import com.techbayportal.wataya.mvp.view.addaddress.AddAddress
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeAddAddress(): AddAddress
}
