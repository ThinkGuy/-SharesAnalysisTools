package com.lxw.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException

/**
 * Created by lxw on 2018/1/21.
 */

class NetRequest {

    /**
     * 网络请求器
     */
    fun netRequest(url: String): Document {

        var html: Document = Document("")

        try {
            html = Jsoup.connect(url).timeout(4000).userAgent("Mozilla").get()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
        } catch (e1: RuntimeException) {
            e1.printStackTrace()
        }

        return html
    }
}