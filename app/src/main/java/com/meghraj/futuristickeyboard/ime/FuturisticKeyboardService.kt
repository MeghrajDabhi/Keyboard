package com.meghraj.futuristickeyboard.ime

import android.content.*
import android.inputmethodservice.InputMethodService
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Toast
import android.media.ToneGenerator
import android.media.AudioManager
import com.meghraj.futuristickeyboard.keyboard.*
import com.meghraj.futuristickeyboard.settings.SettingsActivity

class FuturisticKeyboardService:InputMethodService(),KeyboardView.Listener {
 private lateinit var keyboard:KeyboardView
 private lateinit var clipboard:ClipboardManager
 private val history=LinkedHashSet<String>()
 private val pinned=LinkedHashSet<String>()
 private val tone=ToneGenerator(AudioManager.STREAM_SYSTEM,70)
 private val clipListener=ClipboardManager.OnPrimaryClipChangedListener{captureClipboard()}
 override fun onCreate(){super.onCreate();clipboard=getSystemService(CLIPBOARD_SERVICE) as ClipboardManager;loadHistory();clipboard.addPrimaryClipChangedListener(clipListener)}
 override fun onCreateInputView():View{keyboard=KeyboardView(this);keyboard.listener=this;keyboard.setPrefs(Prefs.load(this));keyboard.setClipboardItems((pinned.map{"★ "+it}+history.filter{!pinned.contains(it)}).toList());return keyboard}
 override fun onStartInput(info:EditorInfo?,restarting:Boolean){super.onStartInput(info,restarting);keyboard.takeIf{::keyboard.isInitialized}?.setPrefs(Prefs.load(this))}
 override fun onEvaluateFullscreenMode():Boolean=false
 override fun onAction(action:KeyAction,label:String){if(Prefs.load(this).sound) tone.startTone(ToneGenerator.TONE_PROP_BEEP,35)
  if(action.type==ActionType.CATEGORY&&action.category==Category.CLIPBOARD){keyboard.setClipboardItems((pinned.map{"★ "+it}+history.filter{!pinned.contains(it)}).toList());keyboard.setCategory(Category.CLIPBOARD);return};val ic=currentInputConnection ?: return
  when(action.type){
   ActionType.TEXT->ic.commitText(action.value,1)
   ActionType.SPACE->ic.commitText(" ",1)
   ActionType.BACKSPACE->deleteOne(ic)
   ActionType.DELETE_WORD->deleteWord(ic)
   ActionType.ENTER->sendEnter(ic)
   ActionType.SHIFT->keyboard.toggleShift()
   ActionType.CATEGORY->keyboard.setCategory(action.category?:Category.LETTERS)
   ActionType.ARROW->sendArrow(ic,label)
   ActionType.HOME->sendKey(ic,KeyEvent.KEYCODE_MOVE_HOME)
   ActionType.END->sendKey(ic,KeyEvent.KEYCODE_MOVE_END)
   ActionType.PAGE->sendKey(ic,if(label.equals("PgUp",true))KeyEvent.KEYCODE_PAGE_UP else KeyEvent.KEYCODE_PAGE_DOWN)
   ActionType.TAB->sendKey(ic,KeyEvent.KEYCODE_TAB)
   ActionType.ESC->sendKey(ic,KeyEvent.KEYCODE_ESCAPE)
   ActionType.INSERT->sendKey(ic,KeyEvent.KEYCODE_INSERT)
   ActionType.DELETE->sendKey(ic,KeyEvent.KEYCODE_FORWARD_DEL)
   ActionType.FUNCTION->sendFunction(ic,label)
   ActionType.CTRL->sendModifier(ic,KeyEvent.KEYCODE_CTRL_LEFT)
   ActionType.ALT->sendModifier(ic,KeyEvent.KEYCODE_ALT_LEFT)
   ActionType.SELECT->sendKey(ic,KeyEvent.KEYCODE_SHIFT_LEFT)
   ActionType.SELECT_ALL->performContext(ic,android.R.id.selectAll)
   ActionType.COPY->performContext(ic,android.R.id.copy)
   ActionType.CUT->performContext(ic,android.R.id.cut)
   ActionType.PASTE->performContext(ic,android.R.id.paste)
   ActionType.UNDO->sendCtrlCombo(ic,KeyEvent.KEYCODE_Z)
   ActionType.REDO->sendCtrlCombo(ic,KeyEvent.KEYCODE_Y)
   ActionType.NOOP->when(action.value){"SETTINGS"->startActivity(Intent(this,SettingsActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));"CLEAR_CLIPBOARD"->{history.clear();pinned.clear();saveHistory();savePinned();keyboard.setClipboardItems(emptyList())}
   action.value.startsWith("PIN:")->togglePinned(action.value.removePrefix("PIN:"))}
  }
 }
 private fun deleteOne(ic:InputConnection){val b=ic.getTextBeforeCursor(2,0)?:"";if(b.isNotEmpty())ic.deleteSurroundingText(1,0)}
 private fun deleteWord(ic:InputConnection){val b=ic.getTextBeforeCursor(64,0)?:"";val n=b.takeLastWhile{it.isWhitespace().not()}.length;if(n>0)ic.deleteSurroundingText(n,0) else deleteOne(ic)}
 private fun sendEnter(ic:InputConnection){val opts=currentInputEditorInfo?.imeOptions?:0;val action=opts and EditorInfo.IME_MASK_ACTION;if(action!=EditorInfo.IME_ACTION_NONE&&action!=EditorInfo.IME_ACTION_UNSPECIFIED){ic.performEditorAction(action)}else sendKey(ic,KeyEvent.KEYCODE_ENTER)}
 private fun sendArrow(ic:InputConnection,label:String){val k=when(label){"←"->KeyEvent.KEYCODE_DPAD_LEFT;"→"->KeyEvent.KEYCODE_DPAD_RIGHT;"↑"->KeyEvent.KEYCODE_DPAD_UP;else->KeyEvent.KEYCODE_DPAD_DOWN};sendKey(ic,k)}
 private fun sendKey(ic:InputConnection,k:Int){ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,k));ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP,k))}
 private fun sendFunction(ic:InputConnection,label:String){val n=label.removePrefix("F").toIntOrNull()?:return;val key=when(n){1->KeyEvent.KEYCODE_F1;2->KeyEvent.KEYCODE_F2;3->KeyEvent.KEYCODE_F3;4->KeyEvent.KEYCODE_F4;5->KeyEvent.KEYCODE_F5;6->KeyEvent.KEYCODE_F6;7->KeyEvent.KEYCODE_F7;8->KeyEvent.KEYCODE_F8;9->KeyEvent.KEYCODE_F9;10->KeyEvent.KEYCODE_F10;11->KeyEvent.KEYCODE_F11;12->KeyEvent.KEYCODE_F12;else->return};sendKey(ic,key)}
 private fun sendModifier(ic:InputConnection,k:Int){sendKey(ic,k)}
 private fun sendCtrlCombo(ic:InputConnection,k:Int){ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_CTRL_LEFT));sendKey(ic,k);ic.sendKeyEvent(KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_CTRL_LEFT))}
 private fun performContext(ic:InputConnection,id:Int){if(!ic.performContextMenuAction(id)){Toast.makeText(this,"Editor does not support this action",Toast.LENGTH_SHORT).show()}}
 private fun captureClipboard(){val clip=clipboard.primaryClip?:return;if(clip.itemCount==0)return;val s=clip.getItemAt(0).coerceToText(this).toString();if(s.isBlank())return;history.remove(s);history.add(s);while(history.size>20)history.remove(history.first());saveHistory();keyboard.takeIf{::keyboard.isInitialized}?.setClipboardItems((pinned.map{"★ "+it}+history.filter{!pinned.contains(it)}).toList())}
 private fun loadHistory(){getSharedPreferences("clipboard",MODE_PRIVATE).getStringSet("items",emptySet())?.let{history.addAll(it)};getSharedPreferences("clipboard",MODE_PRIVATE).getStringSet("pinned",emptySet())?.let{pinned.addAll(it)}}
 private fun savePinned(){getSharedPreferences("clipboard",MODE_PRIVATE).edit().putStringSet("pinned",pinned.toSet()).apply()}
 private fun togglePinned(s:String){if(s.isBlank())return;if(s.startsWith("★ ")){pinned.remove(s.removePrefix("★ "))}else if(pinned.contains(s))pinned.remove(s) else pinned.add(s);savePinned();keyboard.setClipboardItems((pinned.map{"★ "+it}+history.filter{!pinned.contains(it)}).toList())}
 private fun saveHistory(){getSharedPreferences("clipboard",MODE_PRIVATE).edit().putStringSet("items",history.toSet()).apply()}
 override fun onDestroy(){tone.release();if(::clipboard.isInitialized)clipboard.removePrimaryClipChangedListener(clipListener);super.onDestroy()}
}
