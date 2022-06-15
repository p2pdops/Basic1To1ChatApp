package p2pdops.basicchatapp

import android.animation.Animator
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        object : CountDownTimer(500, 1000) {
            override fun onFinish() {
                findViewById<TextView>(R.id.bookITextView).visibility = View.GONE
                findViewById<ProgressBar>(R.id.loadingProgressBar).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.rootView).setBackgroundColor(
                    ContextCompat.getColor(
                        this@LoginActivity,
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

    private fun startAnimation() {
        findViewById<ImageView>(R.id.bookIconImageView).animate().apply {
            x(50f)
            y(100f)
            duration = 250
        }.setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                findViewById<RelativeLayout>(R.id.afterAnimationView).visibility = VISIBLE
                findViewById<View>(R.id.signup_tv).setOnClickListener {
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                    finish()
                }
                findViewById<Button>(R.id.loginButton).setOnClickListener {
                    val email = findViewById<EditText>(R.id.emailEditText).text.toString()
                    val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
                    if (email.isNotEmpty() && password.isNotEmpty()) login(email, password)
                    else showPopup("Enter fields", "")
                }
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                showPopup("Login success", "") {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                }
            }
            .addOnFailureListener {
                showPopup("Login Failed", it.message ?: "Unknown error")
            }
    }

}