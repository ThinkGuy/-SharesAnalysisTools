package com.lxw.service

import com.lxw.model.Finance
import com.lxw.util.Constants
import com.lxw.util.NetRequest
import javaslang.collection.List
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

/**
 * Created by lxw on 2018/1/21.
 */

class FinanceAnalysisService {
    var dateList = ArrayList<String>()
    var finances = ArrayList<Finance>()
    var npMap = TreeMap<Double, Int>()
    var reVenueMap = TreeMap<Double, Int>()

    fun gatherFinanceInfo(type: Int, num: Int): TreeMap<String, TreeMap<String, String>> {
        var outMap = TreeMap<String, TreeMap<String, String>>()

        var url = """http://soft-f9.eastmoney.com/soft/gp13.php?code="""

        if (type == 2) {
            when (num.toString().length) {
                1 -> url = url + "00000" + num + "02&exp=0&tp=4"
                2 -> url = url + "0000" + num + "02&exp=0&tp=4"
                3 -> url = url + "000" + num + "02&exp=0&tp=4"
                4 -> url = url + "00" + num + "02&exp=0&tp=4"
            }
        } else {
            when (num.toString().length) {
                1 -> url = url + "60000" + num + "01&exp=0&tp=4"
                2 -> url = url + "6000" + num + "01&exp=0&tp=4"
                3 -> url = url + "600" + num + "01&exp=0&tp=4"
                4 -> url = url + "60" + num + "01&exp=0&tp=4"
            }
        }

        var html = NetRequest().netRequest(url)

        // 根据id获取table
        var table = html.getElementById("tablefont")

        // 使用选择器选择该table内所有的<tr> <tr/>
        var trs = table.select("tr")

        for (i in 0 until trs.size) {
            var tds = trs[i].select("td")
            var inMap = TreeMap<String, String>()

            for (j in 0 until tds.size) {
                var value = tds[j].select("span").text().trim()

                if (j == 0) {
                    if (Constants.REPORT_DATE.equals(value) || Constants.REVENUES.equals(value) || Constants.NET_PROFITS.equals(value)) {
                        outMap.put(value, inMap)
                    } else {
                        break
                    }
                } else if (i == 0) {
                    inMap.put(value, "")
                    dateList.add(value)
                } else {

                    if (value.endsWith("亿")) {
                        value = (value.substring(0, value.length - 1).toFloat() * 100000000).toString()
                    } else if (value.endsWith("万")) {
                        value = (value.substring(0, value.length - 1).toFloat() * 10000).toString()
                    }

                    inMap.put(dateList.get(j - 1), value)
                }
            }
        }

        outMap.forEach {
            println(it.key + " : " + it.value)
        }

        return outMap
    }

    fun analysisFinancce(tpye: Int, num: Int) {
        println(num)

        val financesMap = gatherFinanceInfo(tpye, num)
        var finance = Finance()

        financesMap.forEach {
            if (!Constants.REPORT_DATE.equals(it.key)) {
                var totalValue: Long = 0
                var firstValue: Long = BigDecimal(it.value.firstEntry().value).toPlainString().toLong()
                it.value.forEach() {
                    totalValue += BigDecimal(it.value).toPlainString().toLong()
                }

                var result: Double = totalValue.toDouble() / (firstValue * dateList.size)

                when (it.key) {
                    Constants.NET_PROFITS -> {
                        finance.averageGrowthOfNPRate = result
                        npMap.put(result, num)
                    }
                    Constants.REVENUES -> {
                        finance.averageGrowthOfRVNRate = result
                        reVenueMap.put(result, num)
                    }
                }
            }
        }

        finances.add(finance)
    }


    fun sortAchievement() {
        println("净利润:")


        reVenueMap.forEach {

            println(it.value.toString() + " : " + it.key)
        }

        println("收入:")

        npMap.forEach {
            println(it.value.toString() + " : " + it.key)
        }
    }

    fun asyncGatherFinance() = runBlocking {
        var num = 0..2926
        var shangNum = 0..1999
        var shangNum2 = 3000..3999
        var jobs = mutableListOf<Job>()

        val time = measureTimeMillis {
            for (i in 0..shangNum.count()) {
                try {
                    analysisFinancce(1, i)
                } catch (i: RuntimeException) {
                    println("出問題了")
                }
            }

            for (i in 3000..shangNum.count()) {
                try {
                    analysisFinancce(1, i)
                } catch (i: RuntimeException) {
                    println("出問題了")
                }
            }

            for (i in 1..num.count()) {
                try {
                    analysisFinancce(2, i)
                } catch (i: RuntimeException) {
                    println("出問題了")
                }
            }
        }
        println("Completed in $time ms")

        sortAchievement()
    }

}

fun main(args: Array<String>) {
    FinanceAnalysisService().asyncGatherFinance()
}