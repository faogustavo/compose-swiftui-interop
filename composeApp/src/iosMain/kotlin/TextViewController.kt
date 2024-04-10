import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
object TextViewController {
class State {
    val name = MutableStateFlow("")
}

    fun create(state: State) = ComposeUIViewController {
        val name by state.name.collectAsState()
        Text(if (name.isBlank()) "Fill your name" else "Hello $name!")
    }

    fun createReverse(
        factory: (state: State) -> UIViewController
    ) = ComposeUIViewController {
        var myName by remember { mutableStateOf("") }
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("This is my Kotlin View")

            MySwiftUIViewText(
                name = myName,
                factory = factory,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = { myName = getRandom(myName) }) {
                    Text("Toggle name")
                }
                Button(
                    onClick = { myName = "" },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White,
                    )
                ) {
                    Text("Clear name")
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
fun MySwiftUIViewText(
    name: String,
    factory: (state: TextViewController.State) -> UIViewController,
    modifier: Modifier = Modifier,
) {
    val swiftViewState = remember { TextViewController.State() }
    UIKitViewController(
        factory = { factory(swiftViewState) },
        modifier = modifier.fillMaxWidth().height(50.dp),
        update = { swiftViewState.name.value = name }
    )
}
