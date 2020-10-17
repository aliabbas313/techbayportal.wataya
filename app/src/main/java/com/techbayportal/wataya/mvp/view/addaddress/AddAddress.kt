package com.techbayportal.wataya.mvp.view.addaddress

import android.os.Bundle
import android.view.View
import javax.inject.Inject
import android.view.ViewGroup
import com.techbayportal.wataya.R
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.techbayportal.wataya.mvp.view.base.BaseActivity
import com.techbayportal.wataya.mvp.view.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_add_address.*
import com.techbayportal.wataya.mvp.data.remote.model.BaseModel
import com.techbayportal.wataya.mvp.data.remote.model.request.AddUserRequest
import com.techbayportal.wataya.mvp.data.remote.model.response.AddUserResponse
import com.techbayportal.wataya.mvp.data.remote.model.response.UserData
import com.techbayportal.wataya.util.widget.Dialog
import kotlinx.android.synthetic.main.fragment_add_address.view.*

class AddAddress(private var lifecycleOwner: LifecycleOwner) : BaseFragment(), AddAddressInterfaces.AddAddressView {

    @Inject
    lateinit var mAddAddressPresenter: AddAddressPresenter<AddAddressInterfaces.AddAddressView>

    private var city_selected_id: Int = 0
    private var area_selected_id: Int = 0

    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_add_address, container, false)

        mAddAddressPresenter.onAttach(this)

        mAddAddressPresenter.getUserAddresses(123, "en")

        rootView.submit_btn.setOnClickListener {
            var addUserRequest = AddUserRequest(
                (activity as BaseActivity).currentLocation.value!!.latitude,
                (activity as BaseActivity).currentLocation.value!!.longitude,
                building_name.text.toString(),
                apartment_name.text.toString(),
                street_address.text.toString(),
                area_selected_id,
                city_selected_id,
                "1234",
                "en")

            mAddAddressPresenter.addUserAddress(addUserRequest)

        }
    // Inflate the layout for this fragment
        return rootView
    }

    override fun showData(data: BaseModel<UserData>) {

        var cities: ArrayList<Int> = ArrayList()
        var areas: ArrayList<Int> = ArrayList()

        var citiesList: ArrayList<String> = ArrayList()
        var areasList: ArrayList<String> = ArrayList()

        val citySelected: MutableLiveData<Boolean> = MutableLiveData()

        data.data!!.cities.forEach {
            citiesList.add(it.name)
            cities.add(it.cityId)
        }

        city_spinner.adapter = ArrayAdapter<String>(
            view!!.context,
            android.R.layout.simple_list_item_1, citiesList
        )
        (city_spinner.adapter as ArrayAdapter<*>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        city_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                city_selected_id = cities[position]
                citySelected.value = true
            }
        }

        citySelected.observe(lifecycleOwner, androidx.lifecycle.Observer {
            areasList.clear()
            data.data!!.areasOfCities.forEach {
                if (it.cityId == city_selected_id) {
                    areasList.add(it.name)
                    areas.add(it.areaId)
                }
            }
            area_spinner.adapter = ArrayAdapter<String>(
                view!!.context,
                android.R.layout.simple_list_item_1, areasList
            )
            (area_spinner.adapter as ArrayAdapter<*>).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            area_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    area_selected_id = areas[position]
                }
            }
        })
    }

    override fun showAddUserResponse(data: BaseModel<AddUserResponse>) {
        var dial: AlertDialog?=null

        activity?.let {
            dial = Dialog.build((it as BaseActivity),
                data.data!!.apartment+", "+data.data!!.buildingName+", "+data.data!!.streetAddress,
                "ok",
                View.OnClickListener {
                    dial!!.dismiss()
                }, true)
            dial!!.show()
        }
    }

    override fun showError(error: String) {
    }

    override fun showLoading() {
        (activity as BaseActivity).progress(true)
    }

    override fun hideLoading() {
        (activity as BaseActivity).progress(false)

    }

}