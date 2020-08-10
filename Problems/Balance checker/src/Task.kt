import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    var account: Int = scanner.nextInt()
    var purchases = 0
    while (scanner.hasNextInt()) {
        purchases = scanner.nextInt()
        if (!scanner.hasNextInt()) {
             if (account < purchases) {
        print("Error, insufficient funds for the purchase. You have $account, but you need $purchases.")
    }
    else {
        print("Thank you for choosing us to manage your account! You have ${account - purchases} money.")
    }
        }
        account -= purchases
    }
}
