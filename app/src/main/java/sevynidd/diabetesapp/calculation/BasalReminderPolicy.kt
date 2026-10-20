package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.settings.therapy.TherapyMode

/**
 * Whether the daily basal reminder applies to [therapyMode] at all: it reminds of the pen's single
 * daily basal injection, which an insulin pump delivers on its own, so it only exists for
 * [TherapyMode.Pen].
 */
fun isBasalReminderApplicable(therapyMode: TherapyMode): Boolean = therapyMode == TherapyMode.Pen

/**
 * Whether the daily basal-reminder alarm should be armed: the user turned it on
 * ([reminderEnabled]) and it applies to the current [therapyMode]. The on/off choice itself is
 * kept while in pump mode, so switching back to pen restores it.
 */
fun shouldScheduleBasalReminder(reminderEnabled: Boolean, therapyMode: TherapyMode): Boolean =
    reminderEnabled && isBasalReminderApplicable(therapyMode)
