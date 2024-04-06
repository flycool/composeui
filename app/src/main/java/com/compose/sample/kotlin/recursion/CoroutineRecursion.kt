package com.compose.sample.kotlin.recursion

import kotlin.coroutines.*
import kotlin.coroutines.intrinsics.*

class Tree(val left: Tree?, val right: Tree?)

fun depth(t: Tree?): Int =
    if (t == null) 0 else maxOf(
        depth(t.left),
        depth(t.right)
    ) + 1


fun depth2(t: Tree?): Int {
    if (t == null) return 0
    class Frame(val node: Tree, var state: Int = 0, var depth: Int = 1)

    val stack = ArrayList<Frame>()
    val root = Frame(t)
    stack.add(root)
    while (stack.isNotEmpty()) {
        val frame = stack.last()
        when (frame.state++) {
            0 -> frame.node.left?.let { l -> stack.add(Frame(l)) }
            1 -> frame.node.right?.let { r -> stack.add(Frame(r)) }
            2 -> {
                stack.removeLast()
                stack.lastOrNull()?.let { p -> p.depth = maxOf(p.depth, frame.depth + 1) }
            }
        }
    }
    return root.depth
}

val depth3 = DeepRecursiveFunction<Tree?, Int> { t ->
    if (t == null) 0 else maxOf(
        callRecursive(t.left),
        callRecursive(t.right)
    ) + 1
}

fun main() {
    val n = 100000
    val deepTree = generateSequence(Tree(null, null)) { prev ->
        Tree(prev, null)
    }.take(n).last()

    //val height = depth(deepTree) // StackOverflow
    //val height = depth2(deepTree)
    val height = depth3(deepTree)

    println(height)


}

//use coroutine
// https://gist.github.com/elizarov/861dda8c3e8c5ee36eaa6db4ad996568

class DeepRecursiveFunction<T, R>(
    val block: suspend DeepRecursiveScope<T, R>.(T) -> R
)

sealed class DeepRecursiveScope<T, R> {
    abstract suspend fun callRecursive(value: T): R
}

operator fun <T, R> DeepRecursiveFunction<T, R>.invoke(value: T): R =
    DeepRecursiveScopeImpl<T, R>(block, value).runCallLoop()

// ================== Implementation ==================

private typealias DeepRecursiveFunctionBlock = Function3<Any?, Any?, Continuation<Any?>?, Any?>

private val UNDEFINED_RESULT = Result.success(COROUTINE_SUSPENDED)

private class DeepRecursiveScopeImpl<T, R>(
    block: suspend DeepRecursiveScope<T, R>.(T) -> R,
    value: T
) : DeepRecursiveScope<T, R>(), Continuation<R> {

    private var function: DeepRecursiveFunctionBlock = block as DeepRecursiveFunctionBlock
    private var value: Any? = value
    private var cont: Continuation<Any?>? = this as Continuation<Any?>

    private var result: Result<Any?> = UNDEFINED_RESULT

    override val context: CoroutineContext
        get() = EmptyCoroutineContext

    override fun resumeWith(result: Result<R>) {
        this.cont = null
        this.result = result
    }

    override suspend fun callRecursive(value: T): R =
        suspendCoroutineUninterceptedOrReturn { cont ->
            this.cont = cont as Continuation<Any?>
            this.value = value
            COROUTINE_SUSPENDED
        }

    fun runCallLoop(): R {
        while (true) {
            val result = this.result
            val cont = this.cont ?: return (result as Result<R>).getOrThrow()

            if (UNDEFINED_RESULT == result) {
                val r = try {
                    function(this, value, cont)
                } catch (e: Throwable) {
                    cont.resumeWithException(e)
                    continue
                }
                if (r != COROUTINE_SUSPENDED) {
                    cont.resume(r as R)
                }
            } else {
                this.result = UNDEFINED_RESULT
                cont.resumeWith(result)
            }
        }
    }
}


