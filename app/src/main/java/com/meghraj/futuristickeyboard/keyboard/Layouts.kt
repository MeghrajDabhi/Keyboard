package com.meghraj.futuristickeyboard.keyboard

object Layouts {
 private fun t(s:String, w:Float=1f, alt:List<String> = emptyList())=KeyboardKey(s,KeyAction(ActionType.TEXT,s),w,alt)
 private fun a(s:String,type:ActionType,w:Float=1f)=KeyboardKey(s,KeyAction(type),w)
 private fun cat(s:String,c:Category,w:Float=1f)=KeyboardKey(s,KeyAction(ActionType.CATEGORY,category=c),w)
 fun all(): Map<Category,KeyboardCategory> = mapOf(
  Category.LETTERS to KeyboardCategory(Category.LETTERS,listOf(
   KeyboardRow("Q W E R T Y U I O P".split(" ").map{t(it,1f,listOf(it.lowercase()))}),
   KeyboardRow("A S D F G H J K L".split(" ").map{t(it)}),
   KeyboardRow(listOf(a("⇧",ActionType.SHIFT,1.4f))+"Z X C V B N M".split(" ").map{t(it)}+listOf(a("⌫",ActionType.BACKSPACE,1.4f))),
   KeyboardRow(listOf(cat("123",Category.NUMBERS,1.2f),cat("SYM",Category.SYMBOLS,1.2f),a("␠",ActionType.SPACE,4.4f),cat("☺",Category.EMOJI,1.1f),a("↵",ActionType.ENTER,1.3f)))
  )),
  Category.NUMBERS to KeyboardCategory(Category.NUMBERS,listOf(
   KeyboardRow("1 2 3 4 5 6 7 8 9 0".split(" ").map{t(it)}),
   KeyboardRow("- + * / = % ( ) # @".split(" ").map{t(it)}),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.3f)) + ". , : ; ! ?".split(" ").map{t(it)}+listOf(a("⌫",ActionType.BACKSPACE,1.3f))),
   KeyboardRow(listOf(cat("SYM",Category.SYMBOLS,1.2f),cat("FN",Category.FN,1.2f),a("␠",ActionType.SPACE,4.5f),cat("☺",Category.EMOJI,1.1f),a("↵",ActionType.ENTER,1.3f)))
  )),
  Category.SYMBOLS to KeyboardCategory(Category.SYMBOLS,listOf(
   KeyboardRow("! @ # $ % ^ & * ( )".split(" ").map{t(it)}),
   KeyboardRow("= / \\ | < > [ ] { }".split(" ").map{t(it)}),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.3f)) + "~ ` _ - + ; : ' \"".split(" ").map{t(it)}+listOf(a("⌫",ActionType.BACKSPACE,1.3f))),
   KeyboardRow(listOf(cat("123",Category.NUMBERS,1.2f),cat("FN",Category.FN,1.2f),a("␠",ActionType.SPACE,4.5f),cat("☺",Category.EMOJI,1.1f),a("↵",ActionType.ENTER,1.3f)))
  )),
  Category.FN to KeyboardCategory(Category.FN,listOf(
   KeyboardRow((1..6).map{a("F$it",ActionType.FUNCTION)}),KeyboardRow((7..12).map{a("F$it",ActionType.FUNCTION)}),
   KeyboardRow(listOf(a("Esc",ActionType.ESC),a("Tab",ActionType.TAB),a("Ctrl",ActionType.CTRL),a("Alt",ActionType.ALT),a("Ins",ActionType.INSERT),a("Del",ActionType.DELETE))),
   KeyboardRow(listOf(a("Home",ActionType.HOME),a("End",ActionType.END),a("PgUp",ActionType.PAGE),a("PgDn",ActionType.PAGE),a("←",ActionType.ARROW),a("→",ActionType.ARROW),a("↵",ActionType.ENTER))),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.2f),a("↑",ActionType.ARROW),a("↓",ActionType.ARROW),cat("NAV",Category.NAVIGATION,1.2f),cat("EDIT",Category.EDITING,1.2f),a("⌫",ActionType.BACKSPACE,1.2f)))
  )),
  Category.NAVIGATION to KeyboardCategory(Category.NAVIGATION,listOf(
   KeyboardRow(listOf(a("←",ActionType.ARROW),a("→",ActionType.ARROW),a("↑",ActionType.ARROW),a("↓",ActionType.ARROW))),
   KeyboardRow(listOf(a("Home",ActionType.HOME),a("End",ActionType.END),a("PgUp",ActionType.PAGE),a("PgDn",ActionType.PAGE))),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.5f),a("Select",ActionType.SELECT,1.5f),a("↵",ActionType.ENTER,1.3f)))
  )),
  Category.EDITING to KeyboardCategory(Category.EDITING,listOf(
   KeyboardRow(listOf(a("Select",ActionType.SELECT),a("All",ActionType.SELECT_ALL),a("Copy",ActionType.COPY),a("Cut",ActionType.CUT))),
   KeyboardRow(listOf(a("Paste",ActionType.PASTE),a("Undo",ActionType.UNDO),a("Redo",ActionType.REDO),a("⌫",ActionType.BACKSPACE))),
   KeyboardRow(listOf(cat("NAV",Category.NAVIGATION,1.4f),cat("ABC",Category.LETTERS,1.4f),a("␠",ActionType.SPACE,3f),a("↵",ActionType.ENTER,1.3f)))
  )),
  Category.CUSTOM to KeyboardCategory(Category.CUSTOM,listOf(
   KeyboardRow(listOf(t("Hello"),t("Thanks"),t("Regards"))),KeyboardRow(listOf(t("→"),t("•"),t("✓"),t("©"))),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.4f),a("␠",ActionType.SPACE,4f),a("⌫",ActionType.BACKSPACE,1.4f)))
  )),
  Category.CLIPBOARD to KeyboardCategory(Category.CLIPBOARD,listOf(
   KeyboardRow(listOf(t("Clipboard"),t("History"),t("Local"))),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.5f),a("Clear",ActionType.NOOP,1.5f),a("⌫",ActionType.BACKSPACE,1.3f)))
  )),
  Category.EMOJI to KeyboardCategory(Category.EMOJI,listOf(
   KeyboardRow(EmojiData.recent.map{t(it,1f)}),
   KeyboardRow(listOf(cat("ABC",Category.LETTERS,1.5f),cat("123",Category.NUMBERS,1.2f),cat("☺",Category.EMOJI,1.2f),a("⌫",ActionType.BACKSPACE,1.4f)))
  ))
 )
}
object EmojiData { val recent=listOf("😀","😂","🤣","😊","😍","😎","🤔","😴","😭","😡","👍","👎","👏","🙏","🔥","❤️","✨","🎯","🚀","💡","✅","❌","⚡","💀","🤖","🧠","📈","💰") }
