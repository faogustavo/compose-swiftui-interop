package co.touchlab.compose.interop.ComposeInterop

import MainView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import org.brightify.hyperdrive.multiplatformx.LifecycleGraph
import vm.RootViewModel

class MainActivity : ComponentActivity() {

    private val root = LifecycleGraph.Root(this)
    private val rootViewModel = RootViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootViewModel.lifecycle.removeFromParent()
        root.addChild(rootViewModel.lifecycle)

        setContent {
            MainView(rootViewModel)
        }

        lifecycleScope.launchWhenResumed {
            val cancelAttach = root.attach(lifecycleScope)
            try {
                awaitCancellation()
            } finally {
                cancelAttach.cancel()
            }
        }
    }
}
