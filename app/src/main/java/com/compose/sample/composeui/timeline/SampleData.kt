package com.compose.sample.composeui.timeline

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

enum class HiringStageStatus {
    FINISHED, CURRENT, UPCOMING
}

sealed class MessageSender(open val message: String) {
    data class Candidate(
        val initials: String,
        override val message: String,
    ) : MessageSender(message)

    data class HR(
        val initials: String,
        override val message: String,
    ) : MessageSender(message)

    data class System(
        override val message: String,
    ) : MessageSender(message)
}

@Stable
data class HiringStage(
    val date: LocalDate?,
    val initiator: MessageSender,
    val status: HiringStageStatus,
)

@RequiresApi(Build.VERSION_CODES.O)
val DATA = persistentListOf(
    HiringStage(
        date = LocalDate.now(),
        initiator = MessageSender.Candidate(
            "VS",
            "Hi! I will be glad to join DreamCompany team. I've sent you my CV."
        ),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now(),
        initiator = MessageSender.HR(
            "JD",
            "Hi! Let's have a short call to discuss your expectations and experience."
        ),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now(),
        initiator = MessageSender.HR(
            "JD",
            "Hi! Let's have a short call to discuss your expectations and experience."
        ),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now(),
        initiator = MessageSender.HR(
            "JD",
            "Hi! Let's have a short call to discuss your expectations and experience."
        ),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now(),
        initiator = MessageSender.HR(
            "JD",
            "Hi! Let's have a short call to discuss your expectations and experience."
        ),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now().plusDays(1),
        initiator = MessageSender.System("Screening call with Jane Doe."),
        status = HiringStageStatus.FINISHED
    ),
    HiringStage(
        date = LocalDate.now().plusDays(1),
        initiator = MessageSender.System("We are waiting for your test task. It should be completed at least one day before the technical interview."),
        status = HiringStageStatus.CURRENT
    ),
    HiringStage(
        date = LocalDate.now().plusDays(7),
        initiator = MessageSender.System("Technical interview."),
        status = HiringStageStatus.UPCOMING
    ),
    HiringStage(
        date = null,
        initiator = MessageSender.System("Bar raiser interview with the team."),
        status = HiringStageStatus.UPCOMING
    ),
    HiringStage(
        date = null,
        initiator = MessageSender.System("Offer proposal."),
        status = HiringStageStatus.UPCOMING
    )
)