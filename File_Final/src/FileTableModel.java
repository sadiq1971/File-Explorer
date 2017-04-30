import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.Date;

/**
 * Created by Sadiqur Rahman on 4/27/2017.
 */
public  class FileTableModel extends AbstractTableModel {

     public  File[] files;
     private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
     private String[] columns = {
             "Icon",
             "File",
             "Size",
             "Last Modified",
     };

     FileTableModel() {
          this(new File[0]);
     }

     FileTableModel(File[] files) {
          this.files = files;
     }

     public Object getValueAt(int row, int column) {
          File file = files[row];
          switch (column) {
               case 0:
                    return fileSystemView.getSystemIcon(file);
               case 1:
                    return fileSystemView.getSystemDisplayName(file);
               case 2:
                    return file.length();
               case 3:
                    return file.lastModified();
          }
          return "";
     }

     public int getColumnCount() {
          return columns.length;
     }

     public Class<?> getColumnClass(int column) {
          switch (column) {
               case 0:
                    return ImageIcon.class;
               case 1:
                    return String.class;
               case 3:
                    return Date.class;
          }
          return String.class;
     }

     public String getColumnName(int column) {
          return columns[column];
     }

     public int getRowCount() {
          return files.length;
     }

     public File getFile(int row) {
          return files[row];
     }

     public void setFiles(File[] files) {
          this.files = files;
          fireTableDataChanged();

     }



}
