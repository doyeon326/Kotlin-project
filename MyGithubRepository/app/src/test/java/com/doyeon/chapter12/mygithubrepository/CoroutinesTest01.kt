package com.doyeon.chapter12.mygithubrepository

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest01 {

    @Test
    fun test01() = runBlocking {

        val time = measureTimeMillis {
            val firstName = getFirstName()
            val lastName = getLastName()

            print("Hello, $firstName $lastName\n")

        }

        print("measureTimes: $time")
    }

    @Test
    fun test02() = runBlocking {
        val time = measureTimeMillis {
            val firstName = async { getFirstName() }
            val lastName = async { getLastName() }

            print("Hello, ${firstName.await()} ${lastName.await()}\n")
        }
        print("measureTimes: $time")
    }

    suspend fun getFirstName(): String {
        delay(1000)
        return "doyeon"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "kim"
    }
}