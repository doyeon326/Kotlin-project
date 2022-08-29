package com.codek.cleantodoapp.di

import com.codek.cleantodoapp.data.repository.TestTodoRepository
import com.codek.cleantodoapp.data.repository.TodoRepository
import com.codek.cleantodoapp.domain.todo.*
import com.codek.cleantodoapp.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    //ViewModel
    viewModel{ ListViewModel(get(), get() , get()) }
    //UseCase
    factory { GetToDoListUseCase( get() ) }
    factory { InsertToDoListUseCase( get() ) }
    factory { UpdateToDoListUseCase( get()) }
    factory { GetToDoItemUseCase( get()) }
    factory { DeleteAllToDoItemUseCase(get()) }

    //Repository
    single<TodoRepository> { TestTodoRepository() }

}