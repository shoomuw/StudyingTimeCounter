import java.awt.event.*;

public class STC {
    public static void main(String[] args){
        StopWatch2 gui = new StopWatch2();
        gui.addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    System.exit(0);
                }
            }
        );
        gui.setVisible(true);
    }
}
