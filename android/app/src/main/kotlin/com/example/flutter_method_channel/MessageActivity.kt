package com.example.flutter_method_channel
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        initAppBar()
        initMessagedata()
    }

    fun initAppBar(){
        var actionBar = supportActionBar
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Message to Flutter"
        }
    }

    fun initMessagedata(){
        println("Test message")
        println(intent.extras)
        println(intent.getStringExtra("result"))
        findViewById<EditText>(R.id.etMessage)?.apply {
            setText(intent.getStringExtra("result").toString())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true;
    }

    fun onSendClick(view : View){
        findViewById<EditText>(R.id.etMessage)?.text.let {
            MainActivity.msgresult?.success(it.toString())
            finish()
        }
    }

}