package com.klinker.android.twitter_l.data.roomdb

import androidx.room.*

const val maxEmojis = 60

@Entity(tableName = "emojis", indices = [Index(value = ["icon"], unique = true)])
data class Emoji @JvmOverloads constructor(
        val text: String,
        val icon: String,
        val count: Long = 0,
        @PrimaryKey(autoGenerate = true) val id: Long = 0
)

@Dao
abstract class EmojiDao : BaseDao<Emoji>() {

    fun insert(entity: Emoji) : Emoji {
        val id = insertEntity(entity)
        return entity.copy(id = id)
    }


    fun insert(vararg entities: Emoji) : List<Emoji> {
        return insertEntities(*entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun insert(entities: List<Emoji>) : List<Emoji> {
        return insertEntities(entities).mapIndexed { index, id ->
            entities[index].copy(id = id)
        }
    }

    fun incrementEmojiCount(emoji: Emoji) : Emoji {
        val updatedEmoji = emoji.copy(count = emoji.count + 1)
        val rowsUpdated = update(updatedEmoji)
        return if (rowsUpdated > 0) updatedEmoji else emoji
    }

    @Query("DELETE FROM emojis WHERE id NOT IN (SELECT id FROM emojis ORDER BY count DESC LIMIT $maxEmojis)")
    protected abstract fun trimEmojis()

    @Query("SELECT * FROM emojis ORDER BY count DESC LIMIT $maxEmojis")
    abstract fun queryEmojis() : List<Emoji>

    fun getAllEmojis() : List<Emoji> {
        val limit = 60
        val emojis = queryEmojis()
        trimEmojis()
        return emojis
    }


}