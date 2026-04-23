package sevynidd.diabetesapp.data.database

import kotlinx.coroutines.flow.Flow

class BolusTemplatesRepository(
    private val dao: BolusTemplateDao
) {
    val templatesFlow: Flow<List<BolusTemplateEntity>> = dao.observeAll()

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

    suspend fun deleteTemplate(template: BolusTemplateEntity) {
        dao.delete(template)
    }

    suspend fun markTemplateUsed(templateId: Long, usedAt: Long = System.currentTimeMillis()) {
        dao.markUsed(templateId, usedAt)
    }
}

private fun normalizeTemplateName(name: String): String {
    return name.trim().lowercase()
}

