package screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter.State
import coil3.compose.rememberAsyncImagePainter
import data.KMPMapMarker

class LocationDetails(private val marker: KMPMapMarker) : Screen {
    @Composable
    override fun Content() {
        Column {
            Header()
            Body()
        }
    }

    @Composable
    private fun Header() {
        val bottomNav = LocalBottomSheetNavigator.current
        val image = rememberAsyncImagePainter(marker.image)

        Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            Image(
                painter = image,
                modifier = Modifier.fillMaxSize(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            AnimatedVisibility(
                visible = image.state is State.Success,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0f to Color.Black.copy(alpha = 0.5f),
                                    0.2f to Color.Transparent,
                                    0.7f to Color.Transparent,
                                    1f to Color.Black.copy(alpha = 0.5f),
                                ),
                            )
                        ),
                )
            }

            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { bottomNav.hide() }) {
                        Icon(
                            modifier = Modifier.size(48.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Button",
                            tint = Color.White,
                        )
                    }
                }

                Text(
                    marker.title,
                    color = Color.White,
                    maxLines = 1,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                )
            }
        }
    }

    @Composable
    private fun Body() {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp, bottom = 32.dp)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "\t ${marker.description}",
                modifier = Modifier,
                textAlign = TextAlign.Justify
            )
            Text(
                "- Wikipedia",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}
