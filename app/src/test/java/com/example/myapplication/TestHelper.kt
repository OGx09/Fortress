package com.example.myapplication

import android.util.SparseArray
import androidx.core.util.forEach
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.data.FortressModel
import com.example.myapplication.data.Icon
import com.example.myapplication.data.WebsiteLogo
import com.example.myapplication.repository.FortressRepository
import com.example.myapplication.repository.WebsiteLogoService
import com.example.myapplication.repository.database.FortressDao
import com.example.myapplication.repository.database.PasswordEntity
import com.example.myapplication.utils.EncryptionUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.crypto.Cipher
import javax.crypto.SecretKey

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

    if (latch.await(time, timeUnit)){
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

class FortressDaoMock : FortressDao{
    private val fakeDb = SparseArray<PasswordEntity>()

    override fun getAllEncryptedPassword(): LiveData<List<PasswordEntity>> {
        val resultLiveData = MutableLiveData<List<PasswordEntity>>()
        val passwordList: MutableList<PasswordEntity> = mutableListOf()
        fakeDb.forEach { key, value ->  passwordList.add(value) }
        resultLiveData.value = passwordList
        return resultLiveData
    }

    override fun getPasswordDetails(id: Int): LiveData<PasswordEntity> {
        val passwordLiveData = MutableLiveData<PasswordEntity>()
        passwordLiveData.value = fakeDb.get(id)
       return passwordLiveData
    }

    override suspend fun getEncryptedEntity(id: Int): String {
        val encryptedString = fakeDb.get(id)?.encryptedData
        return encryptedString!!
    }

    override suspend fun insert(passwordEntity: PasswordEntity) {
        fakeDb.put(fakeDb.size() +1, passwordEntity)
    }

    override suspend fun insertEncryptedEntity(passwordEntity: PasswordEntity) {
        fakeDb.put(fakeDb.size() +1, passwordEntity)
    }

    override suspend fun delete(passwordEntity: PasswordEntity) {
        passwordEntity.id?.apply {
            fakeDb.remove(this)
        }
    }

}

class EncryptionUtilsMock (private val fortressDao: FortressDao) : EncryptionUtils{

    override fun generateSecretKey() {}

    override fun getSecretKey(): SecretKey {
        TODO("Not yet implemented")
    }

    override fun getCipher(): Cipher {
        TODO("Not yet implemented")
    }

    override suspend fun decryptSecretInformation(cipher: Cipher, id: Int): FortressModel? {
        TODO("Not yet implemented")
    }

    override suspend fun
            encryptSecretInformation(cipher: Cipher, passwordEntity: PasswordEntity) {

    }

    override fun getDao(): FortressDao {
        return fortressDao
    }

}

class RepositoryMock(private val encryptionUtils: EncryptionUtils,
                     private val websiteLogoService: WebsiteLogoService,
                     private val dispatcher: CoroutineDispatcher,
                     private val datastore: DataStore<Preferences>
) : FortressRepository {
    override fun fetchAllEncryptedPasswords(): LiveData<List<PasswordEntity>> {

    }

    override suspend fun fetchPasswordDetails(cipher: Cipher, id: Int): FortressModel {
        val fortressModel = FortressModel("123456778", "Gbenga", "Nothing", "m")
        return fortressModel
    }

    override suspend fun removePassword(passwordEntity: PasswordEntity) {

    }

    override suspend fun savePassword(cipher: Cipher, passwordEntity: PasswordEntity) {

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

    }

    override suspend fun fetchUsername(): Flow<String?> {
        return flow {
            emit("Gbenga")
        }
    }

}