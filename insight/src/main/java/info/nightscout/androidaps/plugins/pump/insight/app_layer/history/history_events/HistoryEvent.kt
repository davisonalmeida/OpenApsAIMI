package info.nightscout.androidaps.plugins.pump.insight.app_layer.history.history_events

import info.nightscout.androidaps.plugins.pump.insight.descriptors.HistoryEvents.Companion.fromId
import info.nightscout.androidaps.plugins.pump.insight.utils.BOCUtil
import info.nightscout.androidaps.plugins.pump.insight.utils.ByteBuf
import info.nightscout.shared.logging.StacktraceLoggerWrapper.Companion.getLogger
import org.slf4j.Logger

open class HistoryEvent : Comparable<HistoryEvent> {

    var eventYear = 0
        private set
    var eventMonth = 0
        private set
    var eventDay = 0
        private set
    var eventHour = 0
        private set
    var eventMinute = 0
        private set
    var eventSecond = 0
        private set
    var eventPosition: Long = 0
        private set

    fun parseHeader(byteBuf: ByteBuf) {
        byteBuf.let {
            eventYear = BOCUtil.parseBOC(it.readByte()) * 100 + BOCUtil.parseBOC(it.readByte())
            eventMonth = BOCUtil.parseBOC(it.readByte())
            eventDay = BOCUtil.parseBOC(it.readByte())
            it.shift(1)
            eventHour = BOCUtil.parseBOC(it.readByte())
            eventMinute = BOCUtil.parseBOC(it.readByte())
            eventSecond = BOCUtil.parseBOC(it.readByte())
            eventPosition = it.readUInt32LE()
        }
    }

    open fun parse(byteBuf: ByteBuf) = Unit

    override fun compareTo(other: HistoryEvent): Int = (eventPosition - other.eventPosition).toInt()

    companion object {
        fun deserialize(byteBuf: ByteBuf): HistoryEvent = fromId(byteBuf.readUInt16LE()).apply {
                parseHeader(byteBuf)
                parse(byteBuf)
            }
    }
}