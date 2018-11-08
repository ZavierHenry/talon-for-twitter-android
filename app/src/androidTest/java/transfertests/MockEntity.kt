package transfertests

import android.content.ContentValues
import android.database.Cursor

internal interface MockEntity {

     fun setContentValues(contentValues: ContentValues)

}
