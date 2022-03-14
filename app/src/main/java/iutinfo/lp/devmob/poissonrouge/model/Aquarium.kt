package iutinfo.lp.devmob.poissonrouge.model

import iutinfo.lp.devmob.poissonrouge.model.Species

class Aquarium(val id: String, val name: String, var species : Species, val room : String, var temperature: Double, var beacon: String?) {
}