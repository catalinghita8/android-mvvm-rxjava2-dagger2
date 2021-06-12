package com.inspiringteam.xchange.ui.quakes

class QuakesUiModel(
    val isQuakesListVisible: Boolean,
    val quakes: List<QuakeItem>,
    val isNoQuakesViewVisible: Boolean,
    val noQuakesModel: NoQuakesModel?
)