package com.islam97.android.apps.movie.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GetReleaseYearTest {
    @Test
    fun `valid date format returns correct year`() {
        // Arrange
        val date = "2025-01-01"

        // Act
        val year = date.getReleaseYear()

        // Assert
        assertThat(year).isEqualTo(2025)
    }

    @Test
    fun `invalid date format returns null`() {
        // Arrange
        val date = "20"

        // Act
        val year = date.getReleaseYear()

        // Assert
        assertThat(year).isNull()
    }

    @Test
    fun `empty date returns null`() {
        // Arrange
        val date = ""

        // Act
        val year = date.getReleaseYear()

        // Assert
        assertThat(year).isNull()
    }
}