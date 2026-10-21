package com.khairul.wormzone
import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.*
import android.widget.*
import com.google.android.gms.ads.*

class MainActivity: Activity(){
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState)
 window.statusBarColor=Color.rgb(35,90,45);window.navigationBarColor=Color.rgb(35,90,45)
 val root=LinearLayout(this);root.orientation=1;root.setBackgroundColor(Color.rgb(35,90,45))
 val title=TextView(this);title.text="🐛 KHAIRUL WORM ZONE";title.textSize=22f;title.gravity=Gravity.CENTER;title.setTextColor(Color.WHITE);root.addView(title,LinearLayout.LayoutParams(-1,52))
 val score=TextView(this);score.text="SKOR: 0";score.textSize=18f;score.gravity=Gravity.CENTER;score.setTextColor(Color.YELLOW);root.addView(score,LinearLayout.LayoutParams(-1,34))
 val msg=TextView(this);msg.text="Kutip makanan • Elak cacing lawan";msg.gravity=Gravity.CENTER;msg.setTextColor(Color.WHITE);root.addView(msg,LinearLayout.LayoutParams(-1,28))
 val game=WormView(this){s,n,over->runOnUiThread{score.text="SKOR: $s   PANJANG: $n";if(over)msg.text="TAMAT! Tekan MAIN SEMULA"}}
 root.addView(game,LinearLayout.LayoutParams(-1,0,1f))
 val btn=Button(this);btn.text="MULA BERMAIN";btn.setOnClickListener{game.start();msg.text="Leret skrin untuk mengemudi"};root.addView(btn,LinearLayout.LayoutParams(-1,52))
 val adbox=FrameLayout(this);root.addView(adbox,LinearLayout.LayoutParams(-1,LinearLayout.LayoutParams.WRAP_CONTENT));setContentView(root)
 MobileAds.initialize(this){runOnUiThread{val ad=AdView(this);ad.adUnitId=getString(R.string.admob_banner_ad_unit_id);ad.setAdSize(AdSize.BANNER);adbox.addView(ad,FrameLayout.LayoutParams(-1,-2,Gravity.CENTER));ad.loadAd(AdRequest.Builder().build())}}
 }
 override fun onPause(){super.onPause()}
}
private class WormView(ctx:android.content.Context,val report:(Int,Int,Boolean)->Unit):View(ctx){
 data class Dot(var x:Float,var y:Float,var col:Int)
 data class Seg(var x:Float,var y:Float)
 val p=Paint(3);val h=android.os.Handler(android.os.Looper.getMainLooper());val foods=mutableListOf<Dot>();val worm=mutableListOf<Seg>();val enemies=mutableListOf<MutableList<Seg>>()
 var angle=0.0;var target=0.0;var score=0;var alive=false;var ended=false;var last=0L;var tx=0f;var ty=0f
 val colors=intArrayOf(Color.RED,Color.YELLOW,Color.MAGENTA,Color.CYAN,Color.rgb(255,130,20))
 val loop=object:Runnable{override fun run(){if(!alive)return;val now=android.os.SystemClock.uptimeMillis();val dt=if(last==0L) 0.03f else ((now-last).coerceAtMost(40L)/1000f)
 last=now
 var d=((target-angle+Math.PI*3)%(Math.PI*2))-Math.PI;angle+=d.coerceIn(-2.4*dt.toDouble(),2.4*dt.toDouble())
 val a=worm[0];val nx=a.x+kotlin.math.cos(angle).toFloat()*140*dt;val ny=a.y+kotlin.math.sin(angle).toFloat()*140*dt
 if(nx<30||ny<30||nx>970||ny>1370){alive=false;ended=true;report(score,worm.size,true);invalidate();return}
 worm.add(0,Seg(nx,ny));worm.removeAt(worm.lastIndex)
 for(i in 1 until worm.size){val q=worm[i-1];val b=worm[i];val dx=q.x-b.x;val dy=q.y-b.y;val dist=kotlin.math.hypot(dx,dy);if(dist>18){b.x+=dx/dist*(dist-18);b.y+=dy/dist*(dist-18)}}
 val eaten=foods.filter{kotlin.math.hypot(it.x-nx,it.y-ny)<25};foods.removeAll(eaten.toSet());if(eaten.isNotEmpty()){score+=eaten.size*5;repeat(eaten.size){worm.add(worm.last().copy());food()};report(score,worm.size,false)}
 enemies.forEach{bot->val b=bot[0];val a2=kotlin.math.atan2(ny-b.y,nx-b.x);b.x=(b.x+kotlin.math.cos(a2).toFloat()*65*dt).coerceIn(35f,965f);b.y=(b.y+kotlin.math.sin(a2).toFloat()*65*dt).coerceIn(35f,1365f);for(i in 1 until bot.size){val q=bot[i-1];val z=bot[i];val dx=q.x-z.x;val dy=q.y-z.y;val dd=kotlin.math.hypot(dx,dy);if(dd>18){z.x+=dx/dd*(dd-18);z.y+=dy/dd*(dd-18)}};if(kotlin.math.hypot(b.x-nx,b.y-ny)<25){alive=false;ended=true;report(score,worm.size,true)}}
 invalidate();if(alive)h.postDelayed(this,16)}}
 init{reset()}
 fun reset(){score=0;angle=0.0;target=0.0;worm.clear();repeat(8){worm.add(Seg(480f-it*20,680f))};foods.clear();repeat(130){food()};enemies.clear();repeat(4){val x=(150..850).random().toFloat();val y=(150..1250).random().toFloat();val bot=mutableListOf<Seg>();repeat(8){j->bot.add(Seg(x-j*18,y))};enemies.add(bot)};report(0,worm.size,false)}
 fun food(){foods.add(Dot((40..960).random().toFloat(),(40..1360).random().toFloat(),colors.random()))}
 fun start(){h.removeCallbacks(loop);reset();alive=true;ended=false;last=0;h.post(loop)}
 override fun onDraw(c:Canvas){super.onDraw(c);c.drawColor(Color.rgb(222,236,177));val sc=kotlin.math.min(width/1000f,height/1400f);c.save();c.translate(width/2f-worm[0].x*sc,height/2f-worm[0].y*sc);c.scale(sc,sc)
 p.color=Color.rgb(235,244,197);for(x in 0..9)for(y in 0..13)if((x+y)%2==0)c.drawRect(x*100f,y*100f,(x+1)*100f,(y+1)*100f,p)
 p.color=Color.rgb(100,145,65);p.style=Paint.Style.STROKE;p.strokeWidth=12f;c.drawRect(0f,0f,1000f,1400f,p);p.style=Paint.Style.FILL
 foods.forEach{p.color=it.col;c.drawCircle(it.x,it.y,8f,p);p.color=Color.WHITE;c.drawCircle(it.x-2,it.y-2,2f,p)}
 enemies.forEachIndexed{i,b->drawWorm(c,b,if(i%2==0)Color.rgb(235,95,55) else Color.rgb(65,120,230))}
 drawWorm(c,worm,Color.rgb(35,170,85));c.restore()
 if(ended){p.color=Color.BLACK;p.textSize=30f;p.textAlign=Paint.Align.CENTER;c.drawText("PERMAINAN TAMAT",width/2f,height/2f,p)}
 }
 fun drawWorm(c:Canvas,b:List<Seg>,col:Int){b.asReversed().forEach{s->p.color=col;c.drawCircle(s.x,s.y,17f,p);p.color=Color.argb(60,255,255,255);c.drawCircle(s.x-4,s.y-5,4f,p)};val a=b[0];p.color=Color.WHITE;c.drawCircle(a.x+6,a.y-6,5f,p);c.drawCircle(a.x+6,a.y+6,5f,p);p.color=Color.BLACK;c.drawCircle(a.x+8,a.y-6,2f,p);c.drawCircle(a.x+8,a.y+6,2f,p)}
 override fun onTouchEvent(e:MotionEvent):Boolean{if(e.action==MotionEvent.ACTION_DOWN){tx=e.x;ty=e.y;return true};if(e.action==MotionEvent.ACTION_MOVE||e.action==MotionEvent.ACTION_UP){target=kotlin.math.atan2(e.y-height/2f,e.x-width/2f).toDouble();return true};return true}
}
