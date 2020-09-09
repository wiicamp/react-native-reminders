package com.wiicamp.reactnativereminders

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.PermissionAwareActivity
import com.facebook.react.modules.core.PermissionListener
import java.util.*


public class RemindersModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), PermissionListener {
    var permissionPromise: Promise? = null;

    override fun getName(): String {
        return "Reminders"
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray): Boolean {
        Log.i("PERMISSION_RESULT", "requestCode: $requestCode, grantResults: ${grantResults.toString()}")
        when (requestCode) {
            9397 -> {
                val isGranted = !grantResults.any { predicate -> predicate != PackageManager.PERMISSION_GRANTED };
                permissionPromise?.resolve(isGranted);
            }
        }

        return true;
    }

    @ReactMethod
    fun requestPermission(promise: Promise) {
        val permissions = arrayOf(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

        var isAllowed = false;

        permissions.forEach { item ->
            run {
                isAllowed = ContextCompat.checkSelfPermission(reactApplicationContext.baseContext, item) == PackageManager.PERMISSION_GRANTED;
            }
        }

        if(isAllowed) {
            promise.resolve(true);
            return;
        }

        permissionPromise = promise;

        val activity = reactApplicationContext.currentActivity as PermissionAwareActivity

        activity.requestPermissions(permissions, 9397, this)
    }

    @ReactMethod
    fun getReminders(promise: Promise) {
        val projection: Array<String> = arrayOf(
                CalendarContract.CalendarAlerts._ID,
                CalendarContract.CalendarAlerts.TITLE,
                CalendarContract.CalendarAlerts.DESCRIPTION,
                CalendarContract.CalendarAlerts.DTSTART,
                CalendarContract.CalendarAlerts.DTEND,
                CalendarContract.CalendarAlerts.EVENT_LOCATION
        )

        val cursor = reactApplicationContext.contentResolver.query(
                CalendarContract.CalendarAlerts.CONTENT_URI,
                projection,
                null,
                null,
                null
        )

        Log.v("VERBOSE", "cursor.count = ${cursor?.count}")

        try {
            val results = WritableNativeArray();

            if (cursor?.count!! > 0) {
                while (cursor.moveToNext()) {
                    val calendarMap = WritableNativeMap();
                    for(columnIndex in 0 until cursor.columnCount) {
                        var key = cursor.getColumnName(columnIndex)
                        val value = cursor.getString(columnIndex)

                        if(key.equals("_id")) {
                            key = "id";
                        }

                        calendarMap.putString(key, value)
                    }
                    results.pushMap(calendarMap);
                }
            }
            promise.resolve(results);
        } catch (ex: AssertionError) { /*TODO: log exception and bail*/
            promise.resolve(WritableNativeArray());
        }

    }

    @ReactMethod
    fun addReminder(config: ReadableMap, promise: Promise) {
        val eventUriString = "content://com.android.calendar/events"
        val eventValues = ContentValues()

        eventValues.put("calendar_id", 1)
        eventValues.put("title", config.getString("title"))
        eventValues.put("description", config.getString("note"))
//        eventValues.put("eventLocation", place)

        val dtStart = config.getDouble("timestamp").toLong()
        Log.v("VERBOSE", "dtStart = $dtStart");
        Log.v("VERBOSE", "Calendar.getInstance().timeInMillis = ${Calendar.getInstance().timeInMillis}")
        eventValues.put("dtstart", dtStart)
        eventValues.put("dtend", dtStart)
        eventValues.put("eventStatus", 1)
        eventValues.put("eventTimezone", "UTC/GMT +2:00")
        eventValues.put("hasAlarm", 1)


        val eventUri: Uri? = reactApplicationContext.applicationContext.contentResolver.insert(Uri.parse(eventUriString), eventValues)
        val eventID = eventUri?.lastPathSegment?.toLong()
        val reminderUriString = "content://com.android.calendar/reminders"
        val reminderValues = ContentValues()
        reminderValues.put("event_id", eventID)
        reminderValues.put("minutes", 5)
        reminderValues.put("method", 1)
        reactApplicationContext.applicationContext.contentResolver.insert(Uri.parse(reminderUriString), reminderValues)

        val resolveValue = WritableNativeMap();
        resolveValue.putString("id", "$eventID")
        resolveValue.putString("title", config.getString("title"))
        resolveValue.putString("note", config.getString("note"))

        val alarms = WritableNativeArray();
        val alarmMap = WritableNativeMap();
        alarmMap.putDouble("dtstart", dtStart.toDouble())
        alarmMap.putDouble("dtend", dtStart.toDouble())
        alarms.pushMap(alarmMap)
        resolveValue.putArray("alarms", alarms)

        promise.resolve(resolveValue)

    }
}
