import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Created by Sadiqur Rahman on 4/27/2017.
 */

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

     private FileSystemView fileSystemView;

     private JLabel label;

     FileTreeCellRenderer() {
          label = new JLabel();
          label.setOpaque(true);
          fileSystemView = FileSystemView.getFileSystemView();
     }

     @Override
     public Component getTreeCellRendererComponent(
             JTree tree,
             Object value,
             boolean selected,
             boolean expanded,
             boolean leaf,
             int row,
             boolean hasFocus) {

          DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
          File file = (File)node.getUserObject();
          label.setIcon(fileSystemView.getSystemIcon(file));
          label.setText(fileSystemView.getSystemDisplayName(file));
          label.setToolTipText(file.getPath());

          if (selected) {
               label.setBackground(new Color(220,250,255));
               //label.setForeground(textSelectionColor);
          } else {
               label.setBackground(backgroundNonSelectionColor);
               label.setForeground(textNonSelectionColor);
          }

          return label;
     }
}