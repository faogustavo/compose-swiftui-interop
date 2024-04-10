private val nameList = listOf("Gustavo", "Kevin", "Tadeas", "Gabriel")
fun getRandom(except: String): String {
    var newName = except

    while (newName == except) {
        newName = nameList.random()
    }

    return newName
}
