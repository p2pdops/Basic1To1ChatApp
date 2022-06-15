package p2pdops.basicchatapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UsersHolder(view: View) : RecyclerView.ViewHolder(view)

class UsersAdapter(val context: Context) : RecyclerView.Adapter<UsersHolder>() {
    var usersList = ArrayList<MUser>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersHolder = UsersHolder(
        LayoutInflater.from(context).inflate(R.layout.chat_row, parent, false).rootView
    )

    override fun onBindViewHolder(holder: UsersHolder, position: Int) {
        val user = usersList[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.name_tv).text = user.name
            findViewById<TextView>(R.id.desc_tv).text = user.desc
        }.setOnClickListener {
            context.startActivity(
                Intent(context, MessagesActivity::class.java)
                    .putExtra("uid", user.uid)
                    .putExtra("name", user.name)
            )
        }
    }

    override fun getItemCount(): Int = usersList.size

    fun setUsers(users: List<MUser>) {
        usersList.clear()
        usersList.addAll(users)
        notifyDataSetChanged()
    }
}


class MessagesHolder(view: View) : RecyclerView.ViewHolder(view)

class MessagesAdapter(val context: Context, val myUid: String) :
    RecyclerView.Adapter<MessagesHolder>() {
    var messagesList = ArrayList<MMessage>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder =
        MessagesHolder(
            LayoutInflater.from(context).inflate(R.layout.message_row, parent, false).rootView
        )

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        val mMessage = messagesList[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.message_tv).text = mMessage.message
            val dtfmt = SimpleDateFormat("hh:mm:ss a | dd MMM, yyyy", Locale.getDefault())
            findViewById<TextView>(R.id.time_tv).text = dtfmt.format(mMessage.timestamp.toDate())
            findViewById<ImageView>(R.id.from_iv).setImageResource(
                when (mMessage.fromUid) {
                    myUid -> R.drawable.ic_baseline_chevron_right_24
                    else -> R.drawable.ic_baseline_chevron_left_24
                }
            )
        };
    }

    override fun getItemCount(): Int = messagesList.size

    fun setMessages(messages: List<MMessage>) {
        messagesList.clear()
        messagesList.addAll(messages)
        notifyDataSetChanged()
    }
}