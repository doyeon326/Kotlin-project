package com.doyeon.chapter14.deliveryapplication.screen.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    //내부적으로 데이터 관리
    protected var stateBundle: Bundle? = null
    //상태를 번들형태로 관리를 해줄수있지만 이번엔 koin 을 사용할거기때문에 안드로이드 프러그먼트 라이프사이클에 따라 관리할수있도록 사용

    open fun fetchData(): Job = viewModelScope.launch {

    }

    open fun storeState(stateBundle: Bundle) {
        this.stateBundle = stateBundle
        //프러그먼트 라이프사이클이 종료가 되기전까지 이 상태를 유지함.
    }
}