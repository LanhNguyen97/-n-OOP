
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Table3 extends JPanel {
    private   static Object[][] data ;
    static File chosenFile;
    private static String pathExcel="E:\\Java\\jexcelapi\\Book1.xls" ;
	static DefaultTableModel model;
	static JTable table;
	public static File chosenSoundFile;
 
   
    public static void getDataFromExcel(String pathExcel){
		Workbook wk;
		try{
			
			File file = new File(pathExcel);
			wk = Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			int rows = sheet.getRows();
			int lastRow = rows-1;
			int cols = sheet.getColumns();
			data=new Object[rows][cols];
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					Cell cell = sheet.getCell(col, row);
					if (col == 0) {
						String m = cell.getContents();
						data[row][col] = m;
		} 
		} 
				
		}
		wk.close();
		}catch(BiffException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
    }
 
    
    public Table3() {
    	
        super(new GridLayout(1,0));

        String[] columnNames = {"Sub"};
        


        final JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

  
    
    private static void createAndShowGUI() {
    	
    	
    	
        //Create and set up the window.
        JFrame frame = new JFrame("MediaPlayer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 1, 5, 5));
        
        JPanel buttonPanel = new JPanel();
    	
		JButton a = new JButton("Browser MP3 File..");
		JButton b = new JButton("Browser Excecl File..");
		JButton cc = new JButton("Play");
		
		
		JTextField d = new JTextField();
		
		
		frame.add(a);
		frame.add(b);
		frame.add(d);
		frame.add(cc);
		
		a.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				chosenSoundFile = fileChooser.getSelectedFile();
			}
			
		});
		cc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				try{
					File soundFile = chosenSoundFile;
					String uriSource = "file:///" + ("" + soundFile).replace("\\", "/").replaceAll(" ", "%20");
					Media me = new Media(uriSource);
					MediaPlayer player = new MediaPlayer(me);
					player.play();
					} catch (Exception e1) {
						System.out.println(e1);
				}
			}
		});
		
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				String linkExcel = file.getAbsolutePath();
				d.setText(linkExcel);
				
				getDataFromExcel(linkExcel);
			}
			
		});
      

        //Create and set up the content pane.
        Table3 newContentPane = new Table3();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.add(newContentPane);

        //Display the window.
        frame.pack();
        frame.show(true);
    }

   

	public static void main(String[] args) {
    	
    	new JFXPanel();
    	getDataFromExcel(pathExcel);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
