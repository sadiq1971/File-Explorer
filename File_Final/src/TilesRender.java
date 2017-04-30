import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Sadiqur Rahman on 4/28/2017.
 */

public class TilesRender  extends JLabel implements ListCellRenderer {

          public TilesRender() {
               setOpaque(false);
               setIconTextGap(3);
          }

          public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
          {
               TilesNode node = (TilesNode) value;
               JPanel box=new JPanel();
               JLabel icon=new JLabel(node.getIcon());
               JLabel name=new JLabel(node.getName());
               box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
               box.add(icon);
               box.add(name);
               box.setBackground(Color.WHITE);
               box.setBorder(new EmptyBorder(0,10,10,5));

               if(isSelected) {
                    box.setBackground(new Color(220,250,255));
               }


               return box;
          }
}
