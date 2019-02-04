package transfertests

import android.content.ContentValues

typealias Mismatch = Pair<Any?, Any?>
typealias FieldMismatch = Pair<String, Mismatch>


internal abstract class MockEntity<T> {

    fun anyMismatches(other: T) : Boolean {
        return showMismatches(other).isEmpty()
    }

    abstract fun showMismatches(other: T) : Collection<FieldMismatch>

    abstract fun setContentValues(contentValues: ContentValues)

    protected fun makeMismatch(name: String, expected: Any?, actual: Any?) : FieldMismatch {
        return name to (expected to actual)
    }

}
