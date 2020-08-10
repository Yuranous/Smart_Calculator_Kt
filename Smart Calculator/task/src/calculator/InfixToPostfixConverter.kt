package calculator

import java.math.BigInteger

class InfixToPostfixConverter {
    private var stack = mutableListOf<String>()
    private var queue = mutableListOf<String>()
    private var expressionList = mutableListOf<String>()

    fun calculateExpression(expression: String): BigInteger {
        parseExpression(expression)
        if (expressionList.count { it == "(" } != expressionList.count { it == ")" }) {
            throw Exception("Invalid expression")
        }
        getPostFixEx()
        return calcPostFix()
    }

    private fun parseExpression(expression: String) {
        var tempValue: String? = null
        expression.forEach {
            if ((it == '+' || it == '-' || it == '*' || it == '/') &&
                    tempValue == null) {
                if (expressionList.size == 0) {
                    tempValue = it.toString()
                } else {
                    expressionList.add(it.toString())
                }
            }
            else if (it == '(' || it == ')') {
                if (tempValue != null) {
                    expressionList.add(tempValue!!)
                    tempValue = null
                }
                expressionList.add(it.toString())
            }
            else if ((it == '+' || it == '-' || it == '*' || it == '/' || it == '(' || it == ')') &&
                    tempValue != null) {
                expressionList.add(tempValue!!)
                expressionList.add(it.toString())
                tempValue = null
            }
            else {
                when (tempValue) {
                    null -> tempValue = it.toString()
                    else -> tempValue += it.toString()
                }
            }
        }
        if (tempValue != null) {
            expressionList.add(tempValue!!)
        }
    }
    private fun getPostFixEx() {
        // Перебираем expressionList
        expressionList.forEach {
            when {
                //Если входящий элемент левая скобка делаем PUSH
                it == "(" -> push(it)

                //Если входящий элемент правая скобка делаем POP
                it == ")" -> {
                    if (expressionList.contains("(")) {
                        pop()
                    }
                }

                //Если входящий элемент число, то добавляем в очередь
                Regex("[\\d]").containsMatchIn(it) -> addQueue(it)

                //Если входящий элемент + или -
                Regex("[+-]").containsMatchIn(it) ->
                    /* Проверяем, если стек пустой или на вершине стека левая скобка,
                    * то делаем PUSH */
                    if (stack.isEmpty() || stack.last() == "(") push(it)
                    /* Иначе, если на вершине стека оператор имеющий больший
                    * приоритет, то делаем POP, потом PUSH */
                    else if (stack.last().contains(Regex("[/*]"))) {
                        pop()
                        push(it)
                    }
                    // Иначе просто делаем PUSH
                    else {
                        addQueue(stack.last())
                        stack[stack.lastIndex] = it
                    }

                //Если входящий элемент * или /
                Regex("[*/]").containsMatchIn(it) -> {
                    /* Проверяем, если на вершине стека элемент с таким же приоритетом,
                    * то делаем POP */
                    if (stack.isNotEmpty() && (stack.last() == "*" || stack.last() == "/")) {
                        pop()
                    }
                    // Потом делаем PUSH
                    push(it)
                }
            }
        }
        // Когда перебрали все элементы выражения, то добавляем из стека элементы в очередь
        if (stack.isNotEmpty()) {
            for (i in stack.lastIndex downTo 0) {
                if (stack[i] != "(") {
                    addQueue(stack[i])
                }
            }
        }
    }

    private fun pop() {
        // Выгружаем стек в очередь пока не найдем левую скобку, потом удаляем скобку из стека
        Loop@ for (i in stack.lastIndex downTo 0) {
            if (stack[i] == "(") {
                stack[i] = " "
                break@Loop
            }
            addQueue(stack[i])
            stack[i] = " "
        }
        stack.removeIf { it == " " }
    }

    private fun addQueue(item: String) {
        queue.add(item)
    }

    private fun push(item: String) {
        stack.add(item)
    }

    private fun calcPostFix(): BigInteger {
        //Создаем стек для работы
        val stack = mutableListOf<BigInteger>()
        // Перебераем все эелементы очереди
        for (item in queue) {
            when {
                // Если входящий элемент - число, то добавляем в стек
                Regex("[\\d]").containsMatchIn(item) -> {
                    stack.add(item.toBigInteger())
                }
                /* Если входящий элемент + , берем два последних элемента
                и производим советующую операцию */
                item == "+" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] + stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                /* Если входящий элемент * , берем два последних элемента
                и производим советующую операцию */
                item == "*" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] * stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                /* Если входящий элемент / , берем два последних элемента
                и производим советующую операцию */
                item == "/" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] / stack.last()
                    stack.removeAt(stack.lastIndex)
                }
                /* Если входящий элемент -, берем два последних элемента
                 и производим советующую операцию */
                item == "-" -> {
                    stack[stack.lastIndex - 1] = stack[stack.lastIndex - 1] - stack.last()
                    stack.removeAt(stack.lastIndex)
                }
            }
        }
        // Результат - это элемент, который остался в стеке
       return stack.first()
    }
}
