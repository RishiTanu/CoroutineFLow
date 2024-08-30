package com.example.coroutineflows

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //execute will separate thread dispatchers
        GlobalScope.launch {
            //   delay(3000L)
            // Log.d(TAG, "THREAD 333: ${Thread.currentThread().name}")
            checkAsynchronousTask()
        }

        //execute will main thread
        /*Log.d(TAG, "THREAD 222: ${Thread.currentThread().name}")

        GlobalScope.launch(newSingleThreadContext("MyThread")) {
            Log.d(TAG, "THREAD 111: ${Thread.currentThread().name}")
        }

        //thread context switching
        GlobalScope.launch(Dispatchers.IO) {
            val networkResult = doNetworkCall()
            withContext(Dispatchers.Main){
                Log.d(TAG, "SWIITCH TO IO TO MAIN  THREAD ${networkResult}")
            }
        }*/

    }

    suspend fun doNetworkCall(): String {
        delay(5000)
        return "This is IO thread do long running task"
    }


    private fun doSomething() {
        val parentJob = CoroutineScope(Dispatchers.Main).launch(Dispatchers.Main) {
            Log.d("THREAD", "THREAD 1-: =====${Thread.currentThread().name}")
            val childJob = launch(Dispatchers.IO) {
                Log.d("THREAD", "THREAD 11-: =====${Thread.currentThread().name}")
                async {
                    Log.d("THREAD", "THREAD 111-: =====${Thread.currentThread().name}")
                }
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d("THREAD", "THREAD 2-: =====${Thread.currentThread().name}")
        }
        MainScope().launch(Dispatchers.Main) {
            Log.d("THREAD", "THREAD 3-: =====${Thread.currentThread().name}")
            withContext(Dispatchers.Main) {

            }
        }
    }

    private suspend fun launchJobText() {
        val job = GlobalScope.launch(Dispatchers.IO) {
            repeat(5) {
                Log.d(TAG, "coroutine is still working.")
                delay(1000)
            }
        }
        job.join()
        Log.d(TAG, "launchJobText: continue main thread..............")
    }


    private suspend fun launchJobCancelText() {
        val job = GlobalScope.launch(Dispatchers.IO) {
            withTimeout(4000L) {
                repeat(15) {
                    if (isActive) {
                        Log.d(TAG, "coroutine is still working.")
                        delay(1000)
                    }
                }
            }
        }
        /* delay(2000)
         job.cancel()
         Log.d(TAG, "launchJobText: continue main thread..............")*/
    }


    //
    suspend fun checkAsynchronousTask() {
        GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                val result1 = async { networkCall1() }
                val result2 = async { networkCall2() }

                Log.d(TAG, "checkAsynchronousTask1: ${result1.await()}")
                Log.d(TAG, "checkAsynchronousTask2: ${result2.await()}")
            }
            Log.d(TAG, "checkAsynchronousTask1 time take to execute code $time")
        }
    }

    suspend fun networkCall1(): String {
        delay(3000L)
        return "Network Call 1"
    }

    suspend fun networkCall2(): String {
        delay(3000L)
        return "Network Call 2"
    }
}