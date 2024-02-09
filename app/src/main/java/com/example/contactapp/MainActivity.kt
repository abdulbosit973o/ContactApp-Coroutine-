package com.example.contactapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.contactapp.domain.AppRepository
import com.example.contactapp.utils.MyEventBus
import com.example.contactapp.utils.NetworkStatusValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var repository: AppRepository


    @Inject
    lateinit var networkStatusValidator: NetworkStatusValidator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkStatusValidator.init(
            availableNetworkBlock = {
                repository.syncWithServer().onEach {
                    it.onSuccess {
                        MyEventBus.reloadEvent?.invoke()
                    }.onFailure {message ->
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }.launchIn(lifecycleScope)
            },
            lostConnection = { Toast.makeText(this@MainActivity, "Not connection", Toast.LENGTH_SHORT).show() }
        )
    }
}