package com.codek.cleantodoapp.viewModel.todo

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.codek.cleantodoapp.ViewModelTest
import com.codek.cleantodoapp.data.entity.ToDoEntity
import com.codek.cleantodoapp.domain.todo.GetToDoItemUseCase
import com.codek.cleantodoapp.domain.todo.InsertToDoListUseCase
import com.codek.cleantodoapp.presentation.list.ListViewModel
import com.codek.cleantodoapp.presentation.list.ToDoListState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject

/**[ListViewModel]을 테스트하기위한 Unit Test Class
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test Item update
 * 4. test Item Delete All
 *
 * **/

@ExperimentalCoroutinesApi
internal class ListViewModelTest : ViewModelTest() {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel: ListViewModel by inject()
    private val insertTodoListUseCase: InsertToDoListUseCase by inject()
    private val getToDoItemUseCase: GetToDoItemUseCase by inject()

    private val mockList = (0 until 10).map{
       ToDoEntity(
           id = it.toLong(),
           title = "title $it",
           description = "description $it",
           hasCompleted = false

       )
   }

    /**
     * 필요한 Usecase들
     * 1. InsertTodoListUseCase
     * 2. GetTodoItemUseCase
     *
     * **/
    @Before
    fun init() {
        initData()
    }

    private fun initData() = runTest {
        insertTodoListUseCase(mockList)
    }

    //Test: 입력된 데이터를 불러와서 검증한다.
    @Test
    fun `test viewModel fetch`(): Unit = runTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
//                ToDoListState.UnInitialized,
//                ToDoListState.Loading,
//                ToDoListState.Success(mockList)
            )
        )
    }

    //Test: 데이터를 업데이트 했을때 잘 반영 되는가
    @Test
    fun `text Item Update`(): Unit = runTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        if (getToDoItemUseCase(todo.id)?.hasCompleted == true) {

        } else {

        }
       // assert(getToDoItemUseCase(todo.id)?.hasCompleted ?: false == todo.hasCompleted)
    }

    //Test: 데이터를 다 날렸을 때 빈 상태로 보여지는가
    @Test
    fun `test Item Delete All`(): Unit = runTest {
        val testObservable = viewModel.todoListLiveData.test()

        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                ToDoListState.UnInitialized,
                ToDoListState.Loading,
                ToDoListState.Success(listOf())
            )
        )
    }

    //Test: 데이터를 업데이트 했을 때 잘 반영 되는가
    @Test
    fun `test Item Update`(): Unit = runTest {
        val todo = ToDoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
    }

}