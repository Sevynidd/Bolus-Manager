package sevynidd.diabetesapp.data.update

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

private const val GITHUB_API_BASE = "https://api.github.com/repos"
private const val CONNECT_TIMEOUT_MILLIS = 10_000
private const val READ_TIMEOUT_MILLIS = 10_000
private const val HTTP_OK = 200

/**
 * Looks up the latest GitHub release for [owner]/[repo] and exposes its APK asset for in-app
 * updates, if the release publishes one whose name ends in `.apk`.
 */
class GitHubReleaseRepository(
    private val owner: String,
    private val repo: String
) {
    /**
     * The latest published release with a `.apk` asset, or `null` if there isn't one, the
     * request fails, or the response can't be parsed.
     */
    suspend fun fetchLatestRelease(): GitHubRelease? = withContext(Dispatchers.IO) {
        try {
            fetchLatestReleaseOrThrow()
        } catch (ignored: IOException) {
            null
        } catch (ignored: JSONException) {
            null
        }
    }

    private fun fetchLatestReleaseOrThrow(): GitHubRelease? {
        val connection = URL("$GITHUB_API_BASE/$owner/$repo/releases/latest")
            .openConnection() as HttpURLConnection
        connection.connectTimeout = CONNECT_TIMEOUT_MILLIS
        connection.readTimeout = READ_TIMEOUT_MILLIS
        connection.setRequestProperty("Accept", "application/vnd.github+json")

        return try {
            if (connection.responseCode != HTTP_OK) return null

            parseRelease(JSONObject(connection.inputStream.bufferedReader().readText()))
        } finally {
            connection.disconnect()
        }
    }

    private fun parseRelease(json: JSONObject): GitHubRelease? {
        val assets = json.getJSONArray("assets")
        val apkAsset = (0 until assets.length())
            .map { assets.getJSONObject(it) }
            .firstOrNull { it.getString("name").endsWith(".apk") }
            ?: return null

        return GitHubRelease(
            tagName = json.getString("tag_name"),
            releaseNotes = json.optString("body"),
            apkDownloadUrl = apkAsset.getString("browser_download_url"),
            apkFileName = apkAsset.getString("name")
        )
    }
}
