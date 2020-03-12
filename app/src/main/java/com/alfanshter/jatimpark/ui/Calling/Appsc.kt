package com.alfanshter.jatimpark.ui.Calling

import android.annotation.SuppressLint
import android.app.Application
import com.sinch.android.rtc.Sinch
import com.sinch.android.rtc.SinchClient
import com.sinch.android.rtc.calling.CallClient

@SuppressLint("Registered")
class Appsc : Application() {
    override fun onCreate() {
        super.onCreate()
        USER_ID = ("" + "5551").replace("-", "")
        sinchClient = Sinch.getSinchClientBuilder().context(this)
            .applicationKey("e5178df1-dfff-474e-9194-1e775ed10632")
            .applicationSecret("hHHdDN9Sh0GmSbyR2PtxWQ==")
            .environmentHost("clientapi.sinch.com")
            .userId(USER_ID)
            .build()
        sinchClient.setSupportActiveConnectionInBackground(true)
        sinchClient.startListeningOnActiveConnection()
        sinchClient.setSupportCalling(true)
    }

    companion object {
        lateinit var USER_ID: String
        lateinit var sinchClient: SinchClient
        lateinit var callClient: CallClient
    }
}
