package myfDatabase;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class DlgLogin extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870907471038846786L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgLogin dialog = new DlgLogin();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgLogin() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridBagLayout());
		{
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 0, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			gbc_panel.weightx = 0.2;
			gbc_panel.weighty = 1;
			contentPanel.add(panel, gbc_panel);
			{
				JPanel panel_1 = new JPanel(new GridBagLayout());
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				gbc_panel_1.weightx = 1;
				gbc_panel_1.weighty = 0.5;
				panel.add(panel_1, gbc_panel_1);
				{
					JLabel lblNewLabel = new JLabel("ID");
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.gridx = 0;
					gbc_lblNewLabel.gridy = 0;
					panel_1.add(lblNewLabel, gbc_lblNewLabel);
				}
			}
			{
				JPanel panel_1 = new JPanel(new GridBagLayout());
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				gbc_panel_1.weightx = 1;
				gbc_panel_1.weighty = 0.5;
				panel.add(panel_1, gbc_panel_1);
				{
					JLabel lblNewLabel_1 = new JLabel("Password");
					GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
					gbc_lblNewLabel_1.gridx = 0;
					gbc_lblNewLabel_1.gridy = 0;
					panel_1.add(lblNewLabel_1, gbc_lblNewLabel_1);
				}
			}
		}
		{
			JPanel panel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 1;
			gbc_panel.gridy = 0;
			gbc_panel.weightx = 0.8;
			gbc_panel.weighty = 1;
			contentPanel.add(panel, gbc_panel);
			{
				JPanel panel_1 = new JPanel(new GridBagLayout());
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.insets = new Insets(0, 0, 5, 0);
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 0;
				gbc_panel_1.weightx = 1;
				gbc_panel_1.weighty = 0.5;
				panel.add(panel_1, gbc_panel_1);
				{
					textField = new JTextField();
					panel_1.add(textField);
					textField.setColumns(10);
				}
			}
			{
				JPanel panel_1 = new JPanel(new GridBagLayout());
				GridBagConstraints gbc_panel_1 = new GridBagConstraints();
				gbc_panel_1.fill = GridBagConstraints.BOTH;
				gbc_panel_1.gridx = 0;
				gbc_panel_1.gridy = 1;
				gbc_panel_1.weightx = 1;
				gbc_panel_1.weighty = 0.5;
				panel.add(panel_1, gbc_panel_1);
				{
					textField_1 = new JPasswordField();
					GridBagConstraints gbc_textField_1 = new GridBagConstraints();
					gbc_textField_1.weightx = 1;
					gbc_textField_1.weighty = 0;
					panel_1.add(textField_1, gbc_textField_1);
					textField_1.setColumns(10);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							Class.forName("com.mysql.cj.jdbc.Driver");
							String url = "jdbc:mysql://localhost:3306/db_employee?characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=true";
							String user = "myfdbuser";
							String password = "myfdbpwd";
							Connection connection = DriverManager.getConnection(url, user, password);
							if(!connection.isClosed())
								System.out.println("Succeeded connecting to the Database!");
							Statement statement = connection.createStatement();
							String number=textField.getText();
							String sql="SELECT `pwd` FROM `tb_employee` WHERE eid=\""+number+"\";";
							ResultSet rs = statement.executeQuery(sql);
							boolean matched = false;
							rs.next();
							String pwd = rs.getString("pwd");
							if (pwd.equals(String.copyValueOf(textField_1.getPassword()))) {
								matched = true;
								WndDatabase.main(null);
								dispose();
							}
							if (!matched) {
								JOptionPane.showMessageDialog(null, "Login Failed!", "Information", JOptionPane.INFORMATION_MESSAGE);
							}
						}
						catch (Exception ex) {
							
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
