package com.shivam.newsappshorts.utility

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.shivam.newsappshorts.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Utility {

    fun Context.isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    fun convertDateFormat(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Example: "26 Feb 2025"
        val date = inputFormat.parse(dateStr)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun shareWithDynamicLink(context: Context, title: String, link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Read: $title")
            putExtra(
                Intent.EXTRA_TEXT,
                "Check this out: $link\n\nShared via ${context.getString(R.string.app_name)}"
            )
        }
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "Share via ${context.getString(R.string.app_name)}"
            )
        )
    }

}