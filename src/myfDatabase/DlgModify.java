package myfDatabase;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DlgModify extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5593956796843311742L;

	private final JPanel contentPanel = new JPanel();

	private boolean byes = false;
	private JTextPane textPane1;
	private JTextPane textPane2;
	private JTextPane textPane3;
	private JTextPane textPane4;
	private JTextPane textPane5;

	/**
	 * Create the dialog.
	 */
	public DlgModify(DlgModifyConstructorBag bag) {
		super(bag.cParent, bag.cValueBag == null ? "Add record." : "Modify record.", ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("ID");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textPane1 = new JTextPane();
			if (bag.cValueBag != null) textPane1.setText(bag.cValueBag.rID);
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(textPane1, gbc_lblNewLabel);
		}
		{
			JLabel lblNewLabel = new JLabel("Name");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textPane2 = new JTextPane();
			if (bag.cValueBag != null) textPane2.setText(bag.cValueBag.rName);
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.insets = new Insets(0, 0, 5, 0);
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 1;
			gbc_textPane.gridy = 1;
			contentPanel.add(textPane2, gbc_textPane);
		}
		{
			JLabel lblNewLabel = new JLabel("Age");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 2;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textPane3 = new JTextPane();
			if (bag.cValueBag != null) textPane3.setText(bag.cValueBag.rAge);
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.insets = new Insets(0, 0, 5, 0);
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 1;
			gbc_textPane.gridy = 2;
			contentPanel.add(textPane3, gbc_textPane);
		}
		{
			JLabel lblNewLabel = new JLabel("Birthday");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.BELOW_BASELINE;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 3;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textPane4 = new JTextPane();
			if (bag.cValueBag != null) textPane4.setText(bag.cValueBag.rBirthday);
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.insets = new Insets(0, 0, 5, 0);
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 1;
			gbc_textPane.gridy = 3;
			contentPanel.add(textPane4, gbc_textPane);
		}
		{
			JLabel lblNewLabel = new JLabel("Address");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 4;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			textPane5 = new JTextPane();
			if (bag.cValueBag != null) textPane5.setText(bag.cValueBag.rAddress);
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 1;
			gbc_textPane.gridy = 4;
			contentPanel.add(textPane5, gbc_textPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (isNumeric(textPane1.getText()) && isNumeric(textPane3.getText()) && isDateValid(textPane4.getText())) {
							byes = true;
							dispose();
						}
						else {
							JOptionPane.showMessageDialog(null, "Invalid values. Check again!");
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
						byes = false;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private static boolean isNumeric(String strNum) {
	    return strNum.matches("-?\\d+(\\.\\d+)?");
	}
	
	private static boolean isDateValid(String date) 
	{
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public DBValueBag Prompt() {
		setVisible(true);
		return new DBValueBag(byes, textPane1.getText(), textPane2.getText(), textPane3.getText(), textPane4.getText(), textPane5.getText());
	}

}
