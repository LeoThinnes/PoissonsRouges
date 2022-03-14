package iutinfo.lp.devmob.poissonrouge.adapters

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import iutinfo.lp.devmob.poissonrouge.R
import iutinfo.lp.devmob.poissonrouge.model.Aquarium

class AquariumAdapter(var context: Context, var liste: MutableList<Aquarium>): BaseAdapter() {


    override fun getCount(): Int {
        return liste.size
    }

    override fun getItem(position: Int): Any {
        return liste.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.item_card, null)
        val idAquarium:TextView = view.findViewById(R.id.card_name)
        val tempAquarium:TextView = view.findViewById(R.id.card_temperature)

        val alertImage = view.findViewById<ImageView>(R.id.logo_alert)

        val listItem:Aquarium = liste.get(position)

        //affichage selon la température
        if(listItem.temperature > listItem.species.tempMax) {
            setTheme(view, 255, 210, 144, R.drawable.hot)
            alertImage.setImageResource(R.drawable.alert)
        }else if (listItem.temperature < listItem.species.tempMin) {
            setTheme(view, 198, 255, 255, R.drawable.cold)
            alertImage.setImageResource(R.drawable.alert)
        }else {
            setTheme(view, 241, 241, 241, R.drawable.good)
        }



        idAquarium.text = listItem.name
        tempAquarium.text = listItem.temperature.toString() + "°C"

        return view
    }

    //fonction permettant de set l'image et la couleur voulue (evite la duplication)
    private fun setTheme(view: View, red: Int, green: Int, blue: Int, image: Int){
        val relativeLayout = view.findViewById<LinearLayout>(R.id.relative_layout_card)
        val temperatureImage = view.findViewById<ImageView>(R.id.logo_temperature)

        relativeLayout.setBackgroundColor(Color.rgb(red,green,blue))
        temperatureImage.setImageResource(image)
    }
}