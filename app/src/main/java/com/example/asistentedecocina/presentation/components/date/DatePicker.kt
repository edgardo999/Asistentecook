package com.example.asistentedecocina.presentation.components.date

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(selectedDate) }
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val lastDayOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth())
    val daysInMonth = lastDayOfMonth.dayOfMonth
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    Column(modifier = modifier) {
        // Encabezado del mes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { currentMonth = currentMonth.minusMonths(1) }
            ) {
                Icon(Icons.Default.ChevronLeft, contentDescription = "Mes anterior")
            }

            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = { currentMonth = currentMonth.plusMonths(1) }
            ) {
                Icon(Icons.Default.ChevronRight, contentDescription = "Siguiente mes")
            }
        }

        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Días del mes
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            var currentWeek = 0
            while (currentWeek * 7 < daysInMonth + firstDayOfWeek) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { dayOfWeek ->
                        val dayNumber = currentWeek * 7 + dayOfWeek - firstDayOfWeek + 1
                        if (dayNumber in 1..daysInMonth) {
                            val date = currentMonth.withDayOfMonth(dayNumber)
                            DayCell(
                                date = date,
                                isSelected = date == selectedDate,
                                onClick = { onDateSelected(date) }
                            )
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
                currentWeek++
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
} 