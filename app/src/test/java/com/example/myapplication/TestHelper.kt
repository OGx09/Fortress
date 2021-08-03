package com.example.myapplication

import android.security.keystore.KeyProperties
import android.util.SparseArray
import androidx.core.util.forEach
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.Icon
import com.example.myapplication.data.WebsiteLogo
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.FortressRepositoryImpl
import com.example.myapplication.repository.database.FortressDao
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.EncryptionUtils
import kotlinx.coroutines.CoroutineDispatcher
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

    override fun getPasswordDetails(id: Int): LiveData<PasswordEntity> {
        val passwordLiveData = MutableLiveData<PasswordEntity>()
        passwordLiveData.value = _fakeDb[id]
        print("Read: ${_fakeDb[id]}\n")
       return passwordLiveData
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

class EncryptionUtilsMock (private val fortressDao: FortressDao) : EncryptionUtils{

    //Stolen from Android source code in CipherInputStreamTest -_*
    private val aesKeyBytes = byteArrayOf(
        0x50.toByte(),
        0x98.toByte(),
        0xF2.toByte(),
        0xC3.toByte(),
        0x85.toByte(),
        0x23.toByte(),
        0xA3.toByte(),
        0x33.toByte(), 0x50.toByte(), 0x98.toByte(), 0xF2.toByte(), 0xC3.toByte(),
        0x85.toByte(), 0x23.toByte(), 0xA3.toByte(), 0x33.toByte()
    )

    override fun generateSecretKey() {}

    override fun getSecretKey(): SecretKey {
       return SecretKeySpec(aesKeyBytes, "RC4")
    }

    override fun getCipher(): Cipher {
        return Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
    }

    override suspend fun decryptSecretInformation(cipher: Cipher?, id: Int): FortressModel? {
        return if (cipher != null){
            null
        }else{
            try {
                fortressDao.getEncryptedEntity(id) // TODO: Add decytpyion logic
                FortressModel("12345678", "OGX09",
                    "HELLO NO INFO", "http://hello.com")
            }catch (e: Exception){
                null
            }

        }
    }

    override suspend fun
            encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity) {

    }

    override fun getDao(): FortressDao {
        return fortressDao
    }

}

open class RepositoryMock(private val encryptionUtils: EncryptionUtils,
                     //private val websiteLogoService: WebsiteLogoService,
                     private val dispatcher: CoroutineDispatcher,
                     private val datastore: DataStore<Preferences>
) : FortressRepository {


    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {
        return encryptionUtils.getDao().getAllEncryptedPassword()
    }

    override suspend fun fetchPasswordDetails(cipher: Cipher, id: Int): FortressModel {
        return FortressModel("123456778", "Gbenga", "Nothing", "m")
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) {
        encryptionUtils.getDao().delete(passwordEntity)
    }

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity) {
        encryptionUtils.getDao().insertEncryptedEntity(passwordEntity)
    }

    override suspend fun fetchwebsiteIcon(websiteUrl: String): WebsiteLogo {
        return WebsiteLogo().apply {
            icons = mutableListOf<Icon>().apply {
                val icon = Icon()
                icon.bytes =100
                icon.url = "http://"
                for (i in 0..2){
                    add(icon)
                }
            }
            url = "http://"
        }
    }

    override suspend fun saveToDataStore(value: String) {
        datastore.edit {settings ->
            settings[FortressRepositoryImpl.DATASTORE_USERNAME] = value
        }
    }

    override suspend fun fetchUsername() =flow {
        emit("Gbenga")
    }

}