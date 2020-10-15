package com.aveworks.eagle.data

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown=true)
data class TransactionOutput(
    @field:JsonProperty("addr") val address: String,
    @field:JsonProperty("value") val value: Long,
    @field:JsonProperty("xpub") val xpub: XPub?,
) : Parcelable

//
////“txs” -> “out” - an array of the outputs of the transaction. Transactions can have multiple outputs, but the majority only have two. One is a “change” output that is returned to the wallet and should be ignored if you’re planning on displaying the recipient’s address. A “change” output easily spotted because the output has an “xpub” -> “m” field which matches the xPub parameter in the multiaddress call
//
//
//{
//
//
//    "xpub": {
//        "m": "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ",
//        "path": "M/0/148"
//    },
//    "value": 559182,
//
//    ],
//
//}
//]
//
//
//"type": 0,
//"spent": true,
//"value": 575806,
//"spending_outpoints": [
//{
//    "tx_index": 0,
//    "n": 0
//}
//],
//"addr": "1LMmvgrvSpufWEQFj4mrRYGuA7L44kvg8w",
//"n": 0,
//"tx_index": 0,
//"script": "76a914d458360eeb9f58e7b104719b011c4122825ee07088ac"