package com.example.codelab_compose_layout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.codelab_compose_layout.ui.theme.CodelabcomposelayoutTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodelabcomposelayoutTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    LayoutsCodelab()
                }
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Layouts Codelab")
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(

            ) {}
        }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    /*
    Column(modifier = modifier) {
        Text(text = "Hi there")
        Text(text = "thanks for going through the Layouts codelab")
    }*/
    MyOwnColumn(modifier.padding(8.dp)) {
        Text("MyOwnColumn")
        Text("places items")
        Text("vertically.")
        Text("We've done it by hand!")
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

/**
 * Create your own custom layout
 */
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout {measurable, constraints ->
        val pleaceble = measurable.measure(constraints)

        // Check if composable has a first baseline
        check(pleaceble[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = pleaceble[FirstBaseline]

        //Height of the composable with padding - first baseline
        val pleacebleY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = pleaceble.height + pleacebleY
        layout(pleaceble.width, height){
            // Where the composable gets placed
            pleaceble.placeRelative(0, pleacebleY)
        }
    }
)

/**
 * Simple example how a simple LazyList works.
 */
@Composable
fun LazyList() {
    // We have the scrolling position with this state that can also
    // be used to programmatically scroll the list
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            Text("Item #$it")
        }
    }
}

/**
 * Simple example how a simple LazyList with image works.
 */
@Composable
fun ImageList() {
    val scrollState = rememberLazyListState()

    LazyColumn(state = scrollState) {
        items(100) {
            ImageListItem(it)
        }
    }
}

@Composable
fun PhotographCard(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { /* Ignoring click */ })
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            //image goes here

        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            //LocalContentAlpha is defining opacity level of its children
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

/**
 * Using the layout composable
 */
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    // Custom layout attributes
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // measure and position children given constraints logic here
        // Don't constrain a cuild view further, measure then with the given constraint
        // List of measured children
        val pleacebles = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        //Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            pleacebles.forEach { pleaceble ->
                //Position item on the screen
                pleaceble.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += pleaceble.height
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
    CodelabcomposelayoutTheme {
        LayoutsCodelab()
    }
}

@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreviewList() {
    CodelabcomposelayoutTheme {
        ImageList()
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
    CodelabcomposelayoutTheme {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithNormalPaddingPreview() {
    CodelabcomposelayoutTheme {
        Text("Hi there!", Modifier.padding(top = 32.dp))
    }
}


/**
 * Preview for the PhotographyCard
@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
CodelabcomposelayoutTheme {
PhotographCard()
}
}
 */