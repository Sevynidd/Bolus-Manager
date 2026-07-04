package sevynidd.diabetesapp.data.database

import kotlinx.coroutines.flow.Flow

/** Manages saved [BolusTemplateEntity] rows, enforcing case/whitespace-insensitive unique names. */
class BolusTemplatesRepository(
    private val dao: BolusTemplateDao
) {
    /** Every saved template. */
    val templatesFlow: Flow<List<BolusTemplateEntity>> = dao.observeAll()

    /** Adds a new template; returns `false` without inserting if [name] is blank or already taken. */
    suspend fun addTemplate(name: String, emoji: String?, carbohydrates: Double): Boolean {
        val normalizedName = normalizeTemplateName(name)
        if (normalizedName.isBlank() || dao.countByNormalizedName(normalizedName) > 0) {
            return false
        }

        dao.insert(
            BolusTemplateEntity(
                name = name.trim(),
                nameNormalized = normalizedName,
                emoji = emoji?.trim().takeUnless { it.isNullOrBlank() },
                carbohydrates = carbohydrates
            )
        )

        return true
    }

    /** Overwrites [template]; returns `false` without updating if its new name collides with another template. */
    suspend fun updateTemplate(template: BolusTemplateEntity): Boolean {
        val normalizedName = normalizeTemplateName(template.name)
        if (normalizedName.isBlank() || dao.countByNormalizedNameExcludingId(normalizedName, template.id) > 0) {
            return false
        }

        dao.update(
            template.copy(
                name = template.name.trim(),
                nameNormalized = normalizedName,
                emoji = template.emoji?.trim().takeUnless { it.isNullOrBlank() }
            )
        )

        return true
    }

    /** Removes [template]. */
    suspend fun deleteTemplate(template: BolusTemplateEntity) {
        dao.delete(template)
    }

    /** Marks the template with [templateId] as last used at [usedAt] (epoch millis), for "recently used" sorting. */
    suspend fun markTemplateUsed(templateId: Long, usedAt: Long = System.currentTimeMillis()) {
        dao.markUsed(templateId, usedAt)
    }
}

private fun normalizeTemplateName(name: String): String {
    return name.trim().lowercase()
}

