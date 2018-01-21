package com.lxw.model

/**
 * Created by lxw on 2018/1/21.
 */
class Finance {

    /**
     * 股票名
     */
    var sharesName = ""
    /**
     * 股票代码
     */
    var sharesKey = ""

    /**
     * 净利润
     */
    val netProfits : String = ""

    /**
     * 营业收入
     */
    val Revenues: String = ""

    /**
     * 净利润增长率
     */
    var averageGrowthOfNPRate: Double = 0.0

    /**
     * 营业收入增长率
     */
    var averageGrowthOfRVNRate: Double = 0.0

    override fun toString(): String {
        return "Finance(sharesName='$sharesName', sharesKey='$sharesKey', netProfits='$netProfits', Revenues='$Revenues', averageGrowthOfNPRate=$averageGrowthOfNPRate, averageGrowthOfRVNRate=$averageGrowthOfRVNRate)"
    }


}

