import androidx.compose.runtime.Composable
import com.lightning.androidfrontend.theme.MyAppTheme
import com.lightning.androidfrontend.ui.components.More

@Composable
fun MoreDetailsScreen(content: com.lightning.androidfrontend.ui.components.More){
    MyAppTheme {
        val argument: More = content
        argument.setContent()
    }
}

