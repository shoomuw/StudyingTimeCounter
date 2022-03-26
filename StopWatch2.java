import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.text.SimpleDateFormat;

/**
 * ストップウォッチ画面
 */
public class StopWatch2 extends GUI implements Runnable, ActionListener{

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

    /** 1フレームの経過時間 */
    private long time;
    private long f_start;
    private long f_end;

    private int width = 600;
    private int height = 400;

    /** 記録用文字列 */
    private String timeStr;
    private String dateStr;


    /** 時間表示 */
    private TextField startTime = new TextField();
    private TextField timeField = new TextField();

    /** ボタン群 */
    private Panel buttonP = new Panel();
    private Button button = new Button("スタート");
    private Button writeB = new Button("記録");
    private Button resetB = new Button("リセット");

    /** フラグ */
    private boolean isStart;
    private boolean isDefault;

    /**
     * コンストラクタ
     */
    public StopWatch2(){
        s=0; 
        m=0; 
        h=0;
        setTitle("STC");
        setLayout(new BorderLayout());
        buttonP.setLayout(new GridLayout(1, 3));
        sdf = new SimpleDateFormat("開始:yyyy/MM/dd HH:mm:ss");
        startTime.setEditable(false);
        startTime.setFont(new Font("Meiryo", Font.PLAIN, 25));
        startTime.setText("スタートを押して開始");
        setSize(width, height);
        timeField.setEditable(false);
        timeField.setFont(new Font("Meiryo", Font.PLAIN, 100));
        isStart = false;
        isDefault = true;
        add(startTime, BorderLayout.NORTH);
        add(timeField, BorderLayout.CENTER);
        button.addActionListener(this);
        //button.setBounds(100, 300, 120, 40);
        writeB.addActionListener(this);
        writeB.setEnabled(false);
        //writeB.setBounds(250, 300, 120, 40);
        resetB.addActionListener(this);
        resetB.setEnabled(false);
        //resetB.setBounds(400, 300, 120, 40);
        buttonP.add(button);
        buttonP.add(resetB);
        buttonP.add(writeB);
        add(buttonP, BorderLayout.SOUTH);
    }

    public void paint(Graphics g){
        String str = String.format("%02d", h)
                +":"+String.format("%02d", m)
                +":"+String.format("%02d", s);
        timeField.setText(str);
    }

    public void start(){
        if(thread == null && isStart && isDefault){
            thread = new Thread(this, "clock");
            thread.start();
            date = Calendar.getInstance();
            startTime.setText(sdf.format(date.getTime()));
            f_start = System.currentTimeMillis();
            time = 0;
            isDefault = false;
        }
    }

    public void restart(){
        if(thread == null && isStart && !isDefault){
            thread = new Thread(this, "clock");
            thread.start();
            f_start = System.currentTimeMillis();
        }
    }
    public void stop(){
        thread = null;
        isStart = false;
    }

    public void run(){
        while(Thread.currentThread()==thread){
            repaint();
            try{
                f_end = System.currentTimeMillis();
                time = (f_end - f_start);
                if(time >= 1000.0){
                    time = 0;
                    f_start = System.currentTimeMillis();
                    countUp();
                }  
            }
            catch(Exception e){

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
            if(!isStart){
                if(isDefault){
                    isStart = true;
                    button.setLabel("一時停止");
                    writeB.setEnabled(false);
                    resetB.setEnabled(false);
                    start();
                }
                else{
                    isStart = true;
                    button.setLabel("一時停止");
                    writeB.setEnabled(false);
                    resetB.setEnabled(false);
                    restart();
                }
            }
            else{
                isStart = false;
                button.setLabel("再開");
                writeB.setEnabled(true);
                resetB.setEnabled(true);
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
            reset();
            writeB.setEnabled(false);
            button.setLabel("スタート");
        }
    }

    public void reset(){
        h=0;
        m=0;
        s=0;
        isDefault = true;
        button.setLabel("スタート");
        resetB.setEnabled(false);
        writeB.setEnabled(false);
        startTime.setText("スタートを押して開始");
        date = Calendar.getInstance();
        repaint();
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
