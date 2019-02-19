package sr_stekolnikov.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 *  ласс, реализующий Text Viewer, выбранного в FileManager файла
 * 
 * @autor Sr Stekolnikov
 * @version 1.0
 */
public class TextViewer {
	private static final ReadWorker readWorker = new ReadWorker();
	private static File file;
	private static int counterOfStrings = 0;

	static private JTextArea text;
	private JPanel root;
	private JScrollBar scrollBar;

	private static final String INSTRUCTION_TEXT = "\t\tInstruction. \n" + "Task: \n"
			+ "Ќаписать программу дл€ поиска заданного текста в лог файлах. \n" + "Solution:\n"
			+ " 1. ѕри нажатии кнопки 'searching' считываютс€ значени€ всех трех полей,\n"
			+ " 2. ѕроисходит поиск файлов нужного расширени€. \n"
			+ " 3. «атем: ≈сли слово указано - поиск слова в полученных файлах,\n"
			+ "		≈сли слово не указано - отображаютс€ все найденные файлы\n" + "\n" + "≈сли не указан параметр: \n"
			+ "- Ќе указано расширение - поиск расширени€ по умолчанию(.log).\n"
			+ "- Ќе указан путь - поиск файлов в корневом каталоге машины.\n"
			+ "- Ќе указано слово - поиск файлов, подход€щих по остальным параметрам.\n"
			+ "≈сли не указаны все параметры - Default исполнение:\n"
			+ "	- —овокупность сценариев при не указанных параметрах.\n" + "ќриентирована на кодировку UTF-8\n" + "\n"
			+ "" + "" + "Author: Sr.Stekolnikov (sr.stekolnikov@gmail.com)";

	public TextViewer(String absPath) {
		file = new File(absPath);
		text = new JTextArea();

	}

	/*
	 * ћетод отвечает за создание панели - Text viewer
	 * 
	 * @return JSplitPane - with rightSide and LeftSide of window
	 */
	public JPanel createRightSide(JSplitPane jsp, boolean choice) {
		root = new JPanel();
		root.setMinimumSize(new Dimension(300, 300));
		root.setSize(new Dimension(jsp.getSize().width - 400, jsp.getSize().height));
		root.setLayout(new BorderLayout());

		text = new JTextArea();

		if (choice == false) {
			text.setText(INSTRUCTION_TEXT);
			Font font = new Font("Verdana", Font.BOLD, 12); // set format
			text.setFont(font); // format a text
			text.setEditable(false);

			JScrollPane scroll = new JScrollPane(text);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll.setPreferredSize(new Dimension(root.getSize().width - 25, root.getSize().height - 50));

			root.add(scroll, BorderLayout.CENTER);
		} else {
			// когда программа запустилась нам нужны строки начина€ с первой
			// thread дл€ отображени€ начала файла
			new Thread(new Runnable() {
				public void run() {
					readWorker.changeIndex(1);
					stringsCounter();
					System.out.println(counterOfStrings);

				}
			}).start();
			// horizontal scroll for text
			JScrollPane horizontalScroll = new JScrollPane(text);
			horizontalScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
			horizontalScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			horizontalScroll.setPreferredSize(new Dimension(root.getSize().width - 25, root.getSize().height - 50));

			// add ScrollBar for vertical moving text
			scrollBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, stringsCounter());// max is string count
			scrollBar.setMaximumSize(new Dimension(20, 5000));

			// listener for position at text
			scrollBar.addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent e) {
					// System.out.println(e.getValue()); for test
					readWorker.changeIndex(e.getValue());
				}
			});

			root.add(horizontalScroll, BorderLayout.CENTER);
			root.add(scrollBar, BorderLayout.EAST);
		}

		// inputPanel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS)); // Horizontally

		/*
		 * TODO JButton light = new JButton("Light"); inputPanel.add(light); //button
		 * for lighting SearchWord - not done
		 */

		/*
		 * TODO JButton end = new JButton("To end"); inputPanel.add(end);// button to
		 * the end of doc
		 */
		JButton start = new JButton("To start");
		inputPanel.add(start);// button to start of doc

		root.add(inputPanel, BorderLayout.SOUTH);

		// listeners for Jbuttons
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set caret at the start of text
				text.grabFocus();
				readWorker.changeIndex(1);
				root.revalidate();
			}
		});

		return root;
	}

	private static class ReadWorker implements Runnable {
		private final AtomicInteger startLineIndex = new AtomicInteger(0);
		private final AtomicBoolean workingState = new AtomicBoolean(false);

		public ReadWorker() {
			final Thread workerThread = new Thread(this);
			workerThread.start();
		}

		public void changeIndex(int newIndex) {
			startLineIndex.set(newIndex);

			// если сейчас воркер не работает - будим его
			notifyToWork(newIndex);
		}

		private synchronized void notifyToWork(int newIndex) {
			// вызываем notify, чтобы прервать wait в функции .run() когда по€вилась работа
			// но делаем это только если на данный момент работа уже не выполн€етс€
			if (workingState.compareAndSet(false, true)) {
				notify();
			}
		}

		@Override
		public synchronized void run() {
			while (true) {
				// если работы нету - ждем пока она по€витс€
				if (!workingState.get()) {
					try {
						wait();
					} catch (InterruptedException e) {
					}
				}

				final List<String> resultLineList = new ArrayList<>();

				// пытаемс€ прочитать пока не получим нужные строки
				while (resultLineList.isEmpty()) {
					try {
						readLines(resultLineList);
					} catch (IndexChangedException ice) {
						// если во врем€ чтени€ изменилс€ индекс - удал€ем полученные строки
						// и цикл `while (resultLineList.isEmpty())` начнет новую итерацию
						resultLineList.clear();
					}
				}

				// записываем полученные строки в GUI
				text.setText("");
				for (String string : resultLineList)
					text.append(string + "\n");
				// отмечаем что работы больше нет
				workingState.set(false);
			}
		}

		private void readLines(List<String> resultLineList) throws IndexChangedException {
			final int lineAmount = 70;
			final int startLineIndex = this.startLineIndex.get();
			final String pathToFile = file.getAbsolutePath();

			try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
				String line;
				int currentLineIndex = 0;

				while ((line = br.readLine()) != null) {
					// если в процессе чтени€ изменилс€ индекс - бросаем соотв. исключение
					if (this.startLineIndex.get() != startLineIndex) {
						throw new IndexChangedException();
					}

					currentLineIndex++;

					// до тех пор пока мы не дошли до нужной строки - пропускаем
					if (currentLineIndex < startLineIndex)
						continue;

					// когда мы доходим до последней нужной нам строки - прекращаем цикл
					if (currentLineIndex >= startLineIndex + lineAmount)
						break;

					// нужна€ нам строка
					resultLineList.add(line);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private static class IndexChangedException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	// просто возвращает кол-во строк in File дл€ JScrollPane
	public int stringsCounter() {
		try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
			while ((br.readLine()) != null) {
				counterOfStrings++;
			}
			br.close();
			return counterOfStrings;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}
}
