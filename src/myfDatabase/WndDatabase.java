package myfDatabase;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class WndDatabase extends JWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7605738632853659258L;
	private JFrame frame;
	private JTable table;
	private JTextField textField;
	private JRadioButton rdbtnID;
	private JRadioButton rdbtnName;
	private MysqlDataSource dataSource;
	private Connection conn;
	private Statement stmt;
	private Queue<String> queryQueue;
	
	private enum OperationType {
		Refresh,
		Add,
		Delete,
		Modify,
		Commit
	};
	
	private static class myTheme {
		public static Color LabelColor = new Color(23, 44, 60);
		public static Color BackgroundColor = new Color(39, 72, 98);
		public static Color ButtonColor = new Color(250, 218, 141);
		public static Color FontColor = Color.WHITE;
	};
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					WndDatabase window = new WndDatabase();
					window.frame.setVisible(true);
					window.UpdateRecord(OperationType.Refresh);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean UpdateRecord(OperationType type) {
		if (conn == null) {
			if (!InitDatabase()) {
				System.out.println("Cannot connect to database!");
				return false;
			}
		}
		try {
			if (stmt == null) {
				stmt = conn.createStatement();
			}
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			
			switch (type) {
				case Refresh:
					model.setRowCount(0);
					ResultSet rs = stmt.executeQuery("SELECT * FROM `tb_employee`;");
					while (rs.next()) {
						String[] rc = new String[5];
						rc[0] = rs.getString("eid");
						rc[1] = rs.getString("ename");
						rc[2] = rs.getString("eage");
						rc[3] = rs.getString("ebirthday").split(" ")[0].replace("-", "");
						rc[4] = rs.getString("eaddress");
						model.addRow(rc);
					};
					rs.close();
					break;
				case Add:
					DlgModify dlga = new DlgModify(new DlgModifyConstructorBag(null, frame));
					DBValueBag rtna = dlga.Prompt();
					if (rtna.byes) {
						queryQueue.add("INSERT INTO `tb_employee` (`eid`, `ename`, `eage`, `ebirthday`, `eaddress`) VALUES (\"" +
								rtna.rID + "\", \"" +
								rtna.rName + "\", \"" +
								rtna.rAge + "\", \"" +
								rtna.rBirthday + "\", \"" +
								rtna.rAddress + "\");");
						model.addRow(new String[] {rtna.rID, rtna.rName, rtna.rAge, rtna.rBirthday, rtna.rAddress});
					}
					break;
				case Delete:
					int i = table.getSelectedRow();
					if (i >= 0) {
						String strid = (String) table.getValueAt(i, 0);
						if (JOptionPane.showConfirmDialog(null, "Delete this record?\nID:" + strid + ", Name:" + (String) table.getValueAt(i, 1)) == JOptionPane.YES_OPTION) {
							queryQueue.add("DELETE FROM `tb_employee` WHERE `eid`=" + strid + ";");
							model.removeRow(i);
						}
					}
					break;
				case Modify:
					int id = table.getSelectedRow();
					if (id < 0) break;
					DlgModify dlgm = new DlgModify(new DlgModifyConstructorBag(new DBValueBag(false, (String) model.getValueAt(id, 0), (String) model.getValueAt(id, 1), (String) model.getValueAt(id, 2), (String) model.getValueAt(id, 3), (String) model.getValueAt(id, 4)), frame));
					DBValueBag rtnm = dlgm.Prompt();
					if (rtnm.byes) {
						queryQueue.add("UPDATE `tb_employee` SET `ename`=\"" + rtnm.rName + 
								"\", `eage`=\"" + rtnm.rAge + 
								"\", `ebirthday`=\"" + rtnm.rBirthday + 
								"\", `eaddress`=\"" + rtnm.rAddress + "\" WHERE `eid`=" + rtnm.rID + ";");
						model.setValueAt(rtnm.rName, id, 1);
						model.setValueAt(rtnm.rAge, id, 2);
						model.setValueAt(rtnm.rBirthday, id, 3);
						model.setValueAt(rtnm.rAddress, id, 4);
					}
					break;
				case Commit:
					DlgReviewCommit dlgrc = new DlgReviewCommit(frame, queryQueue);
					boolean rtnrc = dlgrc.Prompt();
					if (rtnrc) {
						String[] strcommits = queryQueue.toArray(String[]::new);
						for (String s:strcommits) {
							stmt.execute(s);
						}
						queryQueue.remove();
						UpdateRecord(OperationType.Refresh);
					}
					break;
				default:
					break;
			}
			
			table.revalidate();
		} catch (Exception e) {
			CloseDatabase();
			System.out.println("Cannot communicate with database or SQL error!\n" + e.getLocalizedMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * Create the application.
	 */
	public WndDatabase() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 400);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("MYF Database - 171340236 çÑÓêö­");
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(true);
		panel.setBackground(myTheme.BackgroundColor);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
				
		JPanel panel_op = new JPanel();
		panel_op.setLayout(new GridLayout(1, 1, 0, 0));
		panel_op.setBorder(new EmptyBorder(5, 5, 10, 5));
		panel_op.setOpaque(false);
		GridBagConstraints gbc_panel_op = new GridBagConstraints();
		gbc_panel_op.fill = GridBagConstraints.BOTH;
		gbc_panel_op.gridx = 0;
		gbc_panel_op.gridy = 0;
		gbc_panel_op.weightx = 1;
		gbc_panel_op.weighty = 0.1;
		panel.add(panel_op, gbc_panel_op);
		
		
		JPanel panel_table = new JPanel();
		panel_table.setLayout(new GridBagLayout());
		panel_table.setBorder(new EmptyBorder(0, 5, 10, 5));
		panel_table.setOpaque(false);
		GridBagConstraints gbc_panel_table = new GridBagConstraints();
		gbc_panel_table.fill = GridBagConstraints.BOTH;
		gbc_panel_table.gridx = 0;
		gbc_panel_table.gridy = 1;
		gbc_panel_table.weightx = 1;
		gbc_panel_table.weighty = 0.9;
		panel.add(panel_table, gbc_panel_table);
		
		{
			JPanel panel_ops = new JPanel(new GridLayout(2, 1, 0, 0));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			rdbtnID = new JRadioButton("ID");
			rdbtnName = new JRadioButton("Name");
			rdbtnID.setSelected(true);
			rdbtnID.setOpaque(false);
			rdbtnID.setForeground(myTheme.FontColor);
			rdbtnID.setBackground(myTheme.BackgroundColor);
			rdbtnName.setSelected(false);
			rdbtnName.setOpaque(false);
			rdbtnName.setForeground(myTheme.FontColor);
			rdbtnName.setBackground(myTheme.BackgroundColor);
			ButtonGroup buttonGroup = new ButtonGroup();
			buttonGroup.add(rdbtnID);
			buttonGroup.add(rdbtnName);
			panel_ops.add(rdbtnID);
			panel_ops.add(rdbtnName);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.insets = new Insets(0, 0, 0, 5);
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 0;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.10;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		{
			JPanel panel_ops = new JPanel(new GridBagLayout());
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 0, 5);
			gbc_textField.fill = GridBagConstraints.BOTH;
			gbc_textField.gridx = 0;
			gbc_textField.gridy = 0;
			gbc_textField.weightx = 1;
			gbc_textField.weighty = 0;
			panel_ops.add(textField, gbc_textField);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.insets = new Insets(0, 0, 0, 5);
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 1;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		{
			JPanel panel_ops = new JPanel(new GridLayout(1, 1, 0, 0));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			JButton btn = new JButton("Search");
			btn.setBackground(myTheme.ButtonColor);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String str = textField.getText().trim().toLowerCase();
					boolean matched = false;
					if (str.length() <= 0) return;
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					for (int i = 0; i < model.getRowCount(); i++) {
						String val = rdbtnID.isSelected() ? (String) model.getValueAt(i, 0) : ((String) model.getValueAt(i, 1)).toLowerCase();
						if (val.equals(str)) {
							JOptionPane.showMessageDialog(null, "Found item:\nID:" + (String) model.getValueAt(i, 0) + ", Name: " + (String) model.getValueAt(i, 1), "Information", JOptionPane.INFORMATION_MESSAGE);
							table.setRowSelectionInterval(i, i);
							matched = true;
							break;
						}
					}
					if (!matched) {
						JOptionPane.showMessageDialog(null, "No record matches query.", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					textField.grabFocus();
				}
			});
			panel_ops.add(btn);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.insets = new Insets(0, 0, 0, 5);
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 2;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		{
			JPanel panel_ops = new JPanel(new GridLayout(1, 1, 0, 0));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			JButton btn = new JButton("Add");
			btn.setBackground(myTheme.ButtonColor);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UpdateRecord(OperationType.Add);
				}
			});
			panel_ops.add(btn);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.insets = new Insets(0, 0, 0, 5);
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 3;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		{
			JPanel panel_ops = new JPanel(new GridLayout(1, 1, 0, 0));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			JButton btn = new JButton("Delete");
			btn.setBackground(myTheme.ButtonColor);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UpdateRecord(OperationType.Delete);
				}
			});
			panel_ops.add(btn);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 4;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}

		{
			JPanel panel_ops = new JPanel(new GridLayout(1, 1, 0, 0));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			JButton btn = new JButton("Modify");
			btn.setBackground(myTheme.ButtonColor);
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					UpdateRecord(OperationType.Modify);
				}
			});
			panel_ops.add(btn);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 5;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		{
			JPanel panel_ops = new JPanel(new GridLayout(2, 1, 0, 5));
			panel_ops.setBorder(new EmptyBorder(5, 5, 5, 5));
			panel_ops.setOpaque(true);
			panel_ops.setBackground(myTheme.LabelColor);
			JButton btn1 = new JButton("Commit");
			btn1.setBackground(myTheme.ButtonColor);
			btn1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (queryQueue.size() == 0) {
						JOptionPane.showMessageDialog(null, "Nothing to commit.", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						UpdateRecord(OperationType.Commit);
					}
				}
			});
			panel_ops.add(btn1);
			JButton btn2 = new JButton("Reset");
			btn2.setBackground(myTheme.ButtonColor);
			btn2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					queryQueue.clear();
					UpdateRecord(OperationType.Refresh);
				}
			});
			panel_ops.add(btn2);
			GridBagConstraints gbc_panel_ops = new GridBagConstraints();
			gbc_panel_ops.fill = GridBagConstraints.BOTH;
			gbc_panel_ops.gridx = 6;
			gbc_panel_ops.gridy = 0;
			gbc_panel_ops.weightx = 0.15;
			gbc_panel_ops.weighty = 1;
			panel_op.add(panel_ops, gbc_panel_ops);
		}
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setBackground(myTheme.BackgroundColor);
		table.setForeground(myTheme.FontColor);
		DefaultTableModel model = new DefaultTableModel() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 4096185378892580824L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		table.setModel(model);
		String[] tcname = {"ID", "Name", "Age", "Birthday", "Address"};
		for (int i = 0; i < 5; i++) {
			model.addColumn(tcname[i]);
		}
		
		JTableHeader tbheader = table.getTableHeader();
		tbheader.setOpaque(true);
		tbheader.setBackground(myTheme.LabelColor);
		tbheader.setForeground(myTheme.FontColor);
		GridBagConstraints gbc_tableheader = new GridBagConstraints();
		gbc_tableheader.fill = GridBagConstraints.BOTH;
		gbc_tableheader.gridx = 0;
		gbc_tableheader.gridy = 0;
		gbc_tableheader.weightx = 1;
		gbc_tableheader.weighty = 0.05;
		panel_table.add(tbheader, gbc_tableheader);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		gbc_table.weightx = 1;
		gbc_table.weighty = 0.95;
		panel_table.add(table, gbc_table);
		
		queryQueue = new LinkedList<String>();
		
	}

	private boolean InitDatabase() {
		CloseDatabase();
		if (dataSource == null) {
			/*
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (Exception e) {
				return false;
			}
			*/
			dataSource = new MysqlDataSource();
		}
		else {
			dataSource.reset();
		}
		dataSource.setUser("myfdbuser");
		dataSource.setPassword("myfdbpwd");
		dataSource.setServerName("localhost");
		dataSource.setDatabaseName("db_employee");
		try {
			conn = dataSource.getConnection();
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private boolean CloseDatabase() {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
