package com.meghraj.futuristickeyboard.keyboard

enum class Category { LETTERS, NUMBERS, SYMBOLS, FN, EMOJI, CLIPBOARD, NAVIGATION, EDITING, CUSTOM }
enum class ActionType { TEXT, BACKSPACE, ENTER, SHIFT, SPACE, CATEGORY, DELETE_WORD, ARROW, HOME, END, PAGE, COPY, CUT, PASTE, SELECT, SELECT_ALL, UNDO, REDO, TAB, ESC, CTRL, ALT, INSERT, DELETE, FUNCTION, NOOP }
data class KeyAction(val type: ActionType, val value: String = "", val category: Category? = null, val direction: Int = 0)
data class KeyboardKey(val label: String, val action: KeyAction, val weight: Float = 1f, val alternate: List<String> = emptyList())
data class KeyboardRow(val keys: List<KeyboardKey>)
data class KeyboardCategory(val id: Category, val rows: List<KeyboardRow>)
