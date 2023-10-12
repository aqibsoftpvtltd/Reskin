package com.kasa777.modal

class NavigationItemModal{
private var image: Int = 0
private var name: String? = ""
    private var details: String? = ""

constructor(image: Int, name: String?,details: String?) {
this.image = image
this.name = name
    this.details = details
}

fun getImage(): Int {
return image
}

fun setImage(image: Int) {
this.image = image
}

fun getName(): String? {
return name
}

    fun getDetails(): String? {
        return details
    }

fun setName(name: String) {
this.name = name
}

}