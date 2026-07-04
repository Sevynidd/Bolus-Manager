package sevynidd.diabetesapp.data.update

/** A GitHub release relevant for in-app updates: its tag, notes, and APK download asset. */
data class GitHubRelease(
    val tagName: String,
    val releaseNotes: String,
    val apkDownloadUrl: String,
    val apkFileName: String
)
