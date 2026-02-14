package com.gobots.dataprovider.configuration

import com.gobots.model.EventStatus
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

@WritingConverter
class EventStatusToString : Converter<EventStatus, String> {
    override fun convert(source: EventStatus): String = source.wireName()
}

@ReadingConverter
class StringToEventStatus : Converter<String, EventStatus> {
    override fun convert(source: String): EventStatus = EventStatus.fromWireName(source)
}