package uz.fintech.uzbankcard.ui.codeget

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CodeModel
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

@Suppress("UNREACHABLE_CODE")
class CodeGetRepo {
    companion object {
        private var instanse: CodeGetRepo? = null
        fun codeGetInstanse(): CodeGetRepo? {
            if (instanse == null) {
                instanse = CodeGetRepo()
            } else {
                instanse
            }
            return instanse
        }
    }

    private var stringBuilder = StringBuilder()
    private val list = mutableListOf<Int>()
    private val livedataAdd = MutableLiveData<Int>()
    private val firebaseDB by lazyFast { FirebaseDatabase.getInstance() }
    private val status = MutableLiveData<NetworkStatus>()

    fun codeClear() {
        stringBuilder = StringBuilder()
        list.clear()
    }

    fun codeGetAdd(position: Int): MutableLiveData<Int> {
        list.add(position)
        val pos = stringBuilder.append(stringBuilder.length).length
        livedataAdd.postValue(pos)
        return livedataAdd
    }


    fun onAddFirebase(ctx:Context): MutableLiveData<NetworkStatus> {
        status.postValue(NetworkStatus.Loading)
        val num = list.joinToString("")
        val preference = PreferenceManager.instanse(ctx).isCallNumber
        val databasereferense = firebaseDB.getReference(".info/connected")
        databasereferense.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                status.postValue(NetworkStatus.Error(error.code))
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val isboolean = snapshot.getValue(Boolean::class.java) ?: false
                if (isboolean) {
                    firebaseDB.reference.child("codes").orderByChild("callNum")
                        .startAt("998${preference}")
                        .endAt("998${preference}" + "\uf8ff")
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                status.postValue(NetworkStatus.Error(error.code))
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.children.forEach {
                                    val codeModel = it.getValue(CodeModel::class.java)
                                    if (codeModel!!.code == num) {
                                        status.postValue(NetworkStatus.Success)
                                    }
                                }

                            }
                        })
                } else {
                    status.postValue(NetworkStatus.Offline())
                }
            }
        })
        return status
    }

}