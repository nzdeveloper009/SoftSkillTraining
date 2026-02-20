package com.cmp.community.healers.softskilltraining.core.base

/**
 * Marker interfaces for the three MVI contracts.
 *
 *  UiState  – immutable snapshot of everything the screen needs to render
 *  UiEvent  – user intents / actions dispatched from the UI layer
 *  UiEffect – one-shot side-effects (navigation, snackbar, etc.)
 */
interface UiState
interface UiEvent
interface UiEffect
