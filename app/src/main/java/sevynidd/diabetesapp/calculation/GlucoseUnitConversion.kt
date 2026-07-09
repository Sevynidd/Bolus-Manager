package sevynidd.diabetesapp.calculation

private const val MG_DL_PER_MMOL_L = 18.0182

/** Converts [mmolL] to the equivalent mg/dl value, using the standard glucose molar-mass factor. */
fun mmolLToMgDl(mmolL: Double): Double = mmolL * MG_DL_PER_MMOL_L

/** Converts [mgDl] to the equivalent mmol/l value, using the standard glucose molar-mass factor. */
fun mgDlToMmolL(mgDl: Double): Double = mgDl / MG_DL_PER_MMOL_L
