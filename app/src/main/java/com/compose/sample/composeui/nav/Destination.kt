package com.compose.sample.composeui.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    data object Landing : Route()
    @Serializable
    data object AnnotationString : Route()
    @Serializable
    data object Canvas : Route()
    @Serializable
    data object Containertransform : Route()
    @Serializable
    data object DecorationTextfield : Route()
    @Serializable
    data object Dragfood : Route()
    @Serializable
    data object EdgeToEdge : Route()
    @Serializable
    data object Emoji : Route()
    @Serializable
    data object Indicator : Route()
    @Serializable
    data object Navigationbar : Route()
    @Serializable
    data object Netstedscroll : Route()
    @Serializable
    data object Overlaprow : Route()
    @Serializable
    data object Permission : Route()
    @Serializable
    data object Polygonloader : Route()
    @Serializable
    data object Tabrow : Route()
    @Serializable
    data object Visualtransformation : Route()
    @Serializable
    data object CustomModifier : Route()
    @Serializable
    data object TimeLine : Route()
    @Serializable
    data object InvitationCard : Route()
    @Serializable
    data object Slider : Route()
    @Serializable
    data object Playerprogressbar : Route()
    @Serializable
    data object Dragbox : Route()
    @Serializable
    data object Lookaheadscope : Route()
    @Serializable
    data object Containertransform2 : Route()
    @Serializable
    data object Draggableindicator : Route()
    @Serializable
    data object Footballground : Route()
    @Serializable
    data object GlovoScreen : Route()
    @Serializable
    data object DocumentScanner : Route()
    @Serializable
    data object PullToRefresh : Route()
    @Serializable
    data object BiometricPrompt : Route()


}

val routes = listOf(
    Route.AnnotationString, Route.Containertransform,
    Route.DecorationTextfield, Route.Dragfood, Route.EdgeToEdge,
    Route.Emoji,
    Route.Indicator,
    Route.Navigationbar,
    Route.Netstedscroll,
    Route.Overlaprow,
    Route.Permission,
    Route.Polygonloader,
    Route.Tabrow,
    Route.Visualtransformation,
    Route.CustomModifier,
    Route.TimeLine,
    Route.InvitationCard,
    Route.Slider,
    Route.Playerprogressbar,
    Route.Dragbox,
    Route.Lookaheadscope,
    Route.Containertransform2,
    Route.Draggableindicator,
    Route.Footballground,
    Route.GlovoScreen,
    Route.DocumentScanner,
    Route.PullToRefresh,
    Route.BiometricPrompt,
)

enum class Destination(val route: String) {
    annotationstring("annotationstring"),
    canvas("canvas"),
    containertransform("containertransform"),
    decorationtextfield("decorationtextfield"),
    dragfood("dragfood"),
    edgetoedge("edgetoedge"),
    emoji("emoji"),
    indicator("indicator"),
    navigationbar("navigationbar"),
    netstedscroll("netstedscroll"),
    overlaprow("overlaprow"),
    permission("permission"),
    polygonloader("polygonloader"),
    tabrow("tabrow"),
    visualtransformation("visualtransformation"),
    CustomModifier("custommodifier"),
    TimeLine("timeline"),
    InvitationCard("invitationcard"),
    slider("slider"),
    playerprogressbar("playerprogressbar"),
    dragbox("dragbox"),
    lookaheadscope("lookaheadscope"),
    containertransrom("containertransrom"),
    draggableindicator("draggableindicator"),
    footballground("footballground"),
    GlovoScreen("GlovoScreen"),
    documentScanner("documentScanner"),
    pullToRefresh("pullToRefresh"),
    BiometricPrompt("BiometricPrompt"),
}