package com.example.myapplication

import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.data.SecretDataWrapper
import com.example.myapplication.data.Icon
import com.example.myapplication.data.WebsiteLogo
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.FortressRepositoryImpl
import com.example.myapplication.repository.database.CipherTextWrapper
import com.example.myapplication.repository.database.FortressDao
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.EncryptionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

// Created by Gbenga Oladipupo(Devmike01) on 7/31/21.


fun <T> LiveData<T>.getAwaitValue(time: Long = 2,
                                  timeUnit: TimeUnit = TimeUnit.SECONDS): T{

    var data: T? =null
    val latch = CountDownLatch(1)
    var observer: Observer<T>? = null
    observer = Observer<T> { o ->
        data =o
        latch.countDown()
        observer?.apply {
            this@getAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)){
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

class FortressDaoMock : FortressDao{

    private val _fakeDb = HashMap<Int, PasswordEntity>()

    val clear = _fakeDb.clear()

    override fun getAllEncryptedPassword(): LiveData<List<PasswordEntity>> {
        val resultLiveData = MutableLiveData<List<PasswordEntity>>()
        val passwordList: MutableList<PasswordEntity> = mutableListOf()
        _fakeDb.forEach { (_, value) ->  passwordList.add(value) }
        resultLiveData.value = passwordList
        return resultLiveData
    }

    override fun getPasswordDetails(id: Int): PasswordEntity {
        val passwordLiveData = MutableLiveData<PasswordEntity>()
        val passwordEntity = _fakeDb[id]
       return passwordEntity!!
    }

    override suspend fun getEncryptedEntity(id: Int): String {
        val encryptedString = _fakeDb[id]?.encryptedData
        return encryptedString!!
    }

    override suspend fun insert(passwordEntity: PasswordEntity) {
        print("Insert: ${passwordEntity.website}\n")
        _fakeDb[_fakeDb.size +1] = passwordEntity
    }

    override suspend fun insertEncryptedEntity(passwordEntity: PasswordEntity) {
        _fakeDb[_fakeDb.size +1] = passwordEntity
    }

    override suspend fun delete(passwordEntity: PasswordEntity) {
        passwordEntity.id?.apply {
            _fakeDb.remove(this)
        }
    }

}


//class DataStoreMock: DataStore<Preferences>{
//    override val data: Flow<Preferences>
//        get() = flow {
//           emit()
//        }
//
//    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences):
//            Preferences {
//        return Preferences.toMutablePreferences()
//    }
//
//
//    public suspend fun DataStore<Preferences>.edit(
//        transform: suspend (MutablePreferences) -> Unit
//    ): Preferences {
//        return this.updateData {
//            // It's safe to return MutablePreferences since we freeze it in
//            // PreferencesDataStore.updateData()
//            it.toMutablePreferences().apply { transform(this) }
//        }
//    }
//
//}


open class RepositoryMock(private val encryptionUtils: EncryptionUtils,
                     //private val websiteLogoService: WebsiteLogoService,
                     private val dispatcher: CoroutineDispatcher
) : FortressRepository {

    val countDownLatch = CountDownLatch(10)

    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPasswordDetails(id: Int, cipher: Cipher): PasswordEntity? {
        TODO("Not yet implemented")
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchwebsiteIcon(websiteUrl: String): WebsiteLogo {
        TODO("Not yet implemented")
    }

    override suspend fun saveToDataStore(value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCipherTextFromDb(id: Int): ByteArray {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUsername(): Flow<String?> = flow{
        countDownLatch.countDown()
        emit("Gbenga")
    }

    override suspend fun decryptDbCiperText(id: Int): CipherTextWrapper {
        TODO("Not yet implemented")
    }

    override suspend fun persistInDbAsCipherText(passwordEntity: PasswordEntity) {
        TODO("Not yet implemented")
    }


}