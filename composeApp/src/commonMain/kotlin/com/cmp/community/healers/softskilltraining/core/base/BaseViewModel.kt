package com.cmp.community.healers.softskilltraining.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base ViewModel that wires up the three MVI contracts.
 *
 * Usage:
 *   class MyViewModel : BaseViewModel<MyState, MyEvent, MyEffect>(MyState()) {
 *       override fun handleEvent(event: MyEvent) { … }
 *   }
 *
 * In the UI:
 *   val state by vm.state.collectAsStateWithLifecycle()
 *   LaunchedEffect(vm) { vm.effect.collect { … } }
 *   vm.onEvent(MyEvent.ButtonClicked)
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, F : UiEffect>(
    initialState: S
) : ViewModel() {

    // ── State ─────────────────────────────────────────────────────────────────
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    // ── Effects (one-shot, Channel so they're never replayed) ─────────────────
    private val _effect = Channel<F>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    // ── Public API ────────────────────────────────────────────────────────────
    fun onEvent(event: E) = handleEvent(event)

    // ── For subclasses ────────────────────────────────────────────────────────
    protected abstract fun handleEvent(event: E)

    protected fun setState(reduce: S.() -> S) = _state.update(reduce)

    protected fun setEffect(effect: F) {
        viewModelScope.launch { _effect.send(effect) }
    }
}