package com.example.coroutineflows

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ExceptionInCoroutine : AppCompatActivity() {

    private val TAG = "ExceptionInCoroutine"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception_in_coroutine)

        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.d(TAG, "onCreate======$throwable")
        }
        lifecycleScope.launch(handler) {
            launch {
                throw Exception("Error")
            }
        }
        /* val deferred  = lifecycleScope.launch {
              val string = async {
                  delay(500L)
                  throw Exception("error")
                  "Result"
              }
              Log.d(TAG, "onCreate====${string.await()}")
          }*/

        CoroutineScope(Dispatchers.Main + handler).launch {
            launch {
                delay(300L)
                throw Exception("Error")
            }

            launch {
                delay(400L)
                Log.d(TAG, "onCreate: coroutine finished")
            }
        }


        CoroutineScope(Dispatchers.Main + handler).launch {
            supervisorScope {
                launch {
                    delay(300L)
                    throw Exception("Error")
                }

                launch {
                    delay(400L)
                    Log.d(TAG, "onCreate: coroutine finished")
                }
            }
        }
    }
}