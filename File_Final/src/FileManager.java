import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sadiqur Rahman on 4/27/2017.
 */

class FileManager {


     public static final String APP_TITLE = "FileMan";
     private Desktop desktop;
     public FileSystemView fileSystemView;
     private File currentFile;
     private JPanel gui;
     private JTree tree;
     private DefaultTreeModel treeModel;
     private JTable table;
     private FileTableModel fileTableModel;

     //////////////////////
     public static FileTilesModel fileTilesModel;

     private ListSelectionListener listSelectionListener;
     private boolean cellSizesSet = false;
     private int rowIconPadding = 6;
     DefaultMutableTreeNode node;

     /* File controls. */
     private JButton openFile;
     private JButton printFile;
     private JButton editFile;
     private JButton deleteFile;
     private JButton newFile;
     private JButton copyFile;
     /* File details. */
     private JLabel fileName;
     private JTextField path;
     private JLabel date;
     private JLabel size;
     private JCheckBox readable;
     private JCheckBox writable;
     private JCheckBox executable;
     private JRadioButton isDirectory;
     private JRadioButton isFile;

     /* GUI options/containers for new File/Directory creation.  Created lazily. */
     private JPanel newFilePanel;
     private JRadioButton newTypeFile;
     private JTextField name;
     Boolean view;
     JPanel detailView;
     JScrollPane tableScroll;
     JScrollPane treeScroll;

     public Container getGui() {
          if(gui == null) {
               gui = new JPanel(new BorderLayout(10, 10));
               gui.setBorder(new EmptyBorder(10, 10, 10, 10));
               view = true;
               fileSystemView = FileSystemView.getFileSystemView();
               desktop = Desktop.getDesktop();
               detailView = new JPanel(new BorderLayout(10, 10));
               detailView.setBackground(Color.WHITE);
               gui.setBackground(Color.WHITE);


               CreateTableGui();
               CreateTreeGui();
               CreateDetailsGui();
               Factory();

          }
          return gui;
     }

     public void showRootFile() {
          tree.setSelectionInterval(0, 0);
     }

     private TreePath findTreePath(File find) {
          for (int ii = 0; ii < tree.getRowCount(); ii++) {
               TreePath treePath = tree.getPathForRow(ii);
               Object object = treePath.getLastPathComponent();
               DefaultMutableTreeNode node = (DefaultMutableTreeNode) object;
               File nodeFile = (File) node.getUserObject();

               if(nodeFile == find) {
                    return treePath;
               }
          }
          // not found!
          return null;
     }


     private void showErrorMessage(String errorMessage, String errorTitle) {
          JOptionPane.showMessageDialog(
                  gui,
                  errorMessage,
                  errorTitle,
                  JOptionPane.ERROR_MESSAGE
          );
     }

     private void showThrowable(Throwable t) {
          t.printStackTrace();
          JOptionPane.showMessageDialog(
                  gui,
                  t.toString(),
                  t.getMessage(),
                  JOptionPane.ERROR_MESSAGE
          );
          gui.repaint();
     }


     private void setTableData(final File[] files) {
          SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                    if(fileTableModel == null) {
                         fileTableModel = new FileTableModel();
                         table.setModel(fileTableModel);
                    }
                    table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                    int count=0;
                    for(File f:files)
                    {
                         count++;
                    }

                    //sorting the file//
                    for(int i=0;i<count-1;i++)
                    {

                         for(int j=0;j<count-i-1;j++)
                         if(files[j].length()>files[j+1].length())
                         {
                              File t=files[j];
                              files[j]=files[j+1];
                              files[j+1]=t;

                         }
                    }




                    fileTableModel.setFiles(files);


                    table.getSelectionModel().addListSelectionListener(listSelectionListener);
                    if(!cellSizesSet) {
                         Icon icon = fileSystemView.getSystemIcon(files[0]);

                         table.setRowHeight(icon.getIconHeight() + rowIconPadding);

                         setColumnWidth(0, -1);
                         //setColumnWidth(1,120);
                         //setColumnWidth(2,50);
                         setColumnWidth(3,140);

                        // setColumnWidth(3, 1000);
                         table.getColumnModel().getColumn(3).setMaxWidth(200);
                         cellSizesSet = true;
                    }
                    table.setSelectionBackground(new Color(220,255,255));
                    table.setSelectionForeground(Color.BLACK);
                    table.setBackground(Color.WHITE);
                    tableScroll.setBackground(Color.WHITE);

               }
          });
     }


     private void setColumnWidth(int column, int width) {
          TableColumn tableColumn = table.getColumnModel().getColumn(column);
          if(width < 0) {
               JLabel label = new JLabel((String) tableColumn.getHeaderValue());
               Dimension preferred = label.getPreferredSize();
               width = (int) preferred.getWidth() + 14;
          }
          tableColumn.setPreferredWidth(width);
          //tableColumn.setResizable();
          tableColumn.setMaxWidth(width);
          tableColumn.setMinWidth(width);
     }


     private void showChildren(final DefaultMutableTreeNode node) {
          tree.setEnabled(false);

          SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
               @Override
               public Void doInBackground() {
                    File file = (File) node.getUserObject();
                    if(file.isDirectory()) {
                         File[] files = fileSystemView.getFiles(file, true); //!!
                         if(node.isLeaf()) {
                              for (File child : files) {
                                   if(child.isDirectory()) {
                                        publish(child);
                                   }
                              }
                         }
                         setTableData(files);
                         // setTilesData(files);
                    }
                    return null;
               }

               @Override
               protected void process(List<File> chunks) {
                    for (File child : chunks) {
                         node.add(new DefaultMutableTreeNode(child));
                    }
               }

               @Override
               protected void done() {
                    tree.setEnabled(true);
               }
          };
          worker.execute();
     }


     public void setFileDetails(File file) {
          currentFile = file;
          Icon icon = fileSystemView.getSystemIcon(file);
          fileName.setIcon(icon);
          fileName.setText(fileSystemView.getSystemDisplayName(file));
          path.setText(file.getPath());

          JFrame f = (JFrame) gui.getTopLevelAncestor();
          if(f != null) {
               f.setTitle(
                       APP_TITLE +
                               " :: " +
                               fileSystemView.getSystemDisplayName(file));
          }

          gui.repaint();
     }


     void CreateTableGui()
     {
          table = new JTable();
          table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          table.setAutoCreateRowSorter(true);
          table.setShowVerticalLines(false);
          table.setShowHorizontalLines(false);

          listSelectionListener = new ListSelectionListener() {
               @Override
               public void valueChanged(ListSelectionEvent lse) {
                    int row = table.getSelectionModel().getLeadSelectionIndex();
                    setFileDetails(((FileTableModel) table.getModel()).getFile(row));
               }
          };

          table.getSelectionModel().addListSelectionListener(listSelectionListener);
          tableScroll = new JScrollPane(table);
          Dimension d = tableScroll.getPreferredSize();
          tableScroll.setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight()));



          table.addMouseListener(new MouseAdapter() {
               public void mousePressed(MouseEvent me) {
                    JTable table = (JTable) me.getSource();
                    Point p = me.getPoint();
                    int row = table.rowAtPoint(p);
                    if(me.getClickCount() == 2) {
                         if(row > -1) {
                              File file = fileTableModel.getFile(row);
                              DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
                              showChildren(node);
                              setFileDetails((File) node.getUserObject());
                              if(!file.isDirectory()) {
                                   try {
                                        desktop.open(file);
                                   } catch (Throwable t) {
                                        showThrowable(t);
                                   }
                                   gui.repaint();
                              }
                         }
                    }
               }
          });

     }
     void CreateTreeGui()
     {
          DefaultMutableTreeNode root = new DefaultMutableTreeNode();
          treeModel = new DefaultTreeModel(root);


          TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
               public void valueChanged(TreeSelectionEvent tse) {
                    node =
                            (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
                    showChildren(node);
                    setFileDetails((File) node.getUserObject());
                    if(fileTilesModel == null && !view) {
                         fileTilesModel = new FileTilesModel(node, gui, FileManager.this);
                         detailView.add(fileTilesModel.pane, BorderLayout.CENTER);
                    }
                    if(!view) {
                         fileTilesModel.ReLoad(node);
                         detailView.add(fileTilesModel.pane, BorderLayout.CENTER);
                    }
               }
          };



          // show the file system roots.
          File[] roots = fileSystemView.getRoots();
          for (File fileSystemRoot : roots) {
               DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
               root.add(node);
               //showChildren(node);
               //
               File[] files = fileSystemView.getFiles(fileSystemRoot, true);
               for (File file : files) {
                    if(file.isDirectory()) {
                         node.add(new DefaultMutableTreeNode(file));
                    }
               }
               //
          }

          tree = new JTree(treeModel);
          tree.setRootVisible(false);
          tree.addTreeSelectionListener(treeSelectionListener);
          tree.setCellRenderer(new FileTreeCellRenderer());
          tree.expandRow(0);
          treeScroll = new JScrollPane(tree);

          // as per trashgod tip
          tree.setVisibleRowCount(15);

          Dimension preferredSize = treeScroll.getPreferredSize();
          Dimension widePreferred = new Dimension(
                  200,
                  (int) preferredSize.getHeight());
          treeScroll.setPreferredSize(widePreferred);
     }


     void CreateDetailsGui()
     {
          JPanel fileMainDetails = new JPanel(new BorderLayout(4, 2));
          fileMainDetails.setBorder(new EmptyBorder(0, 6, 0, 6));
          fileMainDetails.setBackground(Color.WHITE);

          JPanel fileDetailsLabels = new JPanel(new GridLayout(0, 1, 2, 2));
          fileDetailsLabels.setBackground(Color.WHITE);
          fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);

          JPanel fileDetailsValues = new JPanel(new GridLayout(0, 1, 2, 2));
          fileDetailsValues.setBackground(Color.WHITE);
          fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);

          fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));
          fileName = new JLabel();
          fileDetailsValues.add(fileName);
          fileDetailsLabels.add(new JLabel("Path/name", JLabel.TRAILING));
          path = new JTextField(5);
          path.setEditable(false);
          fileDetailsValues.add(path);

          JButton radio = new JButton("Change View");
          fileMainDetails.add(radio, BorderLayout.EAST);
          radio.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e) {
                    view = !view;
                    Factory();
               }
          });
          radio.setBackground(Color.WHITE);



          int count = fileDetailsLabels.getComponentCount();
          for (int ii = 0; ii < count; ii++) {
               fileDetailsLabels.getComponent(ii).setEnabled(false);
          }


          JPanel fileView = new JPanel(new BorderLayout(3, 3));

          //fileView.add(toolBar, BorderLayout.NORTH);
          fileView.add(fileMainDetails, BorderLayout.CENTER);

          detailView.add(fileView, BorderLayout.SOUTH);

          JSplitPane splitPane = new JSplitPane(
                  JSplitPane.HORIZONTAL_SPLIT,
                  treeScroll,
                  detailView);
          gui.add(splitPane, BorderLayout.CENTER);

          JPanel simpleOutput = new JPanel(new BorderLayout(3, 3));
          gui.add(simpleOutput, BorderLayout.SOUTH);

     }


     void Factory()
     {
          gui.repaint();
          if(fileTilesModel!=null)
          {
               detailView.remove(fileTilesModel.pane);
          }


          if(view) {
               DefaultMutableTreeNode node = new DefaultMutableTreeNode(currentFile);
               showChildren(node);
               detailView.add(tableScroll, BorderLayout.CENTER);
          }
          if(!view) {
               detailView.remove(tableScroll);
               if(fileTilesModel == null) {
                    fileTilesModel = new FileTilesModel(node, gui, FileManager.this);

               }
               detailView.add(fileTilesModel.pane, BorderLayout.CENTER);
               DefaultMutableTreeNode node = new DefaultMutableTreeNode(currentFile);
               fileTilesModel.ReLoad(node);
               detailView.updateUI();
          }
     }
}
