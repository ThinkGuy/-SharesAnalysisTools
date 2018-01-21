package com.lxw.util

/**
 * Created by lxw on 2017/11/2.
 */

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URL

object HTMLParser {
    fun getElementsByAttributeValue(html: String, attribute: String, name: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByAttributeValue(attribute, name)
    }

    fun getElementsByClass(html: String, className: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByClass(className)
    }

    fun getElementsByTag(html: String, tagName: String): Elements {
        val document = Jsoup.parse(html)
        return document.html(html).getElementsByTag(tagName)
    }

    fun getElementsById(html: String, id: String): Element {
        val document = Jsoup.parse(html)
        return document.html(html).getElementById(id)
    }
}
