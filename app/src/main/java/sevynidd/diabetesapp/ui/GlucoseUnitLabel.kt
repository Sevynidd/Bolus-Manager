package sevynidd.diabetesapp.ui

import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

/** The unit symbol shown next to blood-sugar values; identical in every UI language. */
fun glucoseUnitLabel(unit: GlucoseUnit): String = when (unit) {
    GlucoseUnit.MgDl -> "mg/dl"
    GlucoseUnit.MmolL -> "mmol/l"
}
