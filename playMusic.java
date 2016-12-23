import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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

public class playMusic extends JFrame {
	public static JTable table;
	public String[] columnsname = { "Thoi Gian", "Loi bai hat" };
	public static Object[][] data = {};
	public static MediaPlayer play;
	public static int ktra = 0;
	public static int hangChon, hangCuoi, ktraThread;
	public static String thoigianbd, thoigiankt = "3:27", thoigiankt1;
	public static Thread ttrinh1, ttrinh2;
	public static DefaultTableModel model;
	public static String time1, time2;
	public static JTextField c;

	public playMusic() {
		showGUI();
	}

	public void showGUI() {
		JFrame frame = new JFrame("MediaPlayer");
		frame.setBounds(5, 5, 600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setVisible(true);

		JButton a = new JButton("Open File Mp3");
		frame.add(a);
		a.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chonFile = new JFileChooser();
				chonFile.showOpenDialog(null);
				File chonNhac = chonFile.getSelectedFile();
				String me = "file:///" + ("" + chonNhac).replace("\\", "/").replaceAll(" ", "%20");
				Media m = new Media(me);
				play = new MediaPlayer(m);
				ktra = 1;
				play.play();
				xuatLoi();

			}

		});

		JButton b = new JButton("Open File Excel");
		frame.add(b);
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e1) {
				JFileChooser chonFile = new JFileChooser();
				chonFile.showOpenDialog(null);
				File chonExcel = chonFile.getSelectedFile();
				String pathExcel = chonExcel.getAbsolutePath();
				playMusic.getDataFromExcel(pathExcel);
			}
		});

		table = new JTable(data, columnsname);
		table.setPreferredScrollableViewportSize(new Dimension(500, 300));
		table.setFillsViewportHeight(true);
		table.addMouseListener(new java.awt.event.MouseAdapter() {

			@SuppressWarnings("deprecation")
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());

				hangChon = row;
				thoigianbd = (String) data[row][0];
				thoigiankt1 = thoigiankt;
				Duration e = new Duration(timeString2milisecont(thoigianbd));
				Duration g = new Duration(timeString2milisecont(thoigiankt1));

				play.setStartTime(e);
				play.setStopTime(g);

				if (ktra == 1)
					play.stop();
				play.play();

				if (ktraThread == 1) {
					if (ttrinh1.isAlive()) {
						ttrinh1.stop();
						ttrinh2.start();
						ktraThread = 2;
					} else
						xuatLoi();
				} else if (ktraThread == 2) {
					ttrinh2.stop();
					xuatLoi();
				} else
					xuatLoi();
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		frame.add(scrollPane);

		c = new JTextField();
		frame.add(c);
		c.setColumns(50);

		frame.show(true);
	}

	private static double timeString2milisecont(String timeinString) {
		// String string = "004-034556";
		String[] parts = timeinString.split(":");
		String phut = parts[0]; // 004
		String giay = parts[1]; // 034556
		int intPhut = Integer.parseInt(phut);
		int intGiay = Integer.parseInt(giay);
		return intPhut * 60000 + intGiay * 1000;
	}

	protected static void getDataFromExcel(String link) {

		// TODO Auto-generated method stub
		File file = new File(link);
		try {
			Workbook wk = Workbook.getWorkbook(file);
			Sheet sheet = wk.getSheet(0);
			int rows = sheet.getRows();
			int cols = sheet.getColumns();
			data = new Object[rows][cols];
			hangCuoi = rows;
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					Cell cell = sheet.getCell(col, row);
					data[row][col] = cell.getContents();

				}
				String[] columnNames = { "Thoi Gian", "Loi bai hat" };
				model = new DefaultTableModel(data, columnNames);
				table.setModel(model);
			}
			wk.close();
		} catch (BiffException e1) {
		} catch (IOException e) {
		}
	}

	protected void xuatLoi() {
		// TODO Auto-generated method stub
		ttrinh1 = new Thread(new Runnable() {

			@Override
			public void run() {
				ktraThread = 1;
				for (int i = hangChon; i < hangCuoi; i++) {

					if (i == hangCuoi - 1) { //
						time1 = (String) data[i][0];
						time2 = thoigiankt;

					} else {
						time1 = (String) data[i][0];
						time2 = (String) data[i + 1][0];
					}
					c.setText((String) data[i][1]);
					// System.out.println(data[i][1]);
					try {

						Thread.sleep((long) (timeString2milisecont(time2) - timeString2milisecont(time1)));
					} catch (InterruptedException e) {

					}

				}

			}

		});
		ttrinh1.start();
		ttrinh2 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = hangChon; i < hangCuoi; i++) {

					if (i == hangCuoi - 1) { //
						time1 = (String) data[i][0];
						time2 = thoigiankt;

					} else {
						time1 = (String) data[i][0];
						time2 = (String) data[i + 1][0];
					}
					c.setText((String) data[i][1]);

					try {

						Thread.sleep((long) (timeString2milisecont(time2) - timeString2milisecont(time1)));
					} catch (InterruptedException e) {

					}
				}

			}

		});

	}

	public static void main(String[] args) {
		new JFXPanel();
		new playMusic();
	}
}
