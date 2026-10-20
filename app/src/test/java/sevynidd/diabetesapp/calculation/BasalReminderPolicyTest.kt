package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode

class BasalReminderPolicyTest {

    @Test
    fun `normal case an enabled reminder is scheduled for pen therapy`() {
        assertTrue(shouldScheduleBasalReminder(reminderEnabled = true, therapyMode = TherapyMode.Pen))
    }

    @Test
    fun `pump therapy never schedules the reminder even when it is enabled`() {
        assertFalse(shouldScheduleBasalReminder(reminderEnabled = true, therapyMode = TherapyMode.Pump))
        assertFalse(isBasalReminderApplicable(TherapyMode.Pump))
    }

    @Test
    fun `a disabled reminder is never scheduled`() {
        assertFalse(shouldScheduleBasalReminder(reminderEnabled = false, therapyMode = TherapyMode.Pen))
    }
}
