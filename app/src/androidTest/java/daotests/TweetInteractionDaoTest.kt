package daotests

import org.junit.After

class TweetInteractionDaoTest : DaoTest() {






    @After
    fun clearTables() {
        clearTestDatabase()
    }


    companion object {


    }



}