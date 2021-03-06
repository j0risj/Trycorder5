package net.ddns.mlsoftlaberge.trycorder.trycorder;

/*
*  By Martin Laberge (mlsoft), From March 2016 to november 2016.
*  Licence: Can be shared with anyone, for non profit, provided my name stays in the comments.
*  This is a conglomerate of examples codes found in differents public forums on internet.
*  I just used the public knowledge to fit a special way to use an android phone functions.
*/

/* Copyright 2016 Martin Laberge
*
*        Licensed under the Apache License, Version 2.0 (the "License");
*        you may not use this file except in compliance with the License.
*        You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*        Unless required by applicable law or agreed to in writing, software
*        distributed under the License is distributed on an "AS IS" BASIS,
*        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*        See the License for the specific language governing permissions and
*        limitations under the License.
*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mlsoft on 16-06-25.
 */
// ============================================================================
// class defining the sensor display widget
public class TraSensorView extends TextView {
    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Paint mPaint2 = new Paint();
    private Paint mPaint3 = new Paint();
    private Canvas mCanvas = new Canvas();

    private int mWidth;
    private int mHeight;

    private int mode=1;   // 1=in 2=out

    private int position=0;

    // initialize the 3 colors, and setup painter
    public TraSensorView(Context context) {
        super(context);
        // text paint
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(24);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        // circle paint
        mPaint2.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setStrokeWidth(2);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(Color.BLUE);
        // line paint
        mPaint3.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint3.setStrokeWidth(2);
        mPaint3.setStyle(Paint.Style.STROKE);
        mPaint3.setColor(Color.LTGRAY);
    }

    public void setmode(int no) {
        mode=no;
    }

    public void setposition(int no) {
        position=no;
        invalidate();
    }

    // ======= timer section =======
    private Timer timer=null;
    private MyTimer myTimer;

    public void stop() {
        if(timer!=null) {
            timer.cancel();
            timer=null;
        }
    }

    public void start() {
        position=1;
        // start the timer to eat this stuff and display it
        timer = new Timer("transporter");
        myTimer = new MyTimer();
        timer.schedule(myTimer, 10L, 10L);
    }

    private class MyTimer extends TimerTask {
        public void run() {
            position++;
            postInvalidate();
            if(position>=250) {
                cancel();
                position=0;
                postInvalidate();
            }
        }
    }

    // =========== textview callbacks =================
    // initialize the bitmap to the size of the view, fill it white
    // init the view state variables to initial values
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.BLACK);
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    // draw
    @Override
    public void onDraw(Canvas viewcanvas) {
        synchronized (this) {
            if (mBitmap != null) {
                // clear the surface
                mCanvas.drawColor(Color.BLACK);
                // ajust alpha level of person
                if(position==0) {
                    if(mode==1) {
                        mPaint3.setAlpha(255);
                    } else {
                        mPaint3.setAlpha(0);
                    }
                } else {
                    if(mode==1) {
                        mPaint3.setAlpha(position);
                    } else {
                        mPaint3.setAlpha(255-position);
                    }
                }
                // draw the vertical person
                int px=mWidth/2;
                int py=mHeight/2;
                int len=mHeight/5;
                mCanvas.drawLine(px,py-len,px,py+len,mPaint3);
                mCanvas.drawLine(px-len/2,py,px+len/2,py,mPaint3);
                mCanvas.drawLine(px,py+len,px-len/2,py+len*2,mPaint3);
                mCanvas.drawLine(px,py+len,px+len/2,py+len*2,mPaint3);
                mCanvas.drawCircle(px,py-len-len/2,len/2,mPaint3);
                // draw the transport circle effect
                if(position!=0) {
                    if (mode == 1) {
                        mCanvas.drawCircle(mWidth / 2, mHeight / 2, position, mPaint2);
                    } else {
                        mCanvas.drawCircle(mWidth / 2, mHeight / 2, 250 - position, mPaint2);
                    }
                }
                // transfer the bitmap to the view
                viewcanvas.drawBitmap(mBitmap, 0, 0, null);
            }
        }
        super.onDraw(viewcanvas);
    }

}

