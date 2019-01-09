package myfDatabase;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import java.util.LinkedList;
import java.util.Queue;

public class DlgReviewCommit extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 986363925893328515L;

	private final JPanel contentPanel = new JPanel();

	private JTextPane textPane;
	private JList<String> list;
	private DefaultListModel<String> model;
	
	private boolean byes = false;

	/**
	 * Create the dialog.
	 */
	public DlgReviewCommit(Window parent, Queue<String> commits) {
		super(parent, "Review Commits", ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		if (commits == null) {
			commits = new LinkedList<String>();
		}
		else {
			commits = new LinkedList<String>(commits);
		}
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel("Commit to the database?");
			lblNewLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			gbc_lblNewLabel.weightx = 1;
			gbc_lblNewLabel.weighty = 0.1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			model = new DefaultListModel<String>();
			String[] strcommits = commits.toArray(String[]::new);
			for (String s:strcommits) {
				model.addElement(s);
			}
			list = new JList<String>(model);
			list.addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					textPane.setText(list.getSelectedValue());
				}
			});
			GridBagConstraints gbc_list = new GridBagConstraints();
			gbc_list.insets = new Insets(0, 0, 5, 0);
			gbc_list.fill = GridBagConstraints.BOTH;
			gbc_list.gridx = 0;
			gbc_list.gridy = 1;
			gbc_list.weightx = 1;
			gbc_list.weighty = 0.7;
			contentPanel.add(list, gbc_list);
		}
		{
			textPane = new JTextPane();
			GridBagConstraints gbc_textPane = new GridBagConstraints();
			gbc_textPane.fill = GridBagConstraints.BOTH;
			gbc_textPane.gridx = 0;
			gbc_textPane.gridy = 2;
			gbc_textPane.weightx = 1;
			gbc_textPane.weighty = 0.2;
			contentPanel.add(textPane, gbc_textPane);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						byes = true;
						dispose();
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
	
	public boolean Prompt() {
		setVisible(true);
		return byes;
	}

}
