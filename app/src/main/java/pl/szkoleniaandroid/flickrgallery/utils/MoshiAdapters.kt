package pl.szkoleniaandroid.flickrgallery.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.IOException

private const val LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private val LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)!!

class Rfc3339LocalDateTimeJsonAdapter : JsonAdapter<LocalDateTime>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): LocalDateTime {
        val string = reader.nextString()
        return LocalDateTime.parse(string, LOCAL_DATE_TIME_FORMATTER)
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        if (value != null) {
            val string = LOCAL_DATE_TIME_FORMATTER.format(value)
            writer.value(string)
        } else {
            writer.nullValue()
        }
    }
}

class IsoLocalDateTimeJsonAdapter : JsonAdapter<OffsetDateTime>() {

    override fun fromJson(reader: JsonReader): OffsetDateTime? {
        val string = reader.nextString()
        return OffsetDateTime.parse(string, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

    override fun toJson(writer: JsonWriter, value: OffsetDateTime?) {
        if (value != null) {
            val string = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
            writer.value(string)
        } else {
            writer.nullValue()
        }
    }
}
