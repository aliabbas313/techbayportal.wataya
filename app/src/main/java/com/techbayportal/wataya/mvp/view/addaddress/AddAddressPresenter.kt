package com.techbayportal.wataya.mvp.view.addaddress

import com.techbayportal.wataya.di.module.SchedulerProvider
import com.techbayportal.wataya.helper.Constant
import com.techbayportal.wataya.helper.DeviceAvailabilityRetryWithDelay
import com.techbayportal.wataya.helper.ResponseCode
import com.techbayportal.wataya.mvp.data.DataManager
import com.techbayportal.wataya.mvp.data.remote.model.request.AddUserRequest
import com.techbayportal.wataya.mvp.view.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.util.*
import javax.inject.Inject

open class AddAddressPresenter<V : AddAddressInterfaces.AddAddressView> @Inject
constructor(
    val mDataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    disposable: CompositeDisposable
) : BasePresenter<V>(schedulerProvider = schedulerProvider, compositeDisposable = disposable),
    AddAddressInterfaces.AddAddressPresenter<Any?> {

    override fun getUserAddresses(device_id: Int, lang: String) {

        mvpView!!.showLoading()

        compositeDisposable.add(
            mDataManager.getUserAddresses(device_id, lang)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(
                    DeviceAvailabilityRetryWithDelay(
                        Constant.MAX_RETRIES,
                        Constant.RETRY_DELAY
                    )
                )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (ResponseCode.isBetweenSuccessRange(it.code)) {
                        mvpView!!.hideLoading()
                        mvpView!!.showData(it)

                        it.let { ti ->
                            ti.data.let { data ->
//                                for (item in data.areasOfCities) {
//                                    databaseHelper.insertData()
//                                }
                                mDataManager.mPreferencesHelper.putString("date", Calendar.getInstance().time.toString())
                            }
                        }

                    } else {
                        mvpView!!.hideLoading()
                        mvpView!!.showError(it.msg[0])
                    }
                }, {
                    if (it is SocketTimeoutException) {
                        mvpView!!.hideLoading()
                        mvpView!!.showError("Sorry our server is currently not available temporarily. Please try again in few minutes")
                    } else {
                        mvpView!!.hideLoading()
                        mvpView!!.showError(it.message.toString())
                    }
                })
        )
    }

    override fun addUserAddress(body: AddUserRequest) {

        mvpView!!.showLoading()

        compositeDisposable.add(
            mDataManager.addUserAddress(body)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(
                    DeviceAvailabilityRetryWithDelay(
                        Constant.MAX_RETRIES,
                        Constant.RETRY_DELAY
                    )
                )
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (ResponseCode.isBetweenSuccessRange(it.code)) {
                        mvpView!!.hideLoading()
                        mvpView!!.showAddUserResponse(it)

                        it.let { ti ->
                            ti.data.let { data ->
//                                for (item in data.areasOfCities) {
//                                    databaseHelper.insertData()
//                                }
                            }
                        }

                    } else {
                        mvpView!!.hideLoading()
                        mvpView!!.showError(it.msg[0])
                    }
                }, {
                    if (it is SocketTimeoutException) {
                        mvpView!!.hideLoading()
                        mvpView!!.showError("Sorry our server is currently not available temporarily. Please try again in few minutes")
                    } else {
                        mvpView!!.hideLoading()
                        mvpView!!.showError(it.message.toString())
                    }
                })
        )
    }


}
