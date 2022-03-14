package iutinfo.lp.devmob.poissonrouge

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import iutinfo.lp.devmob.poissonrouge.model.Aquarium
import iutinfo.lp.devmob.poissonrouge.model.Species
import iutinfo.lp.devmob.poissonrouge.menunavigation.fragments.AlertFragment
import iutinfo.lp.devmob.poissonrouge.menunavigation.fragments.HistoricFragment
import iutinfo.lp.devmob.poissonrouge.model.Alert

class DetailAquariumActivity: AppCompatActivity() {
    var listAlerts : MutableList<Alert> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_aquarium)

        val idAquarium = intent.getStringExtra("idAquarium")
        val extraIdUser = intent.getStringExtra("idUser")
       // extraIdUser = "bonjour"
//        val extraTemperature = intent.getStringExtra("temperature")

        val database = Firebase.database
        val refAquarium = database.getReference("Aquarium").child(idAquarium.toString())
        refAquarium.addValueEventListener(object: ValueEventListener {
            // récupération des données a chaque mise a jour des données
            override fun onDataChange(snapshot: DataSnapshot) {
                val species = Species(
                    snapshot.child("espece/id").value.toString(),
                    snapshot.child("espece/nom").value.toString(),
                    snapshot.child("espece/tempMax").value.toString().toDouble(),
                    snapshot.child("espece/tempMin").value.toString().toDouble(),
                );
                val aquarium = Aquarium(
                    snapshot.child("id").value.toString(),
                    snapshot.child("nom").value.toString(),
                    species,
                    snapshot.child("salle").value.toString(),
                    snapshot.child("temperature").value.toString().toDouble(),
                    snapshot.child("beacon").value.toString(),
                )

                val refAlert = database.getReference("Alerte")
                refAlert.addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var alert: Alert? = null
                        val alerts = snapshot.children
                        listAlerts = mutableListOf()
                        alerts.forEach(){
                            if(it.child("idAquarium").value.toString() == idAquarium){
                            // On parcours les alertes d'un aqurium précis, il ne peut y a en avoir qu'une en cours, les autres seront mises dans une liste pour en faire un historique
                                if(it.child("enCours").value == false){
                                   val alertClosed = Alert(
                                        it.child("id").value.toString(),
                                       it.child("problem").value.toString(),
                                       it.child("temperature").value.toString(),
                                       it.child("idAquarium").value.toString(),
                                       it.child("idUser").value.toString(),
                                        false,
                                       it.child("description").value.toString(),
                                       it.child("photo").value,
                                    );
                                    listAlerts.add(alertClosed)
                                } else {
                                    alert = Alert(
                                        it.child("id").value.toString(),
                                        it.child("problem").value.toString(),
                                        it.child("temperature").value.toString(),
                                        it.child("idAquarium").value.toString(),
                                        it.child("idUser").value.toString(),
                                        true, "null", "null"
                                    )
                                    if(extraIdUser != null) {
                                        alert!!.idUser = extraIdUser
                                        val dbAlert = database.getReference("Alerte").child(alert!!.id.toString());
                                        dbAlert.setValue(alert);
                                    }
                                }

                            }
                        }

                        showFragment(AlertFragment(aquarium, alert))



                        val bottomNavigationView : BottomNavigationView = findViewById(R.id.bottom_navigation)

                        // Navigation d'un fragment à l'autre selon l'Id de l'item du bottom_navigation
                        bottomNavigationView.setOnItemSelectedListener { item ->
                            when (item.itemId) {
                                R.id.alert_fragment -> {
                                    showFragment(AlertFragment(aquarium, alert))
                                    return@setOnItemSelectedListener true
                                }
                                R.id.historic_fragment -> {
                                    showFragment(HistoricFragment(listAlerts))
                                    return@setOnItemSelectedListener true
                                }
                                R.id.cancel_fragment -> {
                                    val intent = Intent(this@DetailAquariumActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    return@setOnItemSelectedListener false
                                }
                                else -> false
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                    }

                })
            }
            //s'il y a une erreur de récupération
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })


    }

    fun AppCompatActivity.showFragment(frg: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_fragment, frg)
            .commitAllowingStateLoss();
    }


}