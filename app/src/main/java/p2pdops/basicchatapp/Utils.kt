package p2pdops.basicchatapp

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun Activity.showPopup(title: String, msg: String, cb: (() -> Unit)? = null) {
    runOnUiThread {
        AlertDialog.Builder(this)
            .setTitle(title).setMessage(msg)
            .setPositiveButton("Ok") { dialogInterface, i ->
                dialogInterface.dismiss()
                cb?.invoke()
            }
            .show()
    }
}