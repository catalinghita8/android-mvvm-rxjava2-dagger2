package com.inspiringteam.xchange.ui.quakes

import com.inspiringteam.xchange.data.models.Quake
import rx.functions.Action0

/**
 * A quake that should be displayed as an item in a list of quake.
 * Contains the quake and the action that should be triggered when taping on the item a
 */
class QuakeItem(
    val quake: Quake,
    /**
     * @return the action to be triggered on click events
     */
    val onClickAction: Action0
)