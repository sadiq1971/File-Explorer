import javax.swing.*;

/**
 * Created by Sadiqur Rahman on 4/27/2017.
 */
public class EplorerGui {

     public EplorerGui() {
          try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (ClassNotFoundException e) {
               e.printStackTrace();
          } catch (InstantiationException e) {
               e.printStackTrace();
          } catch (IllegalAccessException e) {
               e.printStackTrace();
          } catch (UnsupportedLookAndFeelException e) {
               e.printStackTrace();
          }


          /*try {
               for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                         UIManager.setLookAndFeel(info.getClassName());
                         break;
                    }
               }
          } catch (Exception e) {
               // If Nimbus is not available, you can set the GUI to another look and feel.
          }*/



          CreatGui();

     }

     private void CreatGui() {
          JFrame f=new JFrame("File Explorer");
          f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          FileManager fileManager = new FileManager();
          f.setContentPane(fileManager.getGui());
          f.pack();
          f.setLocationByPlatform(true);
          //f.setMinimumSize(f.getSize());
          f.setSize(1050,550);
          f.setVisible(true);
          fileManager.showRootFile();
     }
}
