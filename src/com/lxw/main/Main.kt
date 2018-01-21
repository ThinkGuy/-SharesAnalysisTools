package com.lxw.main


import com.lxw.service.AnalysisSharesInfo
import com.lxw.service.GatherInformationService

fun main(args: Array<String>) {
    GatherInformationService().asyncGatherInfo()
    AnalysisSharesInfo().readSharesInfoFromFile()
}
