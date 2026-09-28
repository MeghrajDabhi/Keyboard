package com.meghraj.futuristickeyboard.settings

import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.meghraj.futuristickeyboard.BuildConfig
import com.meghraj.futuristickeyboard.keyboard.*

class SettingsActivity:android.app.Activity(){
 private lateinit var p:KeyboardPrefs
 private lateinit var root:LinearLayout
 override fun onCreate(b:Bundle?){super.onCreate(b);p=Prefs.load(this);root=LinearLayout(this);root.orientation=LinearLayout.VERTICAL;root.setPadding(dp(18),dp(18),dp(18),dp(24));setContentView(ScrollView(this).apply{addView(root)});build()}
 private fun build(){root.removeAllViews();title("FUTURISTIC KEYBOARD");text("Real Android IME • offline-first • no typed-text analytics");button("Enable keyboard"){startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))};button("Switch keyboard"){(getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showInputMethodPicker()}
  section("Appearance");choice("Theme",listOf("Dark","Light","AMOLED","Cyber","Glass","Minimal","Custom"),p.theme){p=p.copy(theme=it);save()};seek("Keyboard height",70,140,p.height){p=p.copy(height=it);save()};seek("Key spacing",0,10,p.spacing){p=p.copy(spacing=it);save()};seek("Corner radius",4,24,p.radius){p=p.copy(radius=it);save()};seek("Transparency",20,100,p.transparency){p=p.copy(transparency=it);save()}
  section("Animation");choice("Animation level",listOf("Minimal","Smooth","Cinematic"),p.animation){p=p.copy(animation=it);save()};toggle("Key effects / glow",p.glow){p=p.copy(glow=it);save()};toggle("Character popup",p.popup){p=p.copy(popup=it);save()};toggle("Transitions",p.animation!="Minimal"){p=p.copy(animation=if(it)"Smooth" else "Minimal");save()}
  section("Layout");toggle("Toolbar",p.toolbar){p=p.copy(toolbar=it);save()};toggle("Number row",p.numberRow){p=p.copy(numberRow=it);save()};text("Categories: Letters, Numbers, Symbols, FN, Emoji, Clipboard, Navigation, Editing, Custom.")
  section("Typing");toggle("Suggestions (UI only; no text is uploaded)",p.suggestions){p=p.copy(suggestions=it);save()};toggle("Haptic feedback",p.vibration){p=p.copy(vibration=it);save()};toggle("Key sounds",p.sound){p=p.copy(sound=it);save()};text("Auto-capitalization and editor action behavior are delegated to Android EditorInfo/InputConnection where available.")
  section("Privacy");text("Typed text is processed only by the active Android editor connection. This app declares VIBRATE only; it has no network permission, analytics endpoint, or hidden clipboard upload. Clipboard history is stored locally when the IME is active.")
  section("About");text("Version ${BuildConfig.VERSION_NAME}\nOpen-source native Kotlin project. See README.md for architecture, build, testing, and Android limitations.")
 }
 private fun save(){Prefs.save(this,p)}
 private fun title(s:String){val v=TextView(this);v.text=s;v.textSize=24f;v.setTextColor(Color.WHITE);v.setPadding(0,0,0,dp(8));root.addView(v,lp())}
 private fun section(s:String){val v=TextView(this);v.text=s;v.textSize=18f;v.setTextColor(Color.rgb(70,220,255));v.setPadding(0,dp(20),0,dp(8));root.addView(v,lp())}
 private fun text(s:String){val v=TextView(this);v.text=s;v.textSize=14f;v.setTextColor(Color.LTGRAY);v.setPadding(0,dp(4),0,dp(8));root.addView(v,lp())}
 private fun button(s:String,fn:()->Unit){val b=Button(this);b.text=s;b.setOnClickListener{fn()};root.addView(b,lp())}
 private fun toggle(s:String,checked:Boolean,fn:(Boolean)->Unit){val sw=Switch(this);sw.text=s;sw.isChecked=checked;sw.setOnCheckedChangeListener{_,v->fn(v)};root.addView(sw,lp())}
 private fun choice(label:String,items:List<String>,selected:String,fn:(String)->Unit){val tv=TextView(this);tv.text=label;tv.textSize=14f;tv.setTextColor(Color.WHITE);root.addView(tv,lp());val sp=Spinner(this);sp.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,items);sp.setSelection(items.indexOf(selected).coerceAtLeast(0));sp.onItemSelectedListener=object:android.widget.AdapterView.OnItemSelectedListener{override fun onNothingSelected(p0:android.widget.AdapterView<*>?){};override fun onItemSelected(p0:android.widget.AdapterView<*>?,v:android.view.View?,pos:Int,id:Long){fn(items[pos])}};root.addView(sp,lp())}
 private fun seek(label:String,min:Int,max:Int,value:Int,fn:(Int)->Unit){val t=TextView(this);t.text="$label: $value";t.setTextColor(Color.LTGRAY);root.addView(t,lp());val bar=SeekBar(this);bar.max=max-min;bar.progress=(value-min).coerceIn(0,max-min);bar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{override fun onProgressChanged(s:SeekBar?,x:Int,from:Boolean){val v=x+min;t.text="$label: $v";if(from)fn(v)};override fun onStartTrackingTouch(s:SeekBar?){ };override fun onStopTrackingTouch(s:SeekBar?){ }});root.addView(bar,lp())}
 private fun lp()=LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
 private fun dp(v:Int)= (v*resources.displayMetrics.density).toInt()
}
