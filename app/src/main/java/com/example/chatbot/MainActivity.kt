package com.example.chatbot

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbot.adapters.ChatBotAdapter
import com.example.chatbot.databinding.ActivityMainBinding
import com.example.chatbot.localdata.MessageEntity
import com.example.chatbot.viewmodels.ChatViewModel
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionName
    private lateinit var sessionClient: SessionsClient
    private lateinit var chatBotAdapter: ChatBotAdapter
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        initChatBot()
        setAdapter()

        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString().trim()
            if(message.isNotEmpty()){
                sendMessage(message)
                binding.messageEditText.text = null
            }else{
                Toast.makeText(this,"You have to write a message",Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.bringAllMessages().observe(this, {
            chatBotAdapter.submitList(it)
            chatBotAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.chatRecyclerView.scrollToPosition(chatBotAdapter.itemCount-1)
                }


            })
        })
    }

    /*
    * We will first configure the DialogFlow agent using the JSON key.
    * Then a new session is created using the unique ID and then
    * the bot is made ready to communicate with the user.
    */
    private fun initChatBot(){
        val inputStream = resources.openRawResource(R.raw.tamtam_agent_credentials)
        val credentials = GoogleCredentials.fromStream(inputStream) as ServiceAccountCredentials
        val projectId = credentials.projectId

        val settingsBuilder = SessionsSettings.newBuilder()
        val sessionSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build()
        sessionClient = SessionsClient.create(sessionSettings)
        session = SessionName.of(projectId,UUID.randomUUID().toString())
    }

    private fun sendMessage(userMessage: String){
        val textInput = TextInput.newBuilder().setText(userMessage).setLanguageCode("en-US")
        val queryInput = QueryInput.newBuilder().setText(textInput).build()

        val message = MessageEntity("User","Bot",userMessage,1,Calendar.getInstance().time)
        viewModel.insert(message)

        val job = lifecycleScope.launch {

            val detectIntentResponse = withContext(Dispatchers.IO){
                val detectIntentRequest =
                        DetectIntentRequest.newBuilder()
                                .setSession(session.toString())
                                .setQueryInput(queryInput)
                                .build()

                sessionClient.detectIntent(detectIntentRequest)
            }

            if(detectIntentResponse != null){
                val botReply = detectIntentResponse.queryResult.fulfillmentText

                detectIntentResponse.queryResult.parameters.fieldsMap.forEach {
                    Toast.makeText(this@MainActivity,"${it.value}",Toast.LENGTH_LONG).show()
                }

                val messageBot = MessageEntity("Bot","User",botReply,2,Calendar.getInstance().time)
                viewModel.insert(messageBot)
            }
        }
    }

    private fun setAdapter(){
        lifecycleScope.launch {
            val size = viewModel.getSize()
            chatBotAdapter = ChatBotAdapter()
            binding.chatRecyclerView.apply {
                adapter = chatBotAdapter
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                smoothScrollToPosition(size)
            }
        }

    }

}