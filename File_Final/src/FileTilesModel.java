import sun.awt.shell.ShellFolder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Sadiqur Rahman on 4/28/2017.
 */
public class FileTilesModel {


     public TilesNode tilesNodes[];

     public JList fileList ;

     public JScrollPane pane;

     public File []storeFile;

     private JPanel gui;

     public FileTilesModel(DefaultMutableTreeNode node,JPanel gui,FileManager fm) {
          fileList = new JList();
          fileList.setBorder(new EmptyBorder(10,10,10,10));
          ReLoad(node);
          fileList.setCellRenderer(new TilesRender());
          fileList.setVisibleRowCount(-1);
          fileList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
          storeFile=new File[0];

          pane = new JScrollPane(fileList);


          fileList.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent evt) {
                    JList list = (JList)evt.getSource();
                    if (evt.getClickCount() == 2) {
                         int index = list.locationToIndex(evt.getPoint());
                         System.out.println(index);
                         if(index>-1) {
                              File file = getFile(index);
                              DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);

                              ReLoad(node);
                              fm.setFileDetails(file);

                              if(!file.isDirectory()) {
                                   try {
                                        //desktop.open(file);
                                        Desktop.getDesktop().open(file);
                                   } catch (Throwable t) {
                                        //showThrowable(t);
                                   }
                                   gui.repaint();
                              }
                         }

                    }
               }
          });


     }


     public  void ReLoad(DefaultMutableTreeNode node)
     {
          File file = (File) node.getUserObject();
          FileSystemView fileSystemView = FileSystemView.getFileSystemView();
          //////////

          storeFile = fileSystemView.getFiles(file, true); //!!

          if(file.isDirectory()) {
               tilesNodes=new TilesNode[file.listFiles().length];
               int i=0;
               for (File child : file.listFiles()) {
                    ShellFolder sf = null;
                    try {
                         sf = ShellFolder.getShellFolder(child);
                    } catch (FileNotFoundException e) {
                         e.printStackTrace();
                    }
                    Icon icon = new ImageIcon(sf.getIcon(true), sf.getFolderType());

                    tilesNodes[i]= new TilesNode(FileSystemView.getFileSystemView().getSystemDisplayName(child), icon);
                    i++;
               }
          }else
          {
               tilesNodes=new TilesNode[1];
               tilesNodes[0]= new TilesNode(file.getName(),(Icon) FileSystemView.getFileSystemView().getSystemIcon(file));
          }
          fileList.setListData(tilesNodes);
     }

     File getFile(int index){
          return storeFile[index];
     }
}
