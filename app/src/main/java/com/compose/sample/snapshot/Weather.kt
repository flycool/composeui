package com.compose.sample.snapshot

import androidx.compose.runtime.snapshots.StateObject
import androidx.compose.runtime.snapshots.StateRecord
import androidx.compose.runtime.snapshots.readable
import androidx.compose.runtime.snapshots.writable

// [[7 Implementing snapshot-aware data structures]]

class SimpleWeather(temp: Int, humidity: Float) {
    private val records = mutableListOf(Record(temp, humidity))
    private var currentRecordIndex = 0
    private val currentRecord
        get() = records[currentRecordIndex]

    var temp: Int
        get() = currentRecord.temp
        set(value) {
            currentRecord.temp = value
        }

    var humidity: Float
        get() = currentRecord.humidity
        set(value) {
            currentRecord.humidity = value
        }

    // Takes a new snapshot and returns an ID that can be
    // passed to restoreSnapshot.
    fun takeSnapshot(): Int {
        // The snapshot ID is the index of the current
        // record – any writes after this method returns
        // will use a new record.
        val id = currentRecordIndex

        // Push a copy of the current record onto the record
        // list and update the snapshot ID to the index of
        // that new record, so any subsequent writes will
        // use the new record.
        records += currentRecord.copy()
        currentRecordIndex = records.lastIndex

        return id
    }

    // All we have to do to change the record we're
    // currently looking at is set the index.
    fun restoreSnapshot(id: Int) {
        currentRecordIndex = id
    }

    private data class Record(
        var temp: Int,
        var humidity: Float
    )
}


class Weather(temp: Int, humidity: Float) : StateObject {
    // We keep a more specifically-typed pointer to the
    // head of the list to avoid having to cast it
    // everywhere.
    private var records = Record(temp, humidity)
    override val firstStateRecord: StateRecord
        get() = records

    override fun prependStateRecord(value: StateRecord) {
        // This cast is always safe since the runtime
        // guarantees it will only pass values in that
        // we gave it.
        records = value as Record
    }

    var temp: Int
        get() = records.readable(this).temp
        set(value) {
            records.writable(this) {
                temp = value
            }
        }

    var humidity: Float
        get() = records.readable(this).humidity
        set(value) {
            records.writable(this) {
                humidity = value
            }
        }

    private class Record(var temp: Int = 0, var humidity: Float = 0f) : StateRecord() {
        override fun create(): StateRecord = Record()

        override fun assign(value: StateRecord) {
            // This cast is also guaranteed safe by the
            // runtime.
            value as Record
            temp = value.temp
            humidity = value.humidity
        }
    }

}