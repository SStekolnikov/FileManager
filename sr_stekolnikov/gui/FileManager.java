package sr_stekolnikov.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;

import sr_stekolnikov.logic.SearcherWord;

public class FileManager {
	int lock = 0;

	private JPanel root;
	private TextViewer viewer;

	private File file;

	private String extension;
	private String direction;
	private String searchWord;
	private static boolean openFile;
	private static boolean notEmptyTree;

	private JPanel leftSide;
	private JPanel rightSide;
	private JPanel searchPanel;

	private JSplitPane jsp;
	private JButton searching;

	private JTextField userDirectory;
	private JTextField userSearchWord;
	private JTextField userExtension;

	Dimension size;

	public FileManager() {
		viewer = new TextViewer("");
		extension = ".log";
		direction = "";
		searchWord = "";
		openFile = false;
		notEmptyTree = false;
		userDirectory = new JTextField("", 15);
		userSearchWord = new JTextField("", 20);
		userExtension = new JTextField("", 10);
	}

	/*
	 * Метод отвечает за создание панели, разделенной на две части - Проводник для
	 * поиска(левая часть) и Text viewer
	 * 
	 * @return JSplitPane - with rightSide and LeftSide of window
	 */
	public JSplitPane createStartPanel(Dimension size) {
		this.size = size;
		jsp = new JSplitPane();
		jsp.setMinimumSize(size);

		leftSide = new JPanel();
		leftSide.setPreferredSize(new Dimension(400, size.height));
		// leftSide.setLayout(null);
		leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));

		JPanel dir = createDirPanel();
		// dir.setBounds(2, 2, 396, 25);
		leftSide.add(dir);
		leftSide.add(Box.createRigidArea(new Dimension(400, 10)));

		JPanel tree = createTreePanel();
		tree.setPreferredSize(new Dimension(400, 900));
		// tree.setBounds(2, 30, 398, size.height - 120);
		leftSide.add(tree);
		leftSide.add(Box.createRigidArea(new Dimension(400, 50)));

		JPanel parametres = createSearchingParametresPanel();
		leftSide.add(Box.createRigidArea(new Dimension(400, 5)));
		// parametres.setBounds(2, size.height - 75, 396, 65);
		leftSide.add(parametres);

//makes a right side of JFrame in Start
		viewer = new TextViewer("");
		rightSide = new JPanel();
		rightSide = viewer.createRightSide(jsp, openFile); // false - showing instruction
		// rightSide.setMinimumSize(new Dimension(600, 600));
		rightSide.setPreferredSize(new Dimension(600, size.height));

//divide a pane for leftSide and rightSide
		jsp = new JSplitPane();
		jsp.setDividerLocation(400);
		jsp.setLeftComponent(leftSide);
		jsp.setRightComponent(rightSide);
		jsp.setEnabled(false);
		return jsp;
	}

	/*
	 * Метод отвечает за создание панели, отображающейся во время поиска файлов
	 * 
	 * @return JPanel - with ProgressBar during searching
	 */
	private JPanel createSearchingLeftPanel() {
		JPanel leftSideInSearchTime = new JPanel();
		leftSideInSearchTime.setLayout(new BorderLayout());

		JPanel dir = createDirPanel();
		leftSideInSearchTime.add(dir, BorderLayout.NORTH);

		// panel for progressBar
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		progressPanel.setPreferredSize(new Dimension(400, 65));
		progressPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		;

		// Label for progressBar
		progressPanel.add(new JLabel("Выполняется поиск файлов соответствующих вашему запросу..."));

		// progressBar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(400, 20));
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar);

		leftSideInSearchTime.add(progressPanel, BorderLayout.CENTER);

		// SearchPanel
		JPanel parametres = createSearchingParametresPanel();
		leftSideInSearchTime.add(parametres, BorderLayout.SOUTH);

		return leftSideInSearchTime;
	}

	/**
	 * Метод отвечает за создание панели, отображающейся во время загрузки текста
	 * файла
	 * 
	 * @return JPanel - RightSide
	 */
	private JPanel createSearchingRightPanel() {
		JPanel rightSideInSearchTime = new JPanel();
		rightSideInSearchTime.setLayout(new BorderLayout());
		rightSideInSearchTime.setMinimumSize(new Dimension(300, 500));

//add components			
		// panel for progressBar
		JPanel progressPanel = new JPanel();
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
		progressPanel.setPreferredSize(new Dimension(rightSideInSearchTime.getSize().width, 65));
		progressPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		// Label for progressBar
		progressPanel.add(new JLabel("Выполняется открытие выбранного файла"));

		// progressBar
		JProgressBar progressBar = new JProgressBar();
		progressBar.setPreferredSize(new Dimension(rightSideInSearchTime.getSize().width, 20));
		progressBar.setIndeterminate(true);
		progressPanel.add(progressBar);

		// progressPanel

		rightSideInSearchTime.add(progressPanel, BorderLayout.CENTER);

		// inputPanel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Horizontally

		JButton end = new JButton("To end");
		inputPanel.add(end);// button to the end of doc

		JButton start = new JButton("To start");
		inputPanel.add(start);// button to start of doc

		rightSideInSearchTime.add(inputPanel, BorderLayout.SOUTH);

		return rightSideInSearchTime;
	}

	/**
	 * Метод отвечает за создание JPanel для leftSide, отображающей параметры для
	 * поиска
	 * 
	 * @return JPanel - part of leftSide c параметрами поиска
	 */
	private JPanel createSearchingParametresPanel() {
		searchPanel = new JPanel();
		searchPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		JLabel sw = new JLabel("Word for searching");
		constraints.gridx = 0;
		constraints.gridy = 0;
		searchPanel.add(sw, constraints);

		constraints.gridx = 0; // table -столбец
		constraints.gridy = 1; // row
		searchPanel.add(userSearchWord, constraints);

		JLabel ext = new JLabel("Extension");
		constraints.gridx = 1; // table -столбец
		constraints.gridy = 0; // row
		searchPanel.add(ext, constraints);

		constraints.gridx = 1; // table -столбец
		constraints.gridy = 1; // row
		searchPanel.add(userExtension, constraints);

		searching = new JButton("searching");
		constraints.gridx = 0; // table -столбец
		constraints.gridy = 2; // row
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		searchPanel.add(searching, constraints);

		searching.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// reaction
				if (userExtension.getText().equals(""))
					setExtension(".log"); // default extension
				else {
					if (userExtension.getText().matches("\\.+[A-Za-z0-9]*")) // use a RegExp for check
						setExtension(userExtension.getText());
					else {
						JOptionPane.showMessageDialog(root, "Недопустимое значение поля 'Extension' - пример: .txt");
						return;
					}
				}
				setDirection(userDirectory.getText()); // if empty - will be scanning of root path

				setSearchWord(userSearchWord.getText());

				notEmptyTree = true;

				if (lock == 1) {
					JOptionPane.showMessageDialog(root,
							"В данный момент происходит поиск по другому запросу, результаты данного поиска будут доступны позже.");
				}
				// left side during the searching
				JPanel temperary = createSearchingLeftPanel();
				jsp.setLeftComponent(temperary);
				jsp.revalidate();
				// start of searching
				Thread searchingThread = null;
				searchingThread = new Thread(new Runnable() {
					public void run() { // another thread
						lock = 1;

						JPanel temp = new JPanel();
						temp.setPreferredSize(new Dimension(400, size.height));
						// leftSide.setLayout(null);
						temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));

						JPanel dir = createDirPanel();
						// dir.setBounds(2, 2, 396, 25);
						temp.add(dir);
						temp.add(Box.createRigidArea(new Dimension(400, 10)));

						JPanel tree = createTreePanel();
						tree.setPreferredSize(new Dimension(400, 900));
						// tree.setBounds(2, 30, 398, size.height - 120);
						temp.add(tree);
						temp.add(Box.createRigidArea(new Dimension(400, 50)));

						JPanel parametres = createSearchingParametresPanel();
						temp.add(Box.createRigidArea(new Dimension(400, 5)));
						// parametres.setBounds(2, size.height - 75, 396, 65);
						temp.add(parametres);

						lock = 0;

						jsp.setLeftComponent(temp);
						jsp.revalidate();
					}
				});
				searchingThread.start();
			}
		});
		return searchPanel;
	}

	/**
	 * Метод отвечает за создание JPanel для leftSide, отображающей найденные файлы
	 * 
	 * @return JPanel - part of leftSide c файловым древом
	 */
	private JPanel createTreePanel() {
		JPanel returning = new JPanel();
		returning.setMinimumSize(new Dimension(400, 300));
		returning.setLayout(new GridLayout());
		// создать модель'tree'
		TreeModel model = createTreeModel();
		JTree tree1 = new JTree(model);
		// способ выделения - SINGLE
		tree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		// TreeSelectionListener в последнем tree
		tree1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1.getLastSelectedPathComponent();
				if (e == null) {
					JOptionPane.showMessageDialog(null, "No location selected for new file.", "Select Location",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				Object nodeInfo = node.getUserObject();
				String chosenFile = nodeInfo.toString();
				this.openFile(chosenFile);
				// pack();
				returning.revalidate();
			}

			// show doc at right pane
			private void openFile(String chosenFile) {
				file = new File(chosenFile);
				if (file.canRead()) {
					openFile = true;
					new Thread(new Runnable() {
						public void run() {
							jsp.setRightComponent(createSearchingRightPanel());
							viewer = null;
							viewer = new TextViewer(file.getAbsolutePath());
							jsp.setRightComponent(viewer.createRightSide(jsp, openFile));
							jsp.revalidate();
						}
					}).start();
				} else
					;// showThrowable(new Throwable("Файл не может быть прочитан."));//do smthng
			}
		});
		tree1.setVisibleRowCount(20);
		JScrollPane test = new JScrollPane(tree1);
		returning.add(test);
		return returning;
	}

	/**
	 * Метод отвечает за создание JPanel для leftSide, отображающей Параметры
	 * директории
	 * 
	 * @return JPanel - part of leftSide c директорией
	 */
	private JPanel createDirPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // X_Axis will arrange the content
																			// horizontally
		JLabel label = new JLabel("Directory:");
		inputPanel.add(label);
		inputPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		userDirectory = new JTextField();
		inputPanel.add(userDirectory); // after(to the left) - will be the textField
		// inputPanel.add(new JButton("goTo")); // and then a button "goTo"
		inputPanel.setMaximumSize(new Dimension(400, 30));
		return inputPanel;
	}

	public void showThrowable(Throwable t) {
		JOptionPane.showMessageDialog(null, t.toString(), t.getMessage(), JOptionPane.ERROR_MESSAGE);
		// repaint();
	}

	/*
	 * Метод отвечает за создание древовидной структуры(модели) (notEmptyTree)?
	 * return results of searching : return empty tree
	 * 
	 * @return TreeModel - для части leftSiie
	 */
	private TreeModel createTreeModel() {
		// root of tree
		DefaultMutableTreeNode manager = new DefaultMutableTreeNode("Manager");
		// directory:
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(this.direction);
		manager.add(root);
		DefaultMutableTreeNode lang = new DefaultMutableTreeNode("Files by '" + this.extension + "' extension:");
		root.add(lang);

		// добавить leaves
		if (notEmptyTree) {
			// initialization from search results
			SearcherWord sw = new SearcherWord();
			sw.setDirectory(this.direction);
			sw.setExtension(this.extension);
			sw.setSearch_word(this.searchWord);

			List<String> sear = new ArrayList<>();
			try {
				sear.addAll(sw.searching());
			} catch (IOException exc) {
				showThrowable(exc);
			}
			String[] leaves = new String[sear.size()];
			for (int i = 0; i < leaves.length; i++)
				leaves[i] = sear.get(i);

			for (int i = 0; i < leaves.length; i++)
				lang.add(new DefaultMutableTreeNode(leaves[i]));
			return new DefaultTreeModel(root);
		} else {
			// добавить empty leaf
			lang.add(new DefaultMutableTreeNode(""));

			return new DefaultTreeModel(root);
		}
	}

//setters for Ext, Dir, SearchWord
	private void setExtension(String extension) {
		this.extension = extension;
	}

	private void setDirection(String direction) {
		this.direction = direction;
	}

	private void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

}