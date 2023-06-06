package jg.com.pubgolf.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertDateTime(dateTimeString: String): String {
    val sourceFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    val targetFormat = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")

    val dateTime = LocalDateTime.parse(dateTimeString, sourceFormat)
    return dateTime.format(targetFormat)
}