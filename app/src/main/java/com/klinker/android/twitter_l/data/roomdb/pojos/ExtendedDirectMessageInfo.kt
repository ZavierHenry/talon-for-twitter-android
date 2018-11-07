package com.klinker.android.twitter_l.data.roomdb.pojos

class ExtendedDirectMessageInfo {

    var id: Long = 0
    var text: String? = null
    var time: Long = 0
    var media: String? = null

    var senderName: String? = null
    var senderScreenName: String? = null
    var senderProfilePic: String? = null

    var recipientName: String? = null
    var recipientScreenName: String? = null
    var recipientProfilePic: String? = null

    var gifUrl: String? = null
    var mediaLength: Int = 0

}
