package iutinfo.lp.devmob.poissonrouge.menunavigation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import iutinfo.lp.devmob.poissonrouge.FinishAlertActivity
import iutinfo.lp.devmob.poissonrouge.NfcReaderActivity
import iutinfo.lp.devmob.poissonrouge.R
import iutinfo.lp.devmob.poissonrouge.model.Alert
import iutinfo.lp.devmob.poissonrouge.model.Aquarium
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlertFragment (private val aquarium: Aquarium, private val alert: Alert?): Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alert, container, false)
        // Inflate the layout for this fragment
        initializeView(view)
        return view
    }

    @SuppressLint("CutPasteId", "SetTextI18n")
    private fun initializeView(view : View) {
        // on récupère les différents elements par leurs id.
        val temperature = view.findViewById<TextView>(R.id.detail_temperature)

        val name = view.findViewById<TextView>(R.id.title_detail_page)
        val species = view.findViewById<TextView>(R.id.detail_species)
        val wantedTemperature = view.findViewById<TextView>(R.id.wanted_temperature)

        // On set les différents éléments par la data class aquarium
        temperature.text = aquarium.temperature.toString() + " C°"
        name.text = aquarium.name
        species.text = aquarium.species.name
        wantedTemperature.text = aquarium.species.tempMin.toString() + " C° - " + aquarium.species.tempMax.toString() + " C°"

        val temperatureIcon = view.findViewById<ImageView>(R.id.temperature_icon)
        val room_alert_text = view.findViewById<TextView>(R.id.room_alert)
        val card = view.findViewById<CardView>(R.id.card_alert)
        val detailAlert = view.findViewById<TextView>(R.id.detail_alert)
        val cardNoAlert = view.findViewById<CardView>(R.id.card_no_alert)
        val btnTakeAlert = view.findViewById<Button>(R.id.btn_nfc)
        val btnHere = view.findViewById<Button>(R.id.btn_beacon)

        room_alert_text.text = "Rendez-vous dans la salle : " + aquarium.room
        if (alert == null){
            card.visibility = View.GONE
            temperatureIcon.setImageResource(R.drawable.good)
            cardNoAlert.visibility = View.VISIBLE
            btnTakeAlert.visibility = View.GONE
            btnHere.visibility = View.GONE
            room_alert_text.visibility = View.GONE
        } else {
            var textAlert : String = "";
            if (alert.problem == "Chaud"){
                temperatureIcon.setImageResource(R.drawable.hot)
                textAlert = "L'eau de l'aquarium est trop chaude"
            } else {
                temperatureIcon.setImageResource(R.drawable.cold)
                textAlert = "L'eau de l'aquarium est trop froide"
            }
            detailAlert.text = textAlert
            cardNoAlert.visibility = View.GONE
            card.visibility = View.VISIBLE

            if(alert.idUser == "null"){
                btnTakeAlert.visibility = View.VISIBLE
                btnHere.visibility = View.GONE
                room_alert_text.visibility = View.GONE
            }else{
                btnTakeAlert.visibility = View.GONE
                btnHere.visibility = View.VISIBLE
                room_alert_text.visibility = View.VISIBLE

            }

            btnTakeAlert.setOnClickListener(){
                val intent = Intent(activity, NfcReaderActivity::class.java)
                intent.putExtra("idAquarium", aquarium.id)
                startActivity(intent)
            }

            btnHere.setOnClickListener(){
                checkPermissions(activity, context)
                val rangingObserver = Observer<Collection<Beacon>> { beacons ->
                    Log.d("TAG", "Ranged: ${beacons.count()} beacons")
                    for (beacon: Beacon in beacons) {
                        if (beacon.id1.toString() == aquarium.beacon){
                            if(beacon.distance < 5) {
                                Toast.makeText(
                                    activity,
                                    R.string.youre_here,
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(activity, FinishAlertActivity::class.java)
                                intent.putExtra("idAlert", alert.id)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    activity,
                                    R.string.youre_not_here,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }

                }

                val beaconManager =  BeaconManager.getInstanceForApplication(requireContext())
                val region = Region("beacon", null, null, null)
                beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"))
                beaconManager.getRegionViewModel(region).rangedBeacons.observe(viewLifecycleOwner, rangingObserver)
                beaconManager.startRangingBeacons(region)

            }
        }
    }


    //
    fun checkPermissions(activity: Activity?, context: Context?) {
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf<String>(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_PRIVILEGED
        )
        if (!hasPermissions(context, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSION_ALL)
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

}