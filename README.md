# FileManager
File Manager for searching files files with the specified extension(With TextViewer for big text files(>1GB)).

Задание №1
Написать программу для поиска заданного текста в лог файлах.
Пользователь должен иметь возможность указать папку в сети или на жестком диске, в которой будет происходить поиск заданного текста, включая все вложенные папки.
Должна быть возможность ввода текста поиска и ввода типа расширения файлов, в которых будет осуществляться поиск(расширение по умолчанию *.log).
Результаты поиска можно вывести в левой части приложения в виде дерева файловой системы только те файлы в которых был обнаружен заданный текст.
В правой части приложения выводить содержимое файла с возможностью навигации по найденному тексту (выделить все, вперед/назад).
Плюсом будет многопоточность приложения, «не замораживание» приложения на время поиска, возможность открывать «большие» (более 1Г) файлы и осуществлять по ним быструю навигацию, возможность открывать файлы в новых «табах», т. е. использовать TabFolder или MDI.
Для отображения разрешается использовать любые Java GUI-фреймворки (AWT, Swing, SWT, JavaFX, NetBeans Platform и т.п.).
Приложение может быть как десктопным, так и веб-клиентом.
 
Задание будет оцениваться по следующим критериям:
∙  скорость поиска в файлах заданного текста и скорость навигации по открытому файлу;
∙  приятный и интуитивно понятный интерфейс приложения;
∙  краткий и понятный исходный код.



Реализация:

при нажатии на конпку searching идет считывание значений всех трех полей(расширение, слово для поиска и путь)
По умолчанию(все три поля пустые):
	Идет поиск всех файлов расширения по умолчанию в корневом каталоге машины

Слева отображаются найденные файлы в виде древовидной структуры
Справа можно их открыть - при выборе файла из левой части приложения.

Левая часть окна:
	При запуске открывает проводник( но поиск не производит)
	Т.е показывает пустое дерево и поля
	Есть три поля, одна кнопка - search
	Если поиск идет, отражать ползунок загрузки

Правая часть окна:
	При старте и поиске - вместо открытого файла, инструкция о том, как пользоваться приложением

возможность открывать «большие» (более 1Г) файлы и осуществлять по ним быструю навигацию  - реализовано, решение:
  Построить JScrollBar на основании значения количества строк в файле.
  При изменении значения ScrollBar.Listener отображать строки, начиная с нужной.
  
