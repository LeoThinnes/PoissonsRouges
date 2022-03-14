package iutinfo.lp.devmob.poissonrouge

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.nfc.tech.MifareUltralight

import android.nfc.tech.MifareClassic
import android.os.Parcelable
import android.widget.Button
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.experimental.and


open class NfcReaderActivity: AppCompatActivity() {
    var nfcAdapter: NfcAdapter? = null
    var pendingIntent: PendingIntent? = null
    lateinit var idAquarium: String
    val TAG = "nfc_test"
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detect_nfc)

        idAquarium = intent.getStringExtra("idAquarium").toString()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)

        btnCancel.setOnClickListener(){
            finish();
        }

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this)?.let { it }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"NO NFC Capabilities",
                Toast.LENGTH_SHORT).show();
            finish();
        }

        pendingIntent =PendingIntent.getActivity(
            this,
            0,
            Intent(this, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )


    }

    override fun onResume() {
        super.onResume()
        assert(nfcAdapter != null)
        //nfcAdapter.enableForegroundDispatch(context,pendingIntent,
        //                                    intentFilterArray,
        //                                    techListsArray)
        nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        //Onpause stop listening
        if (nfcAdapter != null) {
            nfcAdapter!!.disableForegroundDispatch(this)
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        resolveIntent(intent)
    }

    private fun resolveIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val tag = (intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?)!!

            val payload = detectTagData(tag).toByteArray()
        }
    }

    private fun detectTagData(tag: Tag?): String {
        val id = tag!!.id
        if (toHex(id)!! == "0ffffff80 65 2a 0ffffffca 21 27 04" || toHex(id)!! == "0ffffff80 6f 0ffffffa6 1a 0b 5b 04" || toHex(id)!! == "0ffffff80 6f 0ffffffa6 12 23 51 04"){
            Toast.makeText(
                this,
                R.string.ok_nfc,
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this, DetailAquariumActivity::class.java)
            intent.putExtra("idUser", toHex(id)!!.toString())
            intent.putExtra("idAquarium", idAquarium )
            startActivity(intent)
        }
        return toHex(id)!!
    }

    private fun toHex(bytes: ByteArray): String? {
        val sb = StringBuilder()
        for (i in bytes.indices.reversed()) {
            val b = (bytes[i] and 0xff.toByte()).toInt()
            if (b < 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
            if (i > 0) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }
}