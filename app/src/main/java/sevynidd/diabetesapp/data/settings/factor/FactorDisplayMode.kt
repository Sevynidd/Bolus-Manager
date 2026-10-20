package sevynidd.diabetesapp.data.settings.factor

/**
 * How factors are shown and entered. Storage is always [UnitsPerBreadUnit]; [GramsPerUnit] is
 * the insulin-pump notation ("1 unit per x grams of carbohydrates") derived from it.
 */
enum class FactorDisplayMode {
    UnitsPerBreadUnit, GramsPerUnit
}
