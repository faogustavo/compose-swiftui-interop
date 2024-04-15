package native

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import data.KMPCoordinates
import effect.ObservableEffect
import ext.observeAsState
import vm.MapViewViewModel

@Composable
actual fun NativeMapView(modifier: Modifier, viewModel: MapViewViewModel) {
    val pins by viewModel.observableMarkers.observeAsState()

    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 13f)
    }

    ObservableEffect(viewModel.observableCoordinates) { _, newValue ->
        if (newValue == null) return@ObservableEffect

        cameraPosition.position =
            CameraPosition.fromLatLngZoom(newValue.toLatLng(), 13f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPosition,
    ) {
        for (pin in pins) {
            Marker(
                state = MarkerState(pin.location.toLatLng()),
                title = pin.title,
                icon = setCustomMapIcon(pin.monogram),
                onClick = {
                    viewModel.onMarkerClick(pin)
                    true
                }
            )
        }
    }
}

private fun KMPCoordinates.toLatLng(): LatLng = LatLng(lat, lng)

// From https://medium.com/p/a55ac696d565
@Composable
private fun setCustomMapIcon(text: String): BitmapDescriptor = remember(text) {
    val background = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 30.dp.value
    }

    val paintTextWhite = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        strokeWidth = 6.dp.value
        textSize = 48.dp.value
    }

    val border = Paint().apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        isAntiAlias = true
        color = Color.DKGRAY
        strokeWidth = 6.dp.value
    }

    val height = 150f
    val widthPadding = 80.dp.value
    val width = paintTextWhite.measureText(text, 0, text.length) + widthPadding
    val roundStart = height/3
    val path = Path().apply {
        arcTo(0f, 0f, roundStart * 2, roundStart * 2, -90f, -180f, true)
        lineTo(width/2 - roundStart / (2.5f), height * 2/3)
        lineTo(width/2, height)
        lineTo(width/2 + roundStart / (2.5f), height * 2/3)
        lineTo(width - roundStart, height * 2/3)
        arcTo(width - roundStart * 2, 0f, width, height * 2/3, 90f, -180f, true)
        lineTo(roundStart, 0f)
    }

    val bm = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)

    canvas.drawPath(path, background)
    canvas.drawPath(path, border)
    canvas.drawText(text, width/2, height * 2/3 * 2/3, paintTextWhite)

    BitmapDescriptorFactory.fromBitmap(bm)
}
