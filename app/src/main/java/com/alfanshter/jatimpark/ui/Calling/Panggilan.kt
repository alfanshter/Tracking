package com.alfanshter.jatimpark.ui.Calling


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.alfanshter.jatimpark.IncommingCallActivity
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.SinchService
import com.alfanshter.jatimpark.SinchStatus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.startService


/**
 * A simple [Fragment] subclass.
 */
class Panggilan : Fragment(), View.OnClickListener {

    private  var mMainMyid: TextView? = null
    private var mMainCallbtn: Button? = null
    private var mMainTargetid: EditText? = null
    private var mMainStatus: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
val root =  inflater.inflate(R.layout.fragment_panggilan, container, false)

        mMainMyid = root.findViewById(R.id.main_myid)
        mMainCallbtn = root.findViewById(R.id.main_callbtn)
        mMainCallbtn?.setOnClickListener(this)
        mMainTargetid = root.findViewById(R.id.main_targetid)
        mMainStatus = root.findViewById(R.id.main_status)
        mMainMyid!!.text = Appsc.USER_ID

        mMainCallbtn!!.isEnabled = Appsc.sinchClient.isStarted
        startService<SinchService>()
        if (Appsc.sinchClient.isStarted) {
            mMainStatus!!.text = "Client Connected, ready to use!"
        }
        // Inflate the layout for this fragment
        return  root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }


    private fun initView() {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.main_callbtn -> {
                if (mMainTargetid!!.text.length < 8) {
                    mMainTargetid!!.error = "Masukan ID yang benar"
                } else mMainTargetid!!.error = null

                val currentcall =
                    Appsc.callClient.callUser(mMainTargetid!!.text.toString())

                startActivity(intentFor<IncommingCallActivity>("callid" to currentcall.callId,
                    "incomming" to false).newTask())

            }
            else -> {
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSinchConnected(sinchConnected: SinchConnected?) {
        mMainStatus!!.append("* CONNECTED :)\n---------------------------\n")
        mMainCallbtn!!.isEnabled = true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSinchDisconnected(sinchDisconnected: SinchDisconnected?) {
        mMainStatus!!.append("* DISCONNECTED\n---------------------------\n")
        mMainCallbtn!!.isEnabled = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSinchFailed(sinchFailed: SinchFailed) {
        mMainStatus!!.append(
            String.format(
                "* CONNECTION FAILED: %s\n---------------------------\n",
                sinchFailed.error.message
            )
        )
        mMainCallbtn!!.isEnabled = false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSinchLogging(sinchLogger: SinchLogger) {
        mMainStatus!!.append(
            String.format(
                "* %s ** %s ** %s\n---------------------------\n",
                sinchLogger.message,
                sinchLogger.area,
                sinchLogger.level
            )
        )
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
