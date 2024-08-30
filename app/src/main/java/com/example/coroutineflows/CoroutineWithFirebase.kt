package com.example.coroutineflows

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


data class Person(
    val name:String = "",
    val age:Int = -1,
)
class CoroutineWithFirebase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_with_firebase)

        val tutorialDocument =  Firebase.firestore.collection("coroutine").document("tutorial")
        val peter = Person("Rishikesh",28)
        GlobalScope.launch(Dispatchers.IO) {
            delay(3000L)
            tutorialDocument.set(peter).await()
            val person = tutorialDocument.get().await().toObject(Person::class.java)
            withContext(Dispatchers.Main){
                //set the data on view.Ō
            }
        }
    }
}