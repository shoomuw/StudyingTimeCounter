import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class StopWatch extends GUI implements Runnable{

    private Thread thread = null;

    private Calendar date;

    private int s = 0;
    private int m = 0;
    private int h = 0;

    public StopWatch(int width, int height){
        setSize(width, height);
    }

    public void paint(Graphics g){
        
    }
    
    public void run(){
        while(Thread.currentThread()==thread){
            repaint();
            try{
                thread.sleep(1000);
            }
            catch(InterruptedException e){

            }
        }
    }
}
