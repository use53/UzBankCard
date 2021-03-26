package uz.fintech.uzbankcard.ui.codesave

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.fintech.uzbankcard.model.CodeModel
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager
import java.lang.StringBuilder

class CodeSaveRepo {

    companion object{
        private var instanse:CodeSaveRepo?=null
        fun codeSaveRP(): CodeSaveRepo {
            if (instanse==null){
                instanse=CodeSaveRepo()
            }else{
                instanse
            }
            return instanse!!
        }
    }
    private val list= mutableListOf<Int>()
    private var stringBuilder=StringBuilder()
    private val firebaseDB=FirebaseDatabase.getInstance()
    val livedata=MutableLiveData<NetworkStatus>()


    fun  listClear(){

        stringBuilder=StringBuilder()
        list.clear()
    }

    fun listAdd(position:Int):MutableLiveData<Int>{
        val liveAddList=MutableLiveData<Int>()
        list.add(position)
        val stringBuilderLenhg=stringBuilder.append(stringBuilder.length)
        liveAddList.postValue(stringBuilderLenhg.length)
        return liveAddList
    }


    fun firebaseSave(ctx:Context): MutableLiveData<NetworkStatus> {
        livedata.postValue(NetworkStatus.Loading)
        val preference =PreferenceManager.instanse(ctx)
        val codes=list.joinToString("")
       // val codeFirebaseModel=CodeFirebaseModel(preference.isCallNumber,code)
            firebaseDB.getReference(".info/connected").addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    livedata.postValue(NetworkStatus.Error(errorMsg = error.message.length))
                     stringBuilder= StringBuilder()
                      list.clear()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val connect=snapshot.getValue(Boolean::class.java)?:false
                    if (connect){
                        livedata.postValue(NetworkStatus.Success)
                         firebaseDB.reference.child("codes")
                             .child(preference.isCallNumber)
                             .setValue(CodeModel(codes,"998${preference.isCallNumber}"))
                        preference.isCodenSave=true
                    }else{
                        livedata.postValue(NetworkStatus.Offline())
                    }
                }
            })
        return livedata

    }

}