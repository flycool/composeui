package com.compose.sample.kotlin.refactor

import kotlin.reflect.KClass
import kotlin.reflect.KProperty


// Usage Example
val engine1 = Engine("Engine1")
val engine2 = Engine("Engine2")

var blockSelected: Int = 1

val eventHandler = EventHandler {
    blockEvents(
        onEngine = ::engine1,
        onBlock = ::blockSelected
    )
    cropEvents(
        onEngine = ::engine2,
        onBlock = ::blockSelected
    )
}

fun main() {
    blockSelected = 1
    eventHandler.handleEvent(BlockEvent.OnDelete)
    blockSelected = 2
    eventHandler.handleEvent(CropEvent.OnCropRotate(1.5f))
}


// define Events example
interface BlockEvent : BaseEvent {
    object OnChangeFinish : BlockEvent
    object OnForward : BlockEvent
    object OnBackward : BlockEvent
    object OnDuplicate : BlockEvent
    object OnDelete : BlockEvent
    object ToFront : BlockEvent
    object ToBack : BlockEvent
    data class OnChangeBlendMode(val blendMode: String) : BlockEvent
    data class OnChangeOpacity(val opacity: Float) : BlockEvent
}

interface CropEvent : BaseEvent {
    object OnFlipCropHorizontal : CropEvent
    object OnResetCrop : BlockEvent
    data class OnCropRotate(val scaleRatio: Float) : BlockEvent
    data class OnCropStraighten(val angle: Float, val scaleRatio: Float) : BlockEvent
}


// register block related events.
fun EventHandler.blockEvents(
    onEngine: () -> Engine,
    onBlock: () -> Int,
) {
    val engine: Engine by Inject(onEngine)
    val block: Int by InjectInt(onBlock)

    register<BlockEvent.OnDelete> { println("$engine.delete($block)") }
    register<BlockEvent.OnBackward> { println("$engine.sendBackward($block)") }
    register<BlockEvent.OnDuplicate> { println("$engine.duplicate($block)") }
    register<BlockEvent.OnForward> { println("$engine.bringForward($block)") }
    register<BlockEvent.ToBack> { println("$engine.sendToBack($block)") }
    register<BlockEvent.ToFront> { println("$engine.bringToFront($block)") }
    register<BlockEvent.OnChangeFinish> { println("$engine.editor.addUndoStep()") }

    register<BlockEvent.OnChangeBlendMode> {
        println("$engine.block.setBlendMode($block, ${it.blendMode})")
    }
    register<BlockEvent.OnChangeOpacity> {
        println("$engine.block.setOpacity($block, ${it.opacity})")
    }
}

fun EventHandler.cropEvents(
    onEngine: () -> Engine,
    onBlock: () -> Int
) {
    val engine: Engine by Inject(onEngine)
    val block: Int by InjectInt(onBlock)

    /* You can add methods here to reduce duplication */
    fun onCropRotateDegrees(scaleRatio: Float, angle: Float, addUndo: Boolean = true) {
        println("$engine.block.rotateCrop($block, $scaleRatio, $angle, $addUndo)")
    }

    register<CropEvent.OnResetCrop> {
        println("$engine.block.resetCrop($block)")
    }
    register<CropEvent.OnFlipCropHorizontal> {
        println("$engine.block.flipCropHorizontal($block)")
    }
    register<CropEvent.OnCropRotate> {
        println("$engine.block.rotateCrop($block, ${it.scaleRatio})")
    }
    register<CropEvent.OnCropStraighten> {
        println("$engine.block.straightenCrop($block, ${it.scaleRatio})")
    }

    register<CropEvent.OnCropRotate> {
        val normalizedDegrees = 0f // Dummy
        onCropRotateDegrees(it.scaleRatio, normalizedDegrees)
    }
    register<CropEvent.OnCropStraighten> {
        val rotationDegrees = 120f // Dummy
        onCropRotateDegrees(it.scaleRatio, rotationDegrees, addUndo = false)
    }
}


//-----------------------------------------------------------------------------

data class Engine(val name: String)

// Here comes the actual EventHandler
interface BaseEvent

/**
 * A simple event system that can be used to communicate between different parts of the SDK.
 * @param register A lambda that will be called to register all events.
 * @see EventsHandler.register
 */
@Suppress("UNCHECKED_CAST")
class EventHandler(
    register: EventHandler.() -> Unit
) {
    private val eventMap = mutableMapOf<KClass<out BaseEvent>, (event: BaseEvent) -> Unit>()

    // handles an event by calling the lambda that was registered for the event type.
    fun handleEvent(event: BaseEvent) {
        eventMap[event::class]?.invoke(event)
    }

    operator fun <T : BaseEvent> set(event: KClass<out T>, lambda: (event: T) -> Unit) {
        eventMap[event] = lambda as (event: BaseEvent) -> Unit
    }

    infix fun <T : BaseEvent> KClass<out T>.to(lambda: (event: T) -> Unit) {
        eventMap[this] = lambda as (event: BaseEvent) -> Unit
    }

    init {
        register()
    }
}

/**
 * Registers a lambda to be called when an event of the given type is fired.
 * @param lambda The lambda that will be called when the event is fired.
 * @return The lambda that was passed in.
 */
inline fun <reified T : BaseEvent> EventHandler.register(noinline lambda: (event: T) -> Unit): Any {
    // T::class to lambda
    this[T::class] = lambda
    return lambda
}


/**
 * Can be used to inject a dynamic value and bind it to a property.
 * The type of the property will be inferred from the lambda.
 * @param inject The lambda that will be called to inject the value
 */
class Inject<T>(private val inject: () -> T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = inject()
}

class InjectInt(private val inject: () -> Int) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int = inject()
}

// reference: https://medium.com/@naehler.sven?source=post_page-----1289590c3925--------------------------------


