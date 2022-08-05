package com.doyeon.chapter14.deliveryapplication.screen.main.home.restraurant

import android.util.Log
import androidx.core.os.bundleOf
import com.doyeon.chapter14.deliveryapplication.databinding.FragmentRestaurantListBinding
import com.doyeon.chapter14.deliveryapplication.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment: BaseFragment<RestaurantListViewModel, FragmentRestaurantListBinding>(){

    private val restaurantCategory by lazy { arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory }
    override val viewModel by viewModel<RestaurantListViewModel>{ parametersOf(restaurantCategory)}

    override fun getViewBinding(): FragmentRestaurantListBinding = FragmentRestaurantListBinding.inflate(layoutInflater)

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"

        fun newInstance(restaurantCategory: RestaurantCategory): RestaurantListFragment {
            return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory
                )
            }
        }
    }
}
