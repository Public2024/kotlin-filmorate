package com.example.doc.util

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.randomizers.range.BigDecimalRangeRandomizer
import org.jeasy.random.randomizers.range.DoubleRangeRandomizer
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer
import org.jeasy.random.randomizers.range.LongRangeRandomizer
import org.jeasy.random.randomizers.time.InstantRandomizer
import org.jeasy.random.randomizers.time.LocalDateTimeRandomizer
import org.jeasy.random.randomizers.time.LocalTimeRandomizer
import org.jeasy.random.randomizers.time.OffsetDateTimeRandomizer
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.Charset
import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.streams.asSequence


object EasyRandomUtil {

    private val default: EasyRandomParameters = defaultParams()
    val easyRandom = EasyRandom(default)

    private fun defaultParams() =
        EasyRandomParameters()
            .seed(123L)
            .objectPoolSize(100)
            .randomizationDepth(10)
            .charset(Charset.forName("UTF-8"))
            .dateRange(LocalDate.now(ZoneOffset.UTC), LocalDate.now(ZoneOffset.UTC).plusDays(2))
            .stringLengthRange(5, 15)
            .collectionSizeRange(1, 3)
            .scanClasspathForConcreteTypes(true)
            .overrideDefaultInitialization(false)
            .ignoreRandomizationErrors(true)
            .randomize(
                LocalDateTime::class.java,
                object : LocalDateTimeRandomizer() {
                    override fun getRandomValue(): LocalDateTime {
                        return super.getRandomValue().truncatedTo(ChronoUnit.SECONDS)
                    }
                },
            )
            .randomize(
                Instant::class.java,
                object : InstantRandomizer() {
                    override fun getRandomValue(): Instant {
                        return super.getRandomValue().truncatedTo(ChronoUnit.SECONDS)
                    }
                },
            )
            .randomize(
                OffsetDateTime::class.java,
                object : OffsetDateTimeRandomizer() {
                    override fun getRandomValue(): OffsetDateTime {
                        return super.getRandomValue().truncatedTo(ChronoUnit.SECONDS)
                            .withOffsetSameLocal(ZoneOffset.UTC)
                    }
                },
            )
            .randomize(
                LocalTime::class.java,
                object : LocalTimeRandomizer() {
                    override fun getRandomValue(): LocalTime {
                        return super.getRandomValue().truncatedTo(ChronoUnit.SECONDS)
                    }
                },
            )
            .randomize(
                Instant::class.java,
                object : InstantRandomizer() {
                    override fun getRandomValue(): Instant {
                        return super.getRandomValue().truncatedTo(ChronoUnit.SECONDS)
                    }
                },
            )
            .randomize(
                BigDecimal::class.java,
                object : BigDecimalRangeRandomizer(0.0, 100.0) {
                    override fun getRandomValue(): BigDecimal {
                        return super.getRandomValue().setScale(2, RoundingMode.HALF_UP)
                    }
                },
            )
            .randomize(
                Int::class.javaObjectType,
                IntegerRangeRandomizer(0, 1_000_000),
            )
            .randomize(
                Long::class.javaObjectType,
                LongRangeRandomizer(0, 1_000_000),
            )
            .randomize(
                Double::class.javaObjectType,
                object : DoubleRangeRandomizer(0.0, 100.0) {
                    override fun getRandomValue(): Double {
                        return super.getRandomValue().toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
                    }
                },
            )

}