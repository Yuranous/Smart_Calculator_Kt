fun main() {
    val words = mutableMapOf<String, Int>()
    while (true) {
        val temp = readLine()!!
        if (temp == "stop") { break }
        if (!words.containsKey(temp)) {
            words[temp] = 1
        }
        else {
            words[temp] = words[temp]!!.plus(1)
        }
    }
    if (words.isEmpty())
        print(null)
    else
        print(words.maxBy { it.value }!!.key)
}
