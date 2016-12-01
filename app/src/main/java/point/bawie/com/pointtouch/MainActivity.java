package point.bawie.com.pointtouch;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    //添加一个点存放集合
    PointF mPointF = new PointF();
    // 判断几个触控点模式
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    float  oldDist;
    float newDist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageView  mImageView = (ImageView) findViewById(R.id.iamge);
        mImageView.setOnTouchListener(new View.OnTouchListener() {    private Matrix mMatrix = new Matrix();
            private Matrix newMatrix = new Matrix();
            private float x;
            private float y;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        mMatrix.set(mImageView.getImageMatrix());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //如果是一个手指点击,那么点击移动图片
                        if(mode == DRAG) {
                            newMatrix.set(mMatrix);
                            newMatrix.postTranslate(event.getX() - x, event.getY() - y);
                        }
                        //如果是两个手指 那么则图片缩放
                        else if(mode  == ZOOM){
                            newDist = spacing(event);
                            if(newDist >10f){
                                mMatrix.set(newMatrix);
                                float scale = newDist/oldDist;
                                //设置缩放比例和图片和图片中点位置
                                mMatrix.postScale(scale,scale,mPointF.x,mPointF.y);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                       oldDist = spacing(event);
                        if(oldDist >10f){
                            newMatrix.set(mMatrix);
                            midPoint(mPointF,event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }
                mImageView.setImageMatrix(newMatrix);
                return true;
            }
        });
    }
    private float spacing(MotionEvent event){
        float  x = event.getX(0) - event.getX(1);
        float  y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x*x-y*y);
    }
private void midPoint(PointF pointF,MotionEvent event){
    float x = event.getX(0)+event.getX(1);
    float y = event.getY(0)+event.getY(1);
    mPointF.set(x/2,y/2);
}

}
