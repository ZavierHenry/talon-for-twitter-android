package com.klinker.android.twitter_l.data.roomdb

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Binder
import android.util.Log

import java.util.ArrayList
import java.util.Collections

class HomeContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        Log.d(TAG, "onCreate")
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return ""
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    @Synchronized
    override fun bulkInsert(uri: Uri, allValues: Array<ContentValues>): Int {
        return 0
    }


    private fun checkUID(context: Context): Boolean {
        val callingUid = Binder.getCallingUid()

        val pm = context.packageManager
        val packages = pm.getInstalledApplications(
                PackageManager.GET_META_DATA
        )

        for (packageInfo in packages) {
            if (callingUid == packageInfo.uid) {
                when (packageInfo.packageName) {
                    "com.klinker.android.twitter", "com.klinker.android.launcher", "com.klinker.android.launcher.twitter_page" -> return true
                }

            }
        }

        return false

    }

    companion object {
        internal val TAG = "HomeTimeline"

        val AUTHORITY = "com.klinker.android.twitter_l.material.android_l_provider"
        internal val BASE_PATH = "tweet_id"
        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$BASE_PATH")
        val STREAM_NOTI = Uri.parse("content://$AUTHORITY/stream")
    }


}
