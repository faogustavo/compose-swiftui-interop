import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import nav.TopAppBarProvider
import tabs.HomeTab
import tabs.MapViewInterop
import vm.RootViewModel

@Composable
fun MainView(rootViewModel: RootViewModel) {
    MaterialTheme {
        BottomSheetNavigator {
            TabNavigator(HomeTab) {
                Scaffold(
                    content = {
                        Surface(modifier = Modifier.padding(it)) {
                            CurrentTab()
                        }
                    },
                    topBar = { CurrentTopAppBar() },
                    bottomBar = {
                        BottomNavigation {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(MapViewInterop(rootViewModel.mapViewModel))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val options = tab.options

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                painter = options.icon ?: return@BottomNavigationItem,
                contentDescription = options.title,
            )
        }
    )
}

@Composable
private fun CurrentTopAppBar() {
    val tabNavigator = LocalTabNavigator.current
    val currentTab = tabNavigator.current as? TopAppBarProvider ?: return

    currentTab.TopBar()
}
