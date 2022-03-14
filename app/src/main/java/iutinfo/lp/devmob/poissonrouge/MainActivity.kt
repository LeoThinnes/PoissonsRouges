package iutinfo.lp.devmob.poissonrouge

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import android.widget.GridView
import com.google.firebase.messaging.FirebaseMessaging

import iutinfo.lp.devmob.poissonrouge.adapters.AquariumAdapter
import iutinfo.lp.devmob.poissonrouge.model.Alert
import iutinfo.lp.devmob.poissonrouge.model.Aquarium
import iutinfo.lp.devmob.poissonrouge.model.Species
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    var listAquariums : MutableList<Aquarium> = mutableListOf()
    private var aquariumAdapter:AquariumAdapter ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        val database = Firebase.database

        val extraID = intent.getStringExtra("id")
        val extraProblem = intent.getStringExtra("problem")
        val extraTemperature = intent.getStringExtra("temperature")

        //Si on ouvre l'app via un notifications
        if (extraID != null) {
            val idAlert = extraID + "_" + extraProblem + "_" + Date().toString()

            val alert = Alert(idAlert, extraProblem, extraTemperature, extraID, "null", true, "null", "null")
            val dbAlert = database.getReference("Alerte").child(idAlert);
            dbAlert.setValue(alert);
            val intent = Intent(this, DetailAquariumActivity::class.java)
            intent.putExtra("idAquarium", extraID)
            intent.putExtra("problem", extraProblem)
            intent.putExtra("temperature", extraTemperature)

            startActivity(intent)
        }

        setContentView(R.layout.activity_main)

        val grille = findViewById<GridView>(R.id.grid)

        //initialisation de Firebase Database
        val dbAquarium = database.getReference("Aquarium") //Choix du noeud voulu

        dbAquarium.addValueEventListener(object: ValueEventListener {
            // récupération des données a chaque mise a jour des données
            override fun onDataChange(snapshot: DataSnapshot) {

                //création d'un liste des aquariums
                listAquariums = mutableListOf()
                val aquariums = snapshot.children
                aquariums.forEach(){
                    val species = Species(
                        it.child("espece/id").value.toString(),
                        it.child("espece/nom").value.toString(),
                        it.child("espece/tempMax").value.toString().toDouble(),
                        it.child("espece/tempMin").value.toString().toDouble(),
                    );
                    val aquarium = Aquarium(
                        it.child("id").value.toString(),
                        it.child("nom").value.toString(),
                        species,
                        it.child("salle").value.toString(),
                        it.child("temperature").value.toString().toDouble(),
                        it.child("beacon").value.toString(),
                    )
                    listAquariums.add(aquarium)
                    // affichage des aquariums dans la gridView
                    aquariumAdapter = AquariumAdapter(applicationContext, listAquariums)
                    grille?.adapter = aquariumAdapter
                }
            }

            //si il y a une erreur de récupération
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })



        grille?.onItemClickListener = this

    }

    //fonction appellée au click sur une carte
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, DetailAquariumActivity::class.java)
        intent.putExtra("idAquarium", listAquariums[position].id)
        startActivity(intent)
    }

}