package io.github.diegoflassa.data.adapters

import android.net.Uri
import com.squareup.moshi.ToJson
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter

class UriAdapter : JsonAdapter<Uri>() {
    @ToJson
    override fun toJson(writer: JsonWriter, value: Uri?) {
        value?.let { writer.value(it.toString()) }

    }

    @FromJson
    override fun fromJson(reader: JsonReader): Uri? {
        return if (reader.peek() != JsonReader.Token.NULL) {
            Uri.parse(reader.nextString())
        } else {
            null
        }
    }

}

