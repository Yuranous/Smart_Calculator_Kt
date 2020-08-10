package calculator

import java.lang.Exception
import java.math.BigInteger
import java.util.regex.Pattern

val variables = mutableMapOf<String, String>()
fun main() {
        loop@ while(true) {
            val line = readLine()!!
            when {
                line.isEmpty() -> continue@loop
                line == "/exit" -> {
                    println("Bye!"); break@loop
                }
                line == "/help" -> println("The program calculates the sum of numbers")
                line.startsWith("/") -> println("Unknown command")
                line.endsWith("+")
                        or line.endsWith("-") -> {
                    println("Invalid expression"); continue@loop
                }
                line.contains("=") -> addVariable(line)
                Pattern.compile("[a-zA-z]").toRegex().containsMatchIn(line) and variables.filterKeys { line.contains(it) }.isEmpty() -> println("Unknown variable")
                else -> try {
                    println(calculate(line))
                } catch (e: Exception) {
                    println("Invalid expression")
                }
            }
        }
}

fun replaceVariablesWithValues(line: String): String {
    var result = line.trim()
    for (entry in variables) {
        if (result.indexOf('=') < result.indexOf(entry.key)) {
            result = result.replace(entry.key, entry.value)
        }
    }
    return result
}

fun calculate(line: String): BigInteger {
    var result = line.replace(" ", "")
    result = replaceVariablesWithValues(result)
    while (result.contains("-+")) { result = result.replace("-+", "-") }
    while (result.contains("--")) { result = result.replace("--", "+") }
    while (result.contains("++")) { result = result.replace("++", "+") }
    while (result.contains("+-")) { result = result.replace("+-", "-") }
    return InfixToPostfixConverter().calculateExpression(result.trim())
}

fun addVariable(line: String) {
    var temp = line.trim()
    val variablePattern = Pattern.compile("\\b[^\\d\\WA-Z]+\\b")
    if (!variablePattern.toRegex().containsMatchIn(temp)) {
        println("Invalid identifier")
        return
    }
    temp = replaceVariablesWithValues(temp)
    val assignmentPattern = Pattern.compile("\\w+\\s*=\\s*[+-]*\\b\\d+\\b")
    if (!assignmentPattern.toRegex().containsMatchIn(temp) or (temp.count { it == '=' } > 1)) {
        println("Invalid assignment")
        return
    }
    val result = temp.trim().replace(" ", "").split("=")
    variables[result[0].trim()] = result[1].trim()
}