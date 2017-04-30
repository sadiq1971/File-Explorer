import javax.swing.*;

/**
 * Created by Sadiqur Rahman on 4/28/2017.
 */
public class TilesNode{
     String name;
     Icon icon;
     TilesNode(String name, Icon icon)
     {
          this.name=name;
          this.icon=icon;

        /*  Image image = icon.getImage(); // transform it
          Image newimg = image.getScaledInstance(20,20, Image.SCALE_SMOOTH); // scale it the smooth way
          this.icon = new ImageIcon(newimg);*/
     }

     public void setIcon(Icon icon) {
          this.icon = icon;
     }

     public void setName(String name) {
          this.name = name;
     }

     public Icon getIcon() {
          return icon;
     }

     public String getName() {
          return name;
     }

}
