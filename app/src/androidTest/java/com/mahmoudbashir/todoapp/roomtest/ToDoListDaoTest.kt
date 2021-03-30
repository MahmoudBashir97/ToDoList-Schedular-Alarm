package com.mahmoudbashir.todoapp.roomtest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mahmoudbashir.todoapp.getOrAwaitValue
import com.mahmoudbashir.todoapp.model.DataModel
import com.mahmoudbashir.todoapp.room.TODOLISTDatabase
import com.mahmoudbashir.todoapp.room.ToDoListDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ToDoListDaoTest {

    // this let your test class that go from first method then other ordered
    // one after another
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database:TODOLISTDatabase
    private lateinit var dao:ToDoListDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TODOLISTDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.todoDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertDataListModel() = runBlockingTest{
        val data = DataModel(id = 1,"you are here ","for playing football","27/3/2021","03:24 pm")
        dao.insert(data)
        val getAllData = dao.getAllDataList().getOrAwaitValue()

        assertThat(getAllData).contains(data)
    }

    @Test
    fun deleteDataFromList() = runBlockingTest {
        val data = DataModel(id = 1,"you are here ","for playing football","27/3/2021","03:24 pm")
        dao.insert(data)
        dao.deleteDataFromList(data)
        val getAllData = dao.getAllDataList().getOrAwaitValue()

        assertThat(getAllData).doesNotContain(data)
    }
}