package p2pdops.basicchatapp

import com.google.firebase.Timestamp

data class MUser(
    var uid: String? = null,
    var name: String,
    var email: String,
    var desc: String,
)

data class MMessage(
    var message: String = "",
    var fromUid: String = "",
    var uids: List<String> = listOf(),
    var timestamp: Timestamp = Timestamp.now()
)