package aioWoodcutter;

import java.awt.Container;
import javax.swing.LayoutStyle;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JFrame;

public class AIOWoodcutterGUI extends JFrame {
	private boolean loadValues, bankLogs, lootNests, afkMode;
	private int minSleep, maxSleep;
	private JLabel labelTitle;
	private JCheckBox checkBoxBankLogs;
	private JCheckBox checkBoxLootNests;
	private JCheckBox checkBoxAfkMode;
	private JLabel labelMinSleep;
	private JSpinner spinnerMinSleep;
	private JLabel labelMaxSleep;
	private JSpinner spinnerMaxSleep;
	private JButton buttonLoad;

	public AIOWoodcutterGUI() {
		initComponents();
	}

	public boolean shouldLoadValues() {
		return loadValues;
	}

	public boolean shouldBankLogs() {
		return bankLogs;
	}

	public boolean shouldLootNests() {
		return lootNests;
	}

	public boolean isAfkMode() {
		return afkMode;
	}

	public int getMinSleep() {
		return minSleep;
	}

	public int getMaxSleep() {
		return maxSleep;
	}

	public void setLoadValues(boolean loadValues) {
		this.loadValues = loadValues;
	}

	private void buttonLoadActionPerformed(final ActionEvent e) {
		bankLogs = checkBoxBankLogs.isSelected();
		lootNests = checkBoxLootNests.isSelected();
		afkMode = checkBoxAfkMode.isSelected();
		minSleep = (int) spinnerMinSleep.getValue();
		maxSleep = (int) spinnerMaxSleep.getValue();
		loadValues = true;
		dispose();
	}

	private void checkBoxAfkModeActionPerformed(final ActionEvent e) {
		boolean enabled = checkBoxAfkMode.isSelected();
		labelMinSleep.setEnabled(enabled);
		labelMaxSleep.setEnabled(enabled);
		spinnerMinSleep.setEnabled(enabled);
		spinnerMaxSleep.setEnabled(enabled);
	}

	private void initComponents() {
		labelTitle = new JLabel();
		checkBoxBankLogs = new JCheckBox();
		checkBoxLootNests = new JCheckBox();
		checkBoxAfkMode = new JCheckBox();
		labelMinSleep = new JLabel();
		spinnerMinSleep = new JSpinner();
		labelMaxSleep = new JLabel();
		spinnerMaxSleep = new JSpinner();
		buttonLoad = new JButton();
		setTitle("AIO Woodcutter");
		Container contentPane = getContentPane();
		
		labelTitle.setText("Fury AIO Woodcutter");
		labelTitle.setHorizontalAlignment(0);
		labelTitle.setFont(labelTitle.getFont().deriveFont(labelTitle.getFont().getSize() + 2.0f));
		
		checkBoxBankLogs.setText("Bank Logs?");
		
		checkBoxLootNests.setText("Loot Bird Nests?");
		
		checkBoxAfkMode.setText("AFK Mode?");
		checkBoxAfkMode.addActionListener(e -> checkBoxAfkModeActionPerformed(e));
		
		labelMinSleep.setText("Min Sleep (ms):");
		labelMinSleep.setEnabled(false);
		
		spinnerMinSleep.setEnabled(false);
		
		labelMaxSleep.setText("Max Sleep (ms):");
		labelMaxSleep.setEnabled(false);
		
		spinnerMaxSleep.setEnabled(false);
		
		buttonLoad.setText("LOAD");
		buttonLoad.addActionListener(e -> buttonLoadActionPerformed(e));
		
		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(contentPaneLayout.createParallelGroup().addComponent(labelTitle, -1, -1, 32767)
						.addComponent(buttonLoad, -1, -1, 32767)
						.addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout
								.createParallelGroup().addComponent(checkBoxAfkMode)
								.addGroup(contentPaneLayout.createSequentialGroup().addComponent(checkBoxBankLogs)
										.addGap(22, 22, 22).addComponent(checkBoxLootNests)))
								.addGap(0, 0, 32767))
						.addGroup(contentPaneLayout.createSequentialGroup()
								.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addComponent(labelMinSleep, -1, -1, 32767)
										.addComponent(labelMaxSleep, -1, -1, 32767))
								.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(spinnerMaxSleep).addComponent(spinnerMinSleep, -2, 117, -2))))
				.addContainerGap()));
		contentPaneLayout.setVerticalGroup(contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup().addContainerGap()
						.addComponent(labelTitle, -2, 14, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(contentPaneLayout.createParallelGroup().addComponent(checkBoxLootNests)
								.addComponent(checkBoxBankLogs))
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(checkBoxAfkMode)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(labelMinSleep, -2, 20, -2).addComponent(spinnerMinSleep, -2, -1, -2))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(labelMaxSleep, -2, 20, -2).addComponent(spinnerMaxSleep, -2, -1, -2))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(buttonLoad)
						.addContainerGap(6, 32767)));
		pack();
		setLocationRelativeTo(getOwner());
	}
}
