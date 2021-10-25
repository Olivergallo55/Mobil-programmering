package com.example.uppgift3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;

/**
 *  Denna klass hanterar ritandet och alla pennor med deras attributer
 *
 * @author Oliver Gallo, olga7031
 */

public class PaintView extends View {

    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final int TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private boolean emboss;
    private boolean blur;
    private int strokeWidth;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    //Konstruktur
    public PaintView(Context context) {
        this(context, null);
    }
    //Konstruktor med alla paint attributer, som stil, färg osv
    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);

        mEmboss = new EmbossMaskFilter(new float[]{1,1,1,}, 0.4f, 6, 3.5f);//gör emboss
        mBlur = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);//gör suddig
    }
    //initialiserar
    public void init(DisplayMetrics metrics){
        int height = metrics.heightPixels;//initialiserar skärmens höjd
        int width = metrics.widthPixels;//initialiserar skärmens bredd

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//gör en bitmap på skärmen
        mCanvas = new Canvas(mBitmap);//gör en canvas som man kan rita på

        currentColor = DEFAULT_COLOR;//initialiserar färgen
        strokeWidth = BRUSH_SIZE;//initialiserar pennans storlek

    }
    //normal penna
    public void normal(){
        emboss = false;
        blur = false;
    }
    //emboss penna
    public void emboss(){
        emboss = false;
        blur = true;
    }
    //suddig penna
    public void blur(){
        emboss = true;
        blur = false;
    }
    //suddar ut hela skärmen
    public void clear(){
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        normal();
        invalidate();
    }

    //metod för att rita
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();//sparar canvasen
        mCanvas.drawColor(backgroundColor);//gör rit ytan till vit

        for(FingerPath fp: paths){
            mPaint.setColor(fp.color);//sätter färgen
            mPaint.setStrokeWidth(fp.strokeWidth);//sätter bredden på pennan
            mPaint.setMaskFilter(null);//normal penna utan maskfilter

            if(fp.emboss)
                mPaint.setMaskFilter(mEmboss);// använder emboss pennan
            else if(fp.blur)
                mPaint.setMaskFilter(mBlur);//använder suddiga pennan

            mCanvas.drawPath(fp.path, mPaint);//gör att man kan målat
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);// gör att man kan se det man har målat på canvasen
        canvas.restore();//tar bort mdificationer från då det senast var sparad
    }

    //startar målandet
    private void touchStart(float x, float y){
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, blur, emboss, strokeWidth, mPath);//gör en ny ritande
        paths.add(fp);//lägger till det i listan

        mPath.reset();
        mPath.moveTo(x,y);//gör ett linje mellan x och y
        mX = x;//tilsätter värdet på x
        mY = y;//tillsätter värdet på y
    }
    //metoden för ritandes rörelse
    private void touchMove(float x, float y){
        float dx = Math.abs(x-mX);//räknar ut absoluta x värdet
        float dy = Math.abs(y-mY);//räknar ut absoluta y värdet

        //gör det smidigta att rita
        if(dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY)/2); //gör så att man kan rita kurvor osv...
            mX = x;// gör pennan till en smal linje i x ledet
            mY = y;// gör pennan till en smal linje i y ledet
        }
    }
    //lägger till en punkt där man slutade måla
    private void touchUp(){
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();//får användarens rörelse i x led
        float y = event.getY();//får användarens rörelse i y led

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN://hanterar när man rör skärmen
                touchStart(x,y);//metod för att starta måla
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE://hanterar rörelsen mellan start och slut
                touchMove(x,y);//ritar
                invalidate();
                break;
            case MotionEvent.ACTION_UP://hanterar när man har släppt skärmen
                touchUp();//metod för när man slutar måla
                invalidate();
                break;
        }
        return true;
    }
}
