package com.example.livechat

import android.app.usage.UsageEvents.Event
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.example.livechat.data.CHATS_NODE
import com.example.livechat.data.ChatData
import com.example.livechat.data.ChatUser
import com.example.livechat.data.USER_NODE
import com.example.livechat.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {


    var inProgress = mutableStateOf(false)
    var inProcessChats = mutableStateOf(false)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    var eventMutableState = mutableStateOf("")
    val chats = mutableStateOf<List<ChatData?>>(listOf())

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun populateChats() {
        inProgress.value = true
        db.collection(CHATS_NODE).where(
            Filter.or(
                Filter.equalTo("user1.userId",userData.value?.userId),
                Filter.equalTo("user2.userId",userData.value?.userId)

        )
        ).addSnapshotListener{
            value , error->
            if (error!=null){
                handleException(error)

        }
            if (value!=null){
                chats.value=value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProgress.value=false
            }
        }
    }

    fun signUp(name: String, number: String, email: String, password: String) {
        inProgress.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
            return
        }
        inProgress.value = false
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    inProgress.value = false
                    if (task.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                        Log.d("TAG", "signUp: user logged in")
                    } else {
                        handleException(task.exception, "signUp failed")
                    }
                }
            } else {
                handleException(customMessage = "number Already Exists")
                inProgress.value = false
            }
        }

    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill the all fields")
            return
        } else {
            inProgress.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        inProgress.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(customMessage = "Login Failed", exception = it.exception)
                    }
                }
        }
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) {
            createOrUpdateProfile(imageUrl = it.toString())
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProgress.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()

        val imageRef = storageRef.child("image/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {
            val result = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener(onSuccess)
            inProgress.value = false

        }
            .addOnFailureListener {
                handleException()
            }
    }

    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        Log.d("TAG", "name: $name, number $number, uid: $uid")
        val userDataInstance = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl,
        )
        inProgress.value = true
        db.collection(USER_NODE).document(uid.toString()).get().addOnSuccessListener { document ->
            Log.d("TAG", "Profile updated successfully $document")

            db.collection(USER_NODE).document(uid.toString()).set(userDataInstance)

            getUserData(uid.toString())
        }.addOnFailureListener { error ->
            handleException(error, "Cannot Retrieve User")
        }
    }

    private fun getUserData(uid: String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { documentSnapshot, error ->
            Log.e("TAG", "Error updating profile", error)

            inProgress.value = false
            error?.let {
                handleException(it, "Cannot retrieve user")
                return@addSnapshotListener
            }
            documentSnapshot?.toObject<UserData>()?.let { user ->
                userData.value = user
            }
            populateChats()
        }
    }

    private fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("TAG", "Live Chat exception: ", exception)
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isEmpty()) errorMsg else customMessage
        Log.e("TAG", message)
    }

    fun logout() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutableState.value = "logged out"

    }

    fun onAddChat(number: String) {
        if (number.isEmpty() or !number.isDigitsOnly()) {
            handleException(customMessage = "Number must be contain digits only")
        } else {
            db.collection(CHATS_NODE).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1 number", number),
                        Filter.equalTo("User2. number", userData.value?.number)

                    ), Filter.and(
                        Filter.equalTo("User2. number", userData.value?.number),
                        Filter.equalTo("user1 number", number),

                        )

                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    db.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                handleException(customMessage = "number not found")
                            } else {
                                val chatPartner = it.toObjects<UserData>()[0]
                                val id = db.collection(CHATS_NODE).document().id
                                val chat = ChatData(
                                    chatId = id,
                                    user1 = ChatUser(
                                        userData.value?.userId,
                                        userData.value?.name,
                                        userData.value?.imageUrl,
                                        userData.value?.number
                                    ),
                                    user2 = ChatUser(
                                        chatPartner.userId,
                                        chatPartner.name,
                                        chatPartner.imageUrl,
                                        chatPartner.number
                                    )
                                )

                                db.collection(CHATS_NODE).document(id).set(chat)
                            }
                        }
                        .addOnFailureListener {
                            handleException(it)
                        }
                } else {
                    handleException(customMessage = "chat already exist")
                }
            }
        }
    }


}
