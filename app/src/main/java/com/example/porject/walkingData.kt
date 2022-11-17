package com.example.porject

import androidx.room.Entity

@Entity(tableName = "walkingTable")
class walkingData (var _id : Int?, var weather : String?, var address : String?, var locationX : String?, var locationY : String?,
                   var contents : String?, var mood : String?, var picture : String?, var createDataStr : String?) {
}