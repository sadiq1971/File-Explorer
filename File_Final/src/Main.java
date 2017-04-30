import javax.swing.*;

/**
 * Created by Sadiqur Rahman on 4/27/2017.
 */
public class Main {


     public static void main(String [] arg)
     {
          SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                         new EplorerGui();
               }
          });
     }
}
