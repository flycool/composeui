@file:OptIn(ExperimentalFoundationApi::class)

package com.compose.sample.composeui.draganddrop

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerContent() {
    val pagerState = rememberPagerState(
        initialPage = 2,
        initialPageOffsetFraction = 0f,
        pageCount = { 3 }
    )
// Wrap the entire horizontal pager with LongPressDraggable
    LongPressDraggable {

        HorizontalPager(state = pagerState) { pageIndex ->
            when (pageIndex) {
                0 -> Page1Content(pagerState)
                1 -> {
                    //Page2Content(pagerState)
                }
            }
        }
    }
}

@Stable
data class WigetListWrapper(
    val widgetList: List<Widget> = emptyList()
)

@Composable
fun Page1Content(pagerState: PagerState) {
    val wigetListWrapper = WigetListWrapper()
    //val widgetList = emptyList<Widget>() //viewModel.widgetList.collectAsState()
    DropTarget<Widget>(modifier = Modifier.fillMaxSize())
    { isInBound, droppedWidget ->
        if (!LocalDragTargetInfo.current.itemDropped) {
            if (isInBound) {
                droppedWidget?.let { widget ->
                    LocalDragTargetInfo.current.itemDropped = true
                    LocalDragTargetInfo.current.dataToDrop = null
                    val currentlyPlacedItem = getCurrentlyPlacedItemInList()
// Use pagerState, LocalDragTargetInfo.current.absolutePositionX,
// LocalDragTargetInfo.current.absolutePositionY to determine what's
// currently placed in the list and make changes to the list accordingly
// Example: If nothing is currently placed at the drop position, add the dropped widget to the list
                    if (currentlyPlacedItem == null) {
                        addWidgetToList(widget)
                    } else {
// Example: Swap the currently placed item with the dropped widget
                        moveWidgets(widget, currentlyPlacedItem)
                    }
                }
            }
        }
    }
    WidgetsList(pagerState, wigetListWrapper)
}

@Composable
fun WidgetsList(pagerState: PagerState, widgetListWrapper: WigetListWrapper) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(widgetListWrapper.widgetList) { widget ->
// this composable was defined earlier as
// each widget item is itself a drag target
            DragTargetWidgetItem(
                data = widget,
                pagerState = pagerState
            )
        }
    }
}

@Composable
fun DragTargetWidgetItem(
    data: Widget,
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val dataWrapper = DataToDrapWrapper(data)
    DragTarget<Widget>(
        pagerSize = 3, // Assuming there are two pages in the horizontal pager
        horizontalPagerState = pagerState,
        modifier = modifier.wrapContentSize(),
        dataToDrapWrapper = dataWrapper,
    ) { shouldAnimate ->
        WidgetItem(data, shouldAnimate)
    }
}

@Composable
fun WidgetItem(
    data: Widget,
    shouldAnimate: Boolean
) {
// Add your custom implementation for the WidgetItem here.
// This composable will render the content of the draggable widget.
// You can use the 'data' parameter to extract necessary information and display it.
// The 'shouldAnimate' parameter can be used to control animations if needed.
// Example: Displaying a simple card with the widget's name
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .graphicsLayer {
                // Scale the card when shouldAnimate is true
                scaleX = if (shouldAnimate) 1.2f else 1.0f
                scaleY = if (shouldAnimate) 1.2f else 1.0f
            },
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = data.name,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = data.description)
        }
    }
}

// Implement the required functions to handle adding and swapping widgets in the list
fun addWidgetToList(widget: Widget) {
// Add the dropped widget to the list
}

fun moveWidgets(widgetA: Widget, currentlyPlacedItem: Widget) {
// Move items in the list
}

// Implement the required function to get the currently placed item in the list based on drop position
fun getCurrentlyPlacedItemInList(): Widget? {
// Use pagerState, LocalDragTargetInfo.current.absolutePositionX,
// LocalDragTargetInfo.current.absolutePositionY
// to determine the currently placed item in the list
// based on the drop position
    return null
}

data class Widget(val name: String, val description: String)