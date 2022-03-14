package iutinfo.lp.devmob.poissonrouge.menunavigation.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import iutinfo.lp.devmob.poissonrouge.R
import iutinfo.lp.devmob.poissonrouge.adapters.HistoricListAdapter
import iutinfo.lp.devmob.poissonrouge.model.Alert

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoricFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoricFragment(private val listAlerts : MutableList<Alert>) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listAdapter: HistoricListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_historic, container, false)
        initializeView(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initializeView(view: View) {
        val list = view.findViewById<ListView>(R.id.historic_list)
        listAdapter = HistoricListAdapter(requireContext(), listAlerts)
        list?.adapter = listAdapter

        //affichage d'un dialogue lors du clique sur un item de la liste
        list.setOnItemClickListener { parent, view, position, id ->
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.apply {
                setTitle("Détail du problème").setMessage(listAlerts.get(position).description)
                    .setNegativeButton("Retour") { _, _ ->

                    }.create().show()
            }
        }

    }
}