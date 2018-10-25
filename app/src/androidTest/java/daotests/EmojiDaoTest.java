package daotests;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.klinker.android.twitter_l.data.roomdb.entities.Emoji;
import com.klinker.android.twitter_l.data.sq_lite.EmojiSQLiteHelper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class EmojiDaoTest extends DaoTest {

    @BeforeClass
    public static void initDatabase() {
        initTestDatabase();
    }

    @Test
    public void insertEmojiTest() {
        Emoji emoji = new Emoji("Smiley face", ":}");
        emoji.count = 33;
        testDatabase.emojiDao().insertEmoji(emoji);
        Cursor cursor = testDatabase.query("SELECT * FROM emojis", null);
        assertThat("Cursor was not returned to check database value", cursor, notNullValue());
        assertThat("Emoji did not insert properly into database", cursor.getCount(), is(1));
        assertThat("Emoji did not insert properly into database", cursor.moveToFirst(), is(true));
        assertThat("Emoji text not saved properly", cursor.getString(cursor.getColumnIndex("text")), is(emoji.text));
        assertThat("Emoji icon was not saved properly", cursor.getString(cursor.getColumnIndex("icon")), is(emoji.icon));
        assertThat("Emoji count was not saved properly", cursor.getInt(cursor.getColumnIndex("count")), is(emoji.count));
        cursor.close();
    }

    @Test
    public void insertEmojiWithDuplicateIcons() {
        Emoji emoji = new Emoji("Smiley face", ":>");
        emoji.count = 44;

        ContentValues contentValues = new ContentValues();
        contentValues.put("text", emoji.text);
        contentValues.put("icon", emoji.icon);
        contentValues.put("count", emoji.count);

        long id = testDatabase.getOpenHelper().getWritableDatabase().insert("emojis", SQLiteDatabase.CONFLICT_ABORT, contentValues);
        assertThat("Database setup did not execute correctly", id, not(-1));

        int oldValue = emoji.count;
        emoji.count += 243;

        testDatabase.emojiDao().insertEmoji(emoji);
        Cursor cursor = testDatabase.query("SELECT * FROM emojis", null);
        assertThat("Database saved when it shouldn't have saved", cursor.getCount(), is(1));
        assertThat("Error getting cursor value", cursor.moveToFirst(), is(true));
        assertThat("New value was saved instead of the old value", cursor.getInt(cursor.getColumnIndex("count")), is(oldValue));

    }


    @Test
    public void deleteEmojiTest() {
        String text = "Smiley face";
        String icon = ":)";
        int count = 44;

        ContentValues contentValues = new ContentValues();
        contentValues.put("icon", icon);
        contentValues.put("text", text);
        contentValues.put("count", count);

        long id = testDatabase.getOpenHelper().getWritableDatabase().insert("emojis", SQLiteDatabase.CONFLICT_ABORT, contentValues);
        assertThat("Database setup did not work properly", id, not(-1));

        testDatabase.emojiDao().deleteEmoji(id);
        Cursor cursor = testDatabase.query("SELECT * FROM emojis", null);
        assertThat("Cannot get cursor to check result", cursor, notNullValue());
        assertThat("Element did not delete properly", cursor.getCount(), is(0));
        cursor.close();

    }

    //increment emoji count test

    @Test
    public void getRecents() {

    }

    @Test
    public void deleteLeastUsedRecents() {


    }


    @Test
    public void deleteListUsedRecentsOutsideBounds() {

    }


    @After
    public void clearDatabase() {
        testDatabase.clearAllTables();
    }

    @AfterClass
    public static void closeDatabase() {
        testDatabase.close();
    }



}
