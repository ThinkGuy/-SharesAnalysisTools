package com.lxw.service

import com.lxw.model.SharesInfo
import java.io.File

/**
 * Created by lxw on 2017/11/7.
 */

class AnalysisSharesInfo {
    var sharesMap = mutableMapOf<String, Double>()
    var peRatioList = mutableListOf<Double>()

    fun readSharesInfoFromFile() {

        File("""D:\sharesInfo.txt""").readLines().forEach { line ->
            var splits = line.split(",")
            var key = ""
            var value = 0.0
            splits.forEach {
                var items = it.split("=")
                when (items[0].trim()) {
                    "sharesName" -> key = items[1].substring(1, items[1].length - 1)
                    "peRatio" -> value = items[1].toDouble()
                }
            }
            sharesMap.put(key, value)
            peRatioList.add(value)
        }
        sort()
    }

    fun sort() {
        peRatioList.sort()

        peRatioList.forEach {
            sharesMap.forEach { key, value ->
                if (it != 0.0 && value == it) {
                    println(key + " : " + value)
                }
            }
        }
    }
}