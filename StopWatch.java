import java.io.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * ストップウォッチ画面
 */
public class StopWatch extends GUI implements Runnable{

    /** スレッド */
    private Thread thread = null;

    /** 開始日時 */
    private Calendar date;
    SimpleDateFormat sdf;

    /** 秒 */
    private int s;
    /** 分 */
    private int m;
    /** 時 */
    private int h;

    /** 裏画面 */
    private Image iBuffer;
    private Graphics gOffscreen;
    private int width = 600;
    private int height = 400;

    /**
     * コンストラクタ
     * @param width 幅
     * @param height 高さ
     */
    public StopWatch(){
        s=0; 
        m=0; 
        h=0;
        date = Calendar.getInstance();
        sdf = new SimpleDateFormat("開始:yyyy/MM/dd HH:mm:ss");
        setSize(width, height);
        setFont(new Font("Meiryo", Font.PLAIN, 25));
    }
    public void init(){
        iBuffer = createImage(width, height);
        gOffscreen = iBuffer.getGraphics();
    }

    public void paint(Graphics g){
        if(gOffscreen != null){
            gOffscreen.clearRect(0, 0, width, height);

            gOffscreen.drawString(sdf.format(date.getTime()), 100, 100);
            String str = String.format("%02d", h)
                +":"+String.format("%02d", m)
                +":"+String.format("%02d", s);
            gOffscreen.drawString(str, 200, 200);
        }
        g.drawImage(iBuffer, 0, 0, this);
    }

    public void start(){
        if(thread == null){
            thread = new Thread(this, "clock");
            gOffscreen.drawRect(0, 0, width, height);
            thread.start();
        }
    }

    public void stop(){
        thread = null;
    }

    public void run(){
        while(Thread.currentThread()==thread){
            repaint();
            try{
                Thread.sleep(1000);
                countUp();
            }
            catch(InterruptedException e){

            }
        }
    }

    public void update(Graphics g){
        paint(g);
    }



    private void countUp(){
        s++;
        if(s>59){
            s=0;
            m++;
        }
        if(h>59){
            m=0;
            h++;
        }
    }
}
