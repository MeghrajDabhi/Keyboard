package com.meghraj.futuristickeyboard.keyboard
import android.graphics.Color

data class ThemeColors(val bg:Int,val key:Int,val text:Int,val accent:Int,val glow:Int)
object ThemeEngine { fun colors(name:String):ThemeColors=when(name){
 "Light"->ThemeColors(Color.rgb(235,239,244),Color.WHITE,Color.rgb(20,25,32),Color.rgb(0,120,160),Color.rgb(0,170,220))
 "AMOLED"->ThemeColors(Color.BLACK,Color.rgb(18,18,20),Color.WHITE,Color.rgb(0,220,255),Color.rgb(0,220,255))
 "Glass"->ThemeColors(Color.rgb(12,18,28),Color.rgb(45,55,68),Color.WHITE,Color.rgb(130,190,255),Color.rgb(100,170,255))
 "Minimal"->ThemeColors(Color.rgb(28,30,34),Color.rgb(48,50,55),Color.WHITE,Color.rgb(190,195,205),Color.TRANSPARENT)
 "Custom"->ThemeColors(Color.rgb(9,12,18),Color.rgb(27,35,48),Color.WHITE,Color.rgb(120,255,190),Color.rgb(120,255,190))
 else->ThemeColors(Color.rgb(7,11,18),Color.rgb(18,29,43),Color.rgb(235,248,255),Color.rgb(70,220,255),Color.rgb(50,180,255))
} }
