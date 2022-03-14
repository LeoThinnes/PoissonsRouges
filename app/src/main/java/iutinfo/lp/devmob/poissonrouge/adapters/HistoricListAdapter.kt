package iutinfo.lp.devmob.poissonrouge.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.cardview.widget.CardView
import iutinfo.lp.devmob.poissonrouge.DetailAquariumActivity
import iutinfo.lp.devmob.poissonrouge.R
import iutinfo.lp.devmob.poissonrouge.model.Alert

class HistoricListAdapter(var context: Context, var list: MutableList<Alert>): BaseAdapter()  {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.item_list, null)
        val alertdate: TextView = view.findViewById(R.id.list_date)
        val alertTitle: TextView = view.findViewById(R.id.list_title)
        val alertTemperature: TextView = view.findViewById(R.id.list_temperature)
        val listItem: Alert = list.get(position)
        val cardList: CardView = view.findViewById(R.id.card_list)

        val tabString : List<String> = listItem.id!!.split(" ")
        var month: String = "00"
        when (tabString[1]){
            "Jan" ->  month = "01"
            "Feb" ->  month = "02"
            "Mar" ->  month = "03"
            "Apr" ->  month = "04"
            "Mai" ->  month = "05"
            "Jun" ->  month = "06"
            "Jul" ->  month = "07"
            "Aug" ->  month = "08"
            "Sep" ->  month = "09"
            "Oct" ->  month = "10"
            "Nov" ->  month = "11"
            "Dec" ->  month = "12"
        }

        alertdate.text = tabString[2] + "/" + month + "/" + tabString[5]
        //affichage selon probleme de temperature
        if (listItem.problem == "Chaud"){
            alertTitle.text = "Eau trop chaude"
            cardList.setCardBackgroundColor(Color.rgb(197,0,12))
        }else {
            alertTitle.text = "Eau trop froide"
            cardList.setCardBackgroundColor(Color.rgb(0,155,165))
        }

        alertTemperature.text = listItem.temperature + "°C"

        return view
    }
}