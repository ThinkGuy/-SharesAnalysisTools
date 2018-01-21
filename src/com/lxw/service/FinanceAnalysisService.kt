package com.lxw.service

import com.lxw.util.NetRequest
import org.jetbrains.kotlin.codegen.initializeVariablesForDestructuredLambdaParameters
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.util.*

/**
 * Created by lxw on 2018/1/21.
 */

class FinanceAnalysisService {

    fun analysis() {
        var outMap = TreeMap<String, TreeMap<String, String>>()
        var headList = ArrayList<String>()

        var url = """http://soft-f9.eastmoney.com/soft/gp13.php?code=00206502&exp=0&tp=4"""
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
                    outMap.put(value, inMap)
                } else if (i == 0) {
                    inMap.put(value, "")
                    headList.add(value)
                } else {

                    if (value.endsWith("亿")) {
                        value = (value.substring(0, value.length - 1).toFloat() * 100000000).toString()
                    } else if (value.endsWith("万")) {
                        value = (value.substring(0, value.length - 1).toFloat() * 10000).toString()
                    }

                    inMap.put(headList.get(j - 1), value)
                }
            }
        }

        outMap.forEach {
            println(it.key + " : " + it.value)
        }


        println(outMap.firstEntry())


        println()
    }
}


fun main(args: Array<String>) {
    FinanceAnalysisService().analysis()
}