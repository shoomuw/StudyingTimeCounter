import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * ストップウォッチ画面
 */
public class StopWatch extends GUI implements Runnable, ActionListener{

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

    /** 記録用文字列 */
    private String timeStr;
    private String dateStr;

    /** 裏画面 */
    private Image iBuffer;
    private Graphics gOffscreen;
    private int width = 600;
    private int height = 400;

    /** ボタン群 */
    private Button button = new Button("開始");
    private Button writeB = new Button("記録");
    private Button resetB = new Button("リセット");

    /** フラグ */
    private boolean isStarted;

    /**
     * コンストラクタ
     */
    public StopWatch(){
        s=0; 
        m=0; 
        h=0;
        setLayout(null);
        date = Calendar.getInstance();
        sdf = new SimpleDateFormat("開始:yyyy/MM/dd HH:mm:ss");
        setSize(width, height);
        setFont(new Font("Meiryo", Font.PLAIN, 25));
        isStarted = false;
        button.addActionListener(this);
        button.setBounds(100, 300, 120, 40);
        writeB.addActionListener(this);
        writeB.setBounds(250, 300, 120, 40);
        resetB.addActionListener(this);
        resetB.setBounds(400, 300, 120, 40);
        add(button);
        add(resetB);
    }

    public void init(){
        iBuffer = createImage(width, height);
        gOffscreen = iBuffer.getGraphics();
    }

    public void paint(Graphics g){
        if(gOffscreen != null){
            gOffscreen.clearRect(0, 0, width, height-100);

            gOffscreen.drawString(sdf.format(date.getTime()), 100, 100);
            String str = String.format("%02d", h)
                +":"+String.format("%02d", m)
                +":"+String.format("%02d", s);
            gOffscreen.drawString(str, 200, 200);
        }
        g.drawImage(iBuffer, 0, 0, this);
    }

    public void start(){
        if(thread == null && isStarted != false){
            thread = new Thread(this, "clock");
            gOffscreen.drawRect(0, 0, width, height);
            thread.start();
        }
    }

    public void stop(){
        thread = null;
        isStarted = false;
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

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source==button){
            if(isStarted == false){
                isStarted = true;
                button.setLabel("一時停止");
                remove(writeB);
                start();
            }
            else{
                isStarted = false;
                button.setLabel("開始");
                add(writeB);
                stop();
            }
        }
        if(source == resetB){
            stop();
            reset();
            repaint();
        }
        if(source == writeB){
            writeFile();
            remove(button);
            remove(writeB);
        }
    }

    public void reset(){
        h=0;
        m=0;
        s=0;
        button.setLabel("開始");
        add(button);
        remove(writeB);
        date = Calendar.getInstance();
    }

    public void writeFile(){
        FileWriter fw;
        File file = new File("rec.csv");
        SimpleDateFormat f = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss,");
        dateStr = f.format(date.getTime());
        timeStr = Integer.toString(h)+","
                + Integer.toString(m)+","
                + Integer.toString(s);
        try{

            if(file.createNewFile()){
                if(!file.canWrite()){
                    file.setWritable(true);
                }

                fw = new FileWriter(file, true);

                fw.write(dateStr+timeStr+"\n");
                fw.close();
            }
            else{
                fw = new FileWriter(file, true);
                fw.write(dateStr+timeStr+"\n");
                fw.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
