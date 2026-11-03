package sevynidd.diabetesapp.screens.settings.update

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/** The installed app's `versionName`, or an empty string if the package manager has none. */
fun Context.appVersionName(): String {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
    }
    return info.versionName.orEmpty()
}
