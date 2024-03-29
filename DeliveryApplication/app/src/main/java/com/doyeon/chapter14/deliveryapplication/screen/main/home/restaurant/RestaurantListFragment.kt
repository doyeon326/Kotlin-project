package com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant

import android.util.Log
import androidx.core.os.bundleOf
import com.doyeon.chapter14.deliveryapplication.data.entity.LocationLatLngEntity
import com.doyeon.chapter14.deliveryapplication.databinding.FragmentRestaurantListBinding
import com.doyeon.chapter14.deliveryapplication.model.restaurant.RestaurantModel
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseFragment
import com.doyeon.chapter14.deliveryapplication.screen.main.home.restaurant.detail.RestaurantDetailActivity
import com.doyeon.chapter14.deliveryapplication.util.provider.ResourcesProvider
import com.doyeon.chapter14.deliveryapplication.widget.adapter.ModelRecyclerAdapter
import com.doyeon.chapter14.deliveryapplication.widget.adapter.listener.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment: BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>(){
    //햇갈리는 부분 https://stackoverflow.com/questions/56436705/how-to-add-a-fragments-argument-item-to-koin-dependency-graph
    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    private val locationLatLng by lazy { arguments?.getParcelable<LocationLatLngEntity>(LOCATION_KEY)}

    override val viewModel by viewModel<RestaurantListViewModel>{ parametersOf(restaurantCategory, locationLatLng)}

    override fun getViewBinding(): FragmentRestaurantListBinding = FragmentRestaurantListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()
    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantListListener {
                override fun oncClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(
                            requireContext(),
                            model.toEntity()
                        )
                    )
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {

        Log.e("restaurantList", it.toString())
        adapter.submitList(it)
    }



    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        const val RESTAURANT_KEY = "Restaurant"

        fun newInstance(
            restaurantCategory: RestaurantCategory,
            locationLatLng: LocationLatLngEntity
        ): RestaurantListFragment {
            return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory,
                    LOCATION_KEY to locationLatLng
                )
            }
        }
    }
}
