package br.edu.ifsp.dmo.ambientum.data.model

import br.edu.ifsp.dmo.ambientum.R

enum class Quality(val label: String, val colorResId: Int) {
    EXCELLENT("Excellent", R.color.excellent),
    GOOD("Good", R.color.good),
    MODERATE("Moderate", R.color.moderate),
    POOR("Poor", R.color.poor),
    VERY_POOR("Very poor", R.color.very_poor),
}