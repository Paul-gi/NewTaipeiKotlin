package com.example.newtaipeizookotlin.tools

import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.net.URLDecoder
import java.net.URLEncoder
import java.security.MessageDigest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


fun String.getColorSpannable(color: Int): Spannable {
    val indexStart = 0
    val spannable: Spannable = SpannableString(this)
    if (this.contains("\n")) {
        val indexEnd: Int = this.indexOf("\n")
        spannable.setSpan(
            ForegroundColorSpan(color),
            indexStart,
            indexEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

fun String.getColorSpannable(color: Int, content: String): Spannable {
    val indexStart = this.indexOf(content)
    val indexEnd = indexStart + content.length
    return getColorSpannable(color, indexStart, indexEnd, content)
}

fun String.getLinkSpannable(url: String, content: String): Spannable {
    val spannable: Spannable = SpannableString(this)

    val indexStart = this.indexOf(content)
    val indexEnd = indexStart + content.length

    if (this.contains(content)) {
        spannable.setSpan(
            URLSpan(url),
            indexStart,
            indexEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

fun String.getColorSpannable(color: Int, start: Int, end: Int, content: String): Spannable {
    val spannable: Spannable = SpannableString(this)
    if (this.contains(content)) {
        spannable.setSpan(
            ForegroundColorSpan(color),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    return spannable
}

fun String.isIdentity(): Boolean {
    if (isNotEmpty()) {
        return matches(Regex("[A-Z]{1}[0-9]{9}"))
    }
    return false
}

fun String.isAccount(): Boolean {
    if (isNotEmpty()) {
        return matches(Regex("[A-Za-z0-9]{1,10}"))
    }
    return false
}

fun String.isEmailAddress(): Boolean {
    try {
        if (this.isNotEmpty()) {
            return matches(
                Regex(
                    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                            "\\@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(" +
                            "\\." +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                            ")+"
                )
            )
        } else {
            return false
        }
    } catch (e: Exception) {
        return false
    }
}

fun String.barcode39(): String {
    return this.trim().toUpperCase().replace(Regex("[^A-Z0-9\\s\\-\\.\\s\\$/\\+\\%]*"), "")
}

fun String.isNumber(): Boolean {
    try {
        if (this.isEmpty()) {
            return false
        } else {
            return this.find { it.toInt() < 48 || it.toInt() > 57 } == null
        }
    } catch (e: NumberFormatException) {
        return false
    }
}

fun String.toHtml(): Spanned {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
            Html.fromHtml(this.toFilterHtml(), Html.FROM_HTML_MODE_LEGACY)
        }
        else -> {
            Html.fromHtml(this.toFilterHtml())
        }
    }
}


fun String.toFilterHtml(): String {
    return this.replace("＜", "<").replace("＞", ">").replace("＃", "#")
}

fun String.sha256(): String {
    return try {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(this.toByteArray(Charsets.UTF_8))
        val digest = md.digest()
        //HexString
        String.format("%064x", BigInteger(1, digest))
    } catch (ex: java.lang.Exception) {
        ""
    }
}

fun String.toURLDecode(): String {
    val temp = this.replace(Regex("%(?![0-9a-fA-F]{2})"), "%")
    return try {
        URLDecoder.decode(temp, "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        this
    }
}

fun String.toURLEncode(): String {
    return try {
        URLEncoder.encode(this, "UTF-8")
    } catch (e: UnsupportedEncodingException) {
        this
    }
}


/**
 * 隱碼規則
 * 帳戶：不用隱碼
 * 手機：0910***888
 * 信用卡卡號：************1234
 * 身分證字號：G2******90
 * 姓名： 林Ｏ如
 */
fun String.mask(start: Int, end: Int, mask: String): String {
    return if (this.length > start && this.length > end) {
        val sb = StringBuilder()
        val chars = this.toCharArray()
        for (i in 0 until chars.size) {
            if (i < start) {
                sb.append(chars[i])
            } else if (i >= chars.size - end && i < chars.size) {
                sb.append(chars[i])
            } else {
                sb.append(mask)
            }
        }
        sb.toString()
    } else {
        ""
    }
}

fun String.convertDateFormat(originFormat: String, expectedFormat: String): String {
    return try {
        val sdf1 = SimpleDateFormat(originFormat, Locale.TAIWAN)
        val sdf2 = SimpleDateFormat(expectedFormat, Locale.TAIWAN)
        val date: Date? = sdf1.parse(this)
        if (date != null) {
            sdf2.format(date)
        } else {
            ""
        }
    } catch (e: ParseException) {
        ""
    }
}

/**
 * 轉換時間字串 format 格式
 */
fun String.convertDateString(from: SimpleDateFormat, to: SimpleDateFormat): String? {
    return try {
        from.parse(this)
    } catch (e: ParseException) {
        null
    }?.let {
        to.format(it)
    }
}

fun String.filterSpecialChar(): String {
    val regEx = "[/\\:~`  !@#$%^&*()+=.,?<>|\"'\n\t]"
    val patten = Pattern.compile(regEx)
    val matcher = patten.matcher(this)
    return matcher.replaceAll("")
}


fun String.verifyPasswordRule(): Boolean {
    val regex = "^[A-Za-z0-9!@#\$%^&*()]+\$"
    val patten = Pattern.compile(regex)
    val matcher = patten.matcher(this)
    return matcher.matches()
}


fun String.toDate(format: String): Date? {
    return try {
        SimpleDateFormat(format, Locale.TAIWAN).parse(this)
    } catch (e: ParseException) {
        null
    }
}