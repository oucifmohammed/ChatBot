package com.example.chatbot.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.chatbot.R
import com.example.chatbot.localdata.MessageEntity

class ChatBotAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userSender = 1
    private val botSender = 2

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageEntity>() {

        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return newItem == oldItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType==userSender){
            MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.user_message,
                    parent,
                    false
                ),
                interaction
            )
        }else {
            MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.bot_message,
                    parent,
                    false
                ),
                interaction
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<MessageEntity>) {
        differ.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return if(differ.currentList[position].senderId == 1){
            userSender
        }else{
            botSender
        }
    }

    class MessageViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MessageEntity) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            val messageView: TextView = itemView.findViewById(R.id.hhhh)
            messageView.text = item.message
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: MessageEntity)
    }
}