package com.kasa777.modal

class NavigationItemModal{
private var image: Int = 0
private var name: String? = ""

constructor(image: Int, name: String?) {
this.image = image
this.name = name
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

fun setName(name: String) {
this.name = name
}

}