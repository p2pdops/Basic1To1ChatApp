package p2pdops.basicchatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MessagesActivity : AppCompatActivity() {

    lateinit var frndUid: String
    lateinit var myUid: String
    lateinit var adapter: MessagesAdapter

    private val TAG = "MessagesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        myUid = FirebaseAuth.getInstance().uid!!
        frndUid = intent.getStringExtra("uid")!!
        adapter = MessagesAdapter(this, myUid)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("name")!!

        val messagesRecycler = findViewById<RecyclerView>(R.id.messagesRecycler)
        messagesRecycler.setHasFixedSize(true)
        messagesRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        messagesRecycler.adapter = adapter
        refreshMessages()
        findViewById<View>(R.id.sendBtn).setOnClickListener {
            val msg = findViewById<EditText>(R.id.message_et).text.toString()
            if (msg.isNotEmpty()) FirebaseFirestore.getInstance()
                .collection("messages")
                .add(
                    MMessage(
                        message = msg,
                        fromUid = myUid,
                        uids = listOf(myUid, frndUid),
                        timestamp = Timestamp.now()
                    )
                ).addOnSuccessListener {
                    runOnUiThread {
                        refreshMessages()
                        findViewById<EditText>(R.id.message_et).setText("")
                    }
                }
        }
    }

    private fun refreshMessages() {
        FirebaseFirestore.getInstance().collection("messages")
            .whereArrayContainsAny("uids", listOf(FirebaseAuth.getInstance().uid!!, frndUid))
            .get().addOnSuccessListener { snapshot ->
                val messages = snapshot.documents.map { it.toObject(MMessage::class.java)!! }
                val filteredMsgs = messages.filter { it.uids.containsAll(listOf(myUid, frndUid)) }
                    .sortedByDescending { it.timestamp.toDate().time }
                Log.d(TAG, "onResume: retrieved messages: $messages")
                runOnUiThread {
                    adapter.setMessages(filteredMsgs)
                }
            }
    }
}