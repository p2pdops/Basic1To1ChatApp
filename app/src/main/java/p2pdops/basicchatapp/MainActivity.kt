package p2pdops.basicchatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        mAuth?.currentUser ?: kotlin.run {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val usersRecycler = findViewById<RecyclerView>(R.id.usersRecycler)
        usersRecycler.setHasFixedSize(true)
        usersRecycler.layoutManager = LinearLayoutManager(this)
        val adapter = UsersAdapter(this)
        usersRecycler.adapter = adapter
        FirebaseFirestore.getInstance().collection("users").get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.map {
                    MUser(
                        uid = it.id,
                        name = it["name"].toString(),
                        desc = it["desc"].toString(),
                        email = it["email"].toString()
                    )
                }
                Log.d(TAG, "onResume: retrieved users: $users")
                runOnUiThread {
                    adapter.setUsers(users.filter { it.uid != mAuth!!.uid!! })
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signOutOption -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}