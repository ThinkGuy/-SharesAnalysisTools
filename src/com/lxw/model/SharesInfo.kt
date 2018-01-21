package com.lxw.model

/**
 * Created by lxw on 2017/11/2.
 */

class SharesInfo {
    /**
     * 股票名
     */
    var sharesName = ""
    /**
     * 股票代码
     */
    var sharesKey = ""
    /**
     * 今开
     */
    var todayStartPrice = 0.0
    /**
     * 今收
     */
    var todayFinalPrice = 0.0
    /**
     * 昨收
     */
    var yestFinalPrice = 0.0
    /**
     * 成交量
     */
    var volume = 0.0
    /**
     * 换手率
     */
    var turnOverRate = 0.0
    /**
     * 最高
     */
    var highestPrice = 0.0
    /**
     * 最低
     */
    var lowestPrice = 0.0
    /**
     * 涨停
     */
    var upStop = 0.0
    /**
     * 跌停
     */
    var downStop = 0.0
    /**
     * 内盘
     */
    var invol = 0.0
    /**
     * 外盘
     */
    var outerDise = 0.0
    /**
     * 成交额
     */
    var turnOver = 0.0
    /**
     * 振幅
     */
    var amplitude = 0.0
    /**
     * 委比
     */
    var committee = 0.0
    /**
     * 量比
     */
    var volumeRatio = 0.0
    /**
     * 流通市值
     */
    var capitalization = 0.0
    /**
     * 总市值
     */
    var totalMarketValue = 0.0
    /**
     * 市盈率
     */
    var peRatio = 0.0
    /**
     * 市净率
     */
    var pbRatio = 0.0
    /**
     * 每股收益
     */
    var earningsPerShare = 0.0
    /**
     * 每股净资产
     */
    var ePS = 0.0

    /**
     * 总股本
     */
    var generalCapital = 0.0
    /**
     * 流通股本
     */
    var flowOfEquity = 0.0

    override fun toString(): String {
        return "sharesName='$sharesName', sharesKey='$sharesKey', todayStartPrice=$todayStartPrice, todayFinalPrice=$todayFinalPrice, yestFinalPrice=$yestFinalPrice, volume=$volume, turnOverRate=$turnOverRate, highestPrice=$highestPrice, lowestPrice=$lowestPrice, upStop=$upStop, downStop=$downStop, invol=$invol, outerDise=$outerDise, turnOver=$turnOver, amplitude=$amplitude, committee=$committee, volumeRatio=$volumeRatio, capitalization=$capitalization, totalMarketValue=$totalMarketValue, peRatio=$peRatio, pbRatio=$pbRatio, earningsPerShare=$earningsPerShare, ePS=$ePS, generalCapital=$generalCapital, flowOfEquity=$flowOfEquity"
    }
}