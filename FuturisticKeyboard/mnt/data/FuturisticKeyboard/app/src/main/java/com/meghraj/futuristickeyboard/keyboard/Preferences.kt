package com.meghraj.futuristickeyboard.keyboard

import android.content.Context

data class KeyboardPrefs(val theme:String="Cyber",val animation:String="Smooth",val height:Int=100,val spacing:Int=3,val radius:Int=12,val transparency:Int=96,val vibration:Boolean=true,val sound:Boolean=false,val glow:Boolean=true,val popup:Boolean=true,val toolbar:Boolean=true,val suggestions:Boolean=false,val numberRow:Boolean=false)
object Prefs {
 private const val FILE="keyboard_prefs"
 fun load(c:Context)=with(c.getSharedPreferences(FILE,Context.MODE_PRIVATE)){KeyboardPrefs(getString("theme","Cyber")!!,getString("animation","Smooth")!!,getInt("height",100),getInt("spacing",3),getInt("radius",12),getInt("transparency",96),getBoolean("vibration",true),getBoolean("sound",false),getBoolean("glow",true),getBoolean("popup",true),getBoolean("toolbar",true),getBoolean("suggestions",false),getBoolean("numberRow",false))}
 fun save(c:Context,p:KeyboardPrefs){c.getSharedPreferences(FILE,Context.MODE_PRIVATE).edit().putString("theme",p.theme).putString("animation",p.animation).putInt("height",p.height).putInt("spacing",p.spacing).putInt("radius",p.radius).putInt("transparency",p.transparency).putBoolean("vibration",p.vibration).putBoolean("sound",p.sound).putBoolean("glow",p.glow).putBoolean("popup",p.popup).putBoolean("toolbar",p.toolbar).putBoolean("suggestions",p.suggestions).putBoolean("numberRow",p.numberRow).apply()}
}
