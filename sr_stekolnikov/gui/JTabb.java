package sr_stekolnikov.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class JTabb extends JFrame {
	static int counter = 0;

	public JTabb() {
		super("File Manager ");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1100, 600));
		setMinimumSize(new Dimension(1100, 600));
		// панель с вкладками
		setLayout(new GridLayout(1, 1));
		final JTabbedPane tabs = new JTabbedPane();

		FileManager manager = new FileManager();
		JSplitPane panel1 = manager.createStartPanel(new Dimension(900, 500));
		tabs.addTab("Tab 1", panel1);
		counter++;

		FileManager manager2 = new FileManager();
		JSplitPane panel2 = manager2.createStartPanel(new Dimension(900, 500));
		tabs.addTab("Tab 2", panel2);
		counter++;

		JButton add = new JButton("Add new tab.");
		// add.setFont(font);
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileManager m = new FileManager();
				JSplitPane j = m.createStartPanel(new Dimension(900, 500));
				tabs.addTab("Tab " + ++counter, j);
			}
		});

		// TODO add a button 'close a tab'
		tabs.addTab("Add Tab", add);
		// выводим окно на экран
		add(tabs);
		setVisible(true);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JTabb();
			}
		});
	}
}