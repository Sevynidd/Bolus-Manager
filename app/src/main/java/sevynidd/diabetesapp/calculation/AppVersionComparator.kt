package sevynidd.diabetesapp.calculation

/**
 * Whether [latestVersion] (e.g. a GitHub release tag like `v1.2.0`) is newer than
 * [currentVersion] (e.g. the app's `versionName`). Compares dot-separated numeric segments in
 * order, left to right; a segment missing from the shorter string counts as `0`, and a
 * non-numeric segment (a malformed or unexpected tag) also counts as `0` so this never throws.
 */
fun isNewerVersion(currentVersion: String, latestVersion: String): Boolean {
    val currentSegments = currentVersion.toVersionSegments()
    val latestSegments = latestVersion.toVersionSegments()
    val segmentCount = maxOf(currentSegments.size, latestSegments.size)

    for (index in 0 until segmentCount) {
        val current = currentSegments.getOrElse(index) { 0 }
        val latest = latestSegments.getOrElse(index) { 0 }
        if (latest != current) return latest > current
    }

    return false
}

private fun String.toVersionSegments(): List<Int> {
    return removePrefix("v")
        .removePrefix("V")
        .substringBefore('-')
        .split('.')
        .map { it.toIntOrNull() ?: 0 }
}
