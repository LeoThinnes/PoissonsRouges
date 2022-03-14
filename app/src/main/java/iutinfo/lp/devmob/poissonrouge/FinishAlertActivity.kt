package iutinfo.lp.devmob.poissonrouge

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import iutinfo.lp.devmob.poissonrouge.model.Alert
import java.lang.System.out

class FinishAlertActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1
    lateinit var imageResolution: ImageView
    lateinit var alert : Alert;
    var image : Bitmap? = null;
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_alert)

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        val database = Firebase.database
        imageResolution = findViewById(R.id.imageResolution)
        val btnTakePicture = findViewById<Button>(R.id.btnTakePicture)
        val btnFinishAlert = findViewById<Button>(R.id.btnProblemResolved)
        btnTakePicture.setOnClickListener(){
            takePicture()
        }

        btnTakePicture.setOnClickListener(){
            takePicture()
        }

        btnFinishAlert.setOnClickListener(){
            val textAlert = findViewById<EditText>(R.id.et_description).text
            Log.e("image", image.toString())
            if (textAlert.isEmpty() && image == null){
                Toast.makeText(
                    this,
                    R.string.info_miss,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                alert.photo = image.toString()
                alert.description = textAlert.toString()
                alert.enCours = false
                Log.e("alerte", alert.id.toString())
                val dbAlert = database.getReference("Alerte").child(alert.id.toString());
                dbAlert.setValue(alert);

                val intent = Intent(this, DetailAquariumActivity::class.java)
                intent.putExtra("idAquarium", alert.idAquarium)
                startActivity(intent)
                Toast.makeText(
                    this,
                    R.string.alert_close,
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        val idAlert = intent.getStringExtra("idAlert")

        val refAquarium = database.getReference("Alerte").child(idAlert.toString())
        refAquarium.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {


                alert = Alert(
                    snapshot.child("id").value.toString(),
                    snapshot.child("problem").value.toString(),
                    snapshot.child("temperature").value.toString(),
                    snapshot.child("idAquarium").value.toString(),
                    snapshot.child("idUser").value.toString(),
                    true,
                    "null",
                    "null"
                )

            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

    }



    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        /*try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }*/
        this.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            image = data?.extras?.get("data") as Bitmap
            imageResolution.setImageBitmap(image);
        }
    }


}