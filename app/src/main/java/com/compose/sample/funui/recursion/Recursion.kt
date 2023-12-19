package com.compose.sample.funui.recursion

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.descendants
import androidx.core.view.forEach
import java.math.BigInteger
import java.util.LinkedList
import java.util.Queue

/**
 * 2023-12-19 Kotlin under the hood how to get rid of recursion
 */

tailrec fun fibonacci(n: Int, a: BigInteger, b: BigInteger): BigInteger {
    return if (n == 0) b else fibonacci(n - 1, a + b, a)
}

//尾递归的迭代方式
fun fibonacci2(
    n: Int, a: BigInteger, b: BigInteger
): BigInteger {
    var n = n
    var a = a
    var b = b
    while (true) {
        if (n == 0) {
            return b
        }
        n = n - 1
        val tmp = a.add(b)
        b = a
        a = tmp
    }
}

fun ViewGroup.findViewRecursion(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    if (predicate(this)) {
        accumulator.add(this)
    }
    children.forEach { child ->
        when {
            child is ViewGroup -> {
                child.findViewRecursion(predicate)
            }

            predicate(child) -> accumulator.add(child)
        }
    }
    return accumulator
}

//以上可见，每次递归都会有个 accumulator list，优化以下
fun ViewGroup.findViewRecursion2(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    this.internalFindView(predicate, accumulator)
    return accumulator
}

private fun ViewGroup.internalFindView(
    predicate: (View) -> Boolean,
    accumulator: MutableList<View> = mutableListOf()
) {
    if (predicate(this)) {
        accumulator.add(this)
    }
    children.forEach { child ->
        when {
            child is ViewGroup -> {
                child.internalFindView(predicate, accumulator)
            }

            predicate(child) -> accumulator.add(child)
        }
    }
}

// 以上使用 queue 来优化
fun ViewGroup.findViewQueue(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    val queue: Queue<View> = LinkedList()

    queue.add(this)
    while (queue.isNotEmpty()) {
        val view = queue.poll()!!
        if (predicate(view)) {
            accumulator.add(view)
        }
        if (view is ViewGroup) {
            view.children.forEach { queue.add(it) }
        }
    }
    return accumulator
}


//更通用的做法 Iterator
class TreeIterator<T>(
    rootIterator: Iterator<T>,
    private val getChildIterator: ((T) -> Iterator<T>?)
) : Iterator<T> {
    private val stack = mutableListOf<Iterator<T>>()
    private var iterator: Iterator<T> = rootIterator

    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun next(): T {
        val item = iterator.next()
        prepareNextIterator(item)
        return item
    }

    private fun prepareNextIterator(item: T) {
        val childIterator = getChildIterator(item)
        if (childIterator != null && childIterator.hasNext()) {
            stack.add(childIterator)
            iterator = childIterator
        } else {
            while (!iterator.hasNext() && stack.isNotEmpty()) {
                iterator = stack.last()
                stack.removeLast()
            }
        }
    }
}

//usage
fun ViewGroup.findViewTreeIterator(predicate: (View) -> Boolean): Sequence<View> {
    val treeIterator = TreeIterator(children.iterator()) { view ->
        (view as? ViewGroup)?.children?.iterator()
    }
    return sequenceOf(this)
        .plus(treeIterator.asSequence())
        .filter { predicate(it) }
}

//sdk 的 方式, 注意：效率比起上面的差多了
public val ViewGroup.descendants: Sequence<View>
    get() = sequence {
        forEach { child ->
            yield(child)
            if (child is ViewGroup) {
                yieldAll(child.descendants)
            }
        }
    }

fun ViewGroup.findViewYield(predicate: (View) -> Boolean): Sequence<View> {
    return sequenceOf(this)
        .plus(this.descendants)
        .filter { predicate(it) }
}