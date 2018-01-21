package com.lxw.service

import com.lxw.model.SharesInfo
import com.lxw.util.Constants
import com.lxw.util.HTMLParser
import kotlinx.coroutines.experimental.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.net.SocketTimeoutException
import java.util.*
import kotlin.system.measureTimeMillis


/**
 * Created by lxw on 2017/11/2.
 */

class GatherInformationService {
    var sharesMap = Hashtable<String, SharesInfo>()
    var sharesInfo = SharesInfo()


    /**
     * 收集股票信息
     */
    fun gatherInformationService(num: Int) {

//        sh600000 ..63999
//        sz000001..002911
        var url = ""
        var html: Document

        sharesInfo = SharesInfo()
        print(" " + num)

        when (num.toString().length) {
            1 -> url = Constants.URL_PREFIX + "sz00000" + num + ".html"
            2 -> url = Constants.URL_PREFIX + "sz0000" + num + ".html"
            3 -> url = Constants.URL_PREFIX + "sz000" + num + ".html"
            4 -> url = Constants.URL_PREFIX + "sz00" + num + ".html"
        }

        try {
            html = Jsoup.connect(url).timeout(4000).userAgent("Mozilla").get()
            getTitleInfo(html)
            getKeyInfo(html)
            // add to db
            sharesMap.put(sharesInfo.sharesName, sharesInfo)
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        } catch (e1: RuntimeException) {
            e1.printStackTrace()
        }
    }

    /**
     * 获取关键信息
     */
    fun getKeyInfo(html: Document) {
        var infoMap = mutableMapOf<String, String>()
        var infoList = mutableListOf<String>()
        val form: Elements = HTMLParser.getElementsByClass(html.toString(), "bets-content")

        if (form.size == 0) {
            return
        }

        var splits = form[0].toString().split("\n")

        splits.forEach {
            if (it.indexOf("<") < 0) {
                infoList.add(it)
            }
        }

        for (i in 0..infoList.size - 1 step 2) {
            infoMap.put(infoList[i].trim(), infoList[i + 1].trim())
        }

        infoMap.forEach {
            var value = it.value

            if ("--".equals(value)) {
                return@forEach
            }

            when (it.key) {
                Constants.TODAY_START_PRICE -> sharesInfo.todayStartPrice = value.toDouble()
                Constants.YEST_FINAL_PRICE -> sharesInfo.yestFinalPrice = value.toDouble()
                Constants.VOLUME -> sharesInfo.volume = value.substring(0, value.length - 2).toDouble()
                Constants.TURNOVER_RATE -> sharesInfo.turnOverRate = value.substring(0, value.length - 1).toDouble()
                Constants.HIGHEST_PRICE -> sharesInfo.highestPrice = value.toDouble()
                Constants.LOWEST_PRICE -> sharesInfo.lowestPrice = value.toDouble()
                Constants.UP_STOP -> sharesInfo.upStop = value.toDouble()
                Constants.DOWN_STOP -> sharesInfo.downStop = value.toDouble()
                Constants.INVOL -> sharesInfo.invol = value.substring(0, value.length - 2).toDouble()
                Constants.OUTER_DISE -> sharesInfo.outerDise = value.substring(0, value.length - 2).toDouble()
                Constants.TURNOVER -> sharesInfo.turnOver = value.substring(0, value.length - 1).toDouble()
                Constants.AMPLITUDE -> sharesInfo.amplitude = value.substring(0, value.length - 1).toDouble()
                Constants.COMMITTEE -> sharesInfo.committee = value.substring(0, value.length - 1).toDouble()
                Constants.VOLUME_RATIO -> sharesInfo.volumeRatio = value.toDouble()
                Constants.CAPITALIZATION -> sharesInfo.capitalization = value.substring(0, value.length - 1).toDouble()
                Constants.TOTAL_MARKET_VALUE -> sharesInfo.totalMarketValue = value.substring(0, value.length - 1).toDouble()
                Constants.PE_RATIO -> sharesInfo.peRatio = value.toDouble()
                Constants.PB_RATIO -> sharesInfo.pbRatio = value.toDouble()
                Constants.EARNINGS_PER_SHARE -> sharesInfo.earningsPerShare = value.toDouble()
                Constants.EPS -> sharesInfo.ePS = value.toDouble()
                Constants.GENERAL_CAPITAL -> sharesInfo.generalCapital = value.substring(0, value.length - 1).toDouble()
                Constants.FLOW_OF_EQUITY -> sharesInfo.flowOfEquity = value.substring(0, value.length - 1).toDouble()
                else -> println(it.toString() + "添加失败")
            }
        }
    }

    /**
     * 获取股票名与股票代码
     */
    fun getTitleInfo(html: Document) {
        val form: Elements = HTMLParser.getElementsByClass(html.toString(), "bets-name")

        if (form.size == 0) {
            return
        }

        var title = form.text()

        sharesInfo.sharesName = title.substring(0, title.indexOf("(")).trim()
        sharesInfo.sharesKey = title.substring(title.indexOf("(") + 1, title.length - 1).trim()
    }

    fun saveSharesInfoToFile() {
        var file = File("""D:\sharesInfo.txt""")
        sharesMap.forEach {
            file.appendText(it.value.toString() + "\n")
        }
    }

    /**
     * 并发采集股票信息
     */
    fun asyncGatherInfo() = runBlocking {
        var num = 0..2911
        var jobs = mutableListOf<Job>()

        val time = measureTimeMillis {
            num.forEach {
                jobs.add(launch(CommonPool) { gatherInformationService(it) })
            }

            jobs.forEach { it.join() }
        }
        println("Completed in $time ms")

        saveSharesInfoToFile()
        AnalysisSharesInfo().readSharesInfoFromFile()
    }
}