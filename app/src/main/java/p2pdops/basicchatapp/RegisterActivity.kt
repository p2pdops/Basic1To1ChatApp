package p2pdops.basicchatapp

import android.animation.Animator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        object : CountDownTimer(10, 1000) {
            override fun onFinish() {
                findViewById<TextView>(R.id.bookITextView).visibility = View.GONE
                findViewById<ProgressBar>(R.id.loadingProgressBar).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.rootView).setBackgroundColor(
                    ContextCompat.getColor(
                        this@RegisterActivity,
                        R.color.colorSplashText
                    )
                )
                findViewById<ImageView>(R.id.bookIconImageView)
                    .setColorFilter(Color.argb(255, 0, 0, 0));
                startAnimation()
            }

            override fun onTick(p0: Long) {}
        }.start()
    }

    private fun register(name: String, email: String, password: String) {
        val data = mapOf(
            "name" to name,
            "email" to email,
            "desc" to "Hey there! I'm using this app"
        )
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user!!.uid
                Log.d(TAG, "register: $uid to $data")
                FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .set(data)
                    .addOnCompleteListener {
                        Log.e(TAG, "register: ${it.isSuccessful} ", it.exception)
                        if (it.isSuccessful) {
                            showPopup("Registration success", "Now can chat with users here!") {
                                startActivity(
                                    Intent(
                                        this@RegisterActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }
                    }.addOnFailureListener {
                        Log.e(TAG, "register: ${it.message}", it)
                    }
            }
            .addOnFailureListener {
                Log.e(TAG, "register: register failed", it)
                showPopup("Registration Failed", it.message ?: "Unknown error")
            }
    }

    private fun startAnimation() {
        findViewById<ImageView>(R.id.bookIconImageView).animate().apply {
            x(50f)
            y(100f)
            duration = 100
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                findViewById<RelativeLayout>(R.id.afterAnimationView).visibility = VISIBLE

                findViewById<View>(R.id.signin_tv).setOnClickListener {
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }

                findViewById<View>(R.id.registerButton).setOnClickListener {
                    val name = findViewById<EditText>(R.id.nameEditText).text.toString()
                    val email = findViewById<EditText>(R.id.emailEditText).text.toString()
                    val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
                    val confPassword =
                        findViewById<EditText>(R.id.confPasswordEditText).text.toString()

//                    if (password != confPassword) {
//                        showPopup("Passwords not matching", "please enter conf. password correctly")
//                        return@setOnClickListener
//                    }

                    register(name, email, password)
                }
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
    }
}