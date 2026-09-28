package com.meghraj.futuristickeyboard.keyboard

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlin.math.max

class KeyboardView(context:Context): View(context) {
 interface Listener { fun onAction(action:KeyAction,label:String) }
 var listener:Listener?=null
 var prefs=Prefs.load(context); private var category=Category.LETTERS; private var clipboardItems=emptyList<String>(); private var shifted=false
 private val layouts=Layouts.all(); private val paint=Paint(Paint.ANTI_ALIAS_FLAG); private val bounds=mutableListOf<Pair<RectF,KeyboardKey>>(); private var pressed=-1
 private val handler=Handler(Looper.getMainLooper()); private var repeat=false; private var longHandled=false
 private var popupLabel:String?=null; private var popupX=0f; private var popupY=0f
 fun setPrefs(p:KeyboardPrefs){prefs=p;invalidate()}
 fun setClipboardItems(items:List<String>){clipboardItems=items.take(8); invalidate()}
 fun setCategory(c:Category){category=c; pressed=-1; popupLabel=null; invalidate()}
 fun toggleShift(){shifted=!shifted;invalidate()}
 override fun onMeasure(w:Int,h:Int){val desired=(resources.displayMetrics.heightPixels*prefs.height/1000f).toInt();setMeasuredDimension(MeasureSpec.getSize(w),desired.coerceIn(dp(220),dp(520)))}
 override fun onDraw(c:Canvas){super.onDraw(c);val tc=ThemeEngine.colors(prefs.theme);c.drawColor(tc.bg);bounds.clear();val rows=rowsForCurrentCategory();val top=if(prefs.toolbar) dp(36) else 0f;val rowH=(height-top-4*dp(prefs.spacing))/rows.size
  rows.forEachIndexed{ri,row->var x=dp(prefs.spacing);val gap=dp(prefs.spacing);val total=row.keys.sumOf{it.weight.toDouble()}.toFloat();val unit=(width-gap*(row.keys.size+1))/total
   row.keys.forEach{key->val w=unit*key.weight;val r=RectF(x,top+ri*(rowH+gap),x+w,top+ri*(rowH+gap)+rowH);bounds.add(r to key);drawKey(c,r,key,tc);x+=w+gap}
  };if(prefs.toolbar)drawToolbar(c,tc);popupLabel?.let{drawPopup(c,it,tc)}
 }
 private fun rowsForCurrentCategory():List<KeyboardRow>{if(category!=Category.CLIPBOARD)return layouts[category]!!.rows;val rows=clipboardItems.map{KeyboardRow(listOf(KeyboardKey(it.take(18),KeyAction(ActionType.TEXT,if(it.startsWith("★ "))it.removePrefix("★ ") else it),1f)))}.toMutableList();rows.add(KeyboardRow(listOf(KeyboardKey("Clear",KeyAction(ActionType.NOOP,"CLEAR_CLIPBOARD"),1.3f),KeyboardKey("ABC",KeyAction(ActionType.CATEGORY,category=Category.LETTERS),1.3f),KeyboardKey("⌫",KeyAction(ActionType.BACKSPACE),1.3f))));return rows.ifEmpty{listOf(KeyboardRow(listOf(KeyboardKey("No clipboard items",KeyAction(ActionType.NOOP),1f))),KeyboardRow(listOf(KeyboardKey("ABC",KeyAction(ActionType.CATEGORY,category=Category.LETTERS),1.5f))))}}
 private fun drawToolbar(c:Canvas,tc:ThemeColors){paint.style=Paint.Style.FILL;paint.color=adjust(tc.key,0.75f);c.drawRect(0f,0f,width.toFloat(),dp(32),paint);val items=listOf("☺" to Category.EMOJI,"▣" to Category.CLIPBOARD,"⌘" to Category.EDITING,"↔" to Category.NAVIGATION,"FN" to Category.FN,"⚙" to null);paint.textSize=dp(16);paint.textAlign=Paint.Align.CENTER;items.forEachIndexed{i,(s,cat)->val x=(i+.5f)*width/items.size;paint.color=tc.text;c.drawText(s,x,dp(21),paint);if(s=="⚙"){ } }
 }
 private fun drawKey(c:Canvas,r:RectF,key:KeyboardKey,tc:ThemeColors){val isPressed=bounds.getOrNull(pressed)?.first===r;paint.style=Paint.Style.FILL;paint.color=if(isPressed)tc.accent else tc.key;c.drawRoundRect(r,dp(prefs.radius),dp(prefs.radius),paint);if(prefs.glow&&tc.glow!=Color.TRANSPARENT&&isPressed){paint.color=tc.glow;paint.style=Paint.Style.STROKE;paint.strokeWidth=dp(2);c.drawRoundRect(r,dp(prefs.radius),dp(prefs.radius),paint);paint.style=Paint.Style.FILL}
  paint.color=if(isPressed)Color.BLACK else tc.text;paint.textSize=dp(if(key.label.length>4)12 else 18);paint.textAlign=Paint.Align.CENTER;val label=if(category==Category.LETTERS&&shifted)key.label.uppercase() else key.label;c.drawText(label,r.centerX(),r.centerY()-(paint.ascent()+paint.descent())/2,paint)
 }
 private fun drawPopup(c:Canvas,s:String,tc:ThemeColors){val w=dp(58);val h=dp(64);val x=(popupX-w/2).coerceIn(2f,width-w-2);val y=max(dp(2),popupY-h-dp(4));paint.color=tc.accent;paint.style=Paint.Style.FILL;c.drawRoundRect(x,y,x+w,y+h,dp(14),dp(14),paint);paint.color=Color.BLACK;paint.textSize=dp(30);paint.textAlign=Paint.Align.CENTER;c.drawText(s,x+w/2,y+h/2-(paint.ascent()+paint.descent())/2,paint)}
 override fun onTouchEvent(e:MotionEvent):Boolean{when(e.action){MotionEvent.ACTION_DOWN->{pressed=find(e.x,e.y);longHandled=false;if(pressed>=0){val k=bounds[pressed].second;popupLabel=if(prefs.popup&&k.label.length<=2)k.label else null;popupX=e.x;popupY=e.y;invalidate(); if(k.action.type==ActionType.BACKSPACE){repeat=true;handler.postDelayed({repeatDelete()},350)} else if(category==Category.CLIPBOARD&&k.action.type==ActionType.TEXT){handler.postDelayed({if(pressed>=0&&!longHandled){longHandled=true;listener?.onAction(KeyAction(ActionType.NOOP,"PIN:"+k.action.value),k.label);invalidate()}},500)};if(prefs.vibration)performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED)};return true}
 MotionEvent.ACTION_UP->{if(e.y<dp(32)&&prefs.toolbar){val i=(e.x/(width.toFloat()/6)).toInt().coerceIn(0,5);val action=when(i){0->KeyAction(ActionType.CATEGORY,category=Category.EMOJI);1->KeyAction(ActionType.CATEGORY,category=Category.CLIPBOARD);2->KeyAction(ActionType.CATEGORY,category=Category.EDITING);3->KeyAction(ActionType.CATEGORY,category=Category.NAVIGATION);4->KeyAction(ActionType.CATEGORY,category=Category.FN);else->KeyAction(ActionType.NOOP,"SETTINGS")};pressed=-1;popupLabel=null;invalidate();listener?.onAction(action,"toolbar");return true};if(repeat){repeat=false;handler.removeCallbacksAndMessages(null)};val p=pressed;pressed=-1;popupLabel=null;invalidate();if(!longHandled&&p>=0&&p<bounds.size)activate(bounds[p].second);return true}
 MotionEvent.ACTION_CANCEL->{repeat=false;handler.removeCallbacksAndMessages(null);pressed=-1;popupLabel=null;invalidate();return true}}
 return true }
 private fun repeatDelete(){if(!repeat)return;listener?.onAction(KeyAction(ActionType.BACKSPACE),"⌫");handler.postDelayed({repeatDelete()},70)}
 private fun activate(k:KeyboardKey){var action=k.action;if(category==Category.LETTERS&&action.type==ActionType.TEXT){val v=if(shifted)k.label.uppercase() else k.label.lowercase();action=KeyAction(ActionType.TEXT,v)};if(action.type==ActionType.SHIFT){shifted=!shifted};listener?.onAction(action,k.label)}
 private fun find(x:Float,y:Float)=bounds.indexOfFirst{it.first.contains(x,y)}
 private fun dp(v:Int)=v*resources.displayMetrics.density
 private fun adjust(c:Int,f:Float):Int=Color.rgb((Color.red(c)*f).toInt().coerceAtMost(255),(Color.green(c)*f).toInt().coerceAtMost(255),(Color.blue(c)*f).toInt().coerceAtMost(255))
 override fun onInitializeAccessibilityNodeInfo(info:AccessibilityNodeInfo){super.onInitializeAccessibilityNodeInfo(info);info.className="android.inputmethodservice.Keyboard";info.contentDescription="Futuristic keyboard ${category.name.lowercase()} layout"}
}
