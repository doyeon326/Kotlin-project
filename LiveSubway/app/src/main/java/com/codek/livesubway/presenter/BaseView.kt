package com.codek.livesubway.presenter

interface BaseView<PresenterT: BasePresenter> {
    val presenter: PresenterT
}