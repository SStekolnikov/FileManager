package sr_stekolnikov.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

/** 
 * Класс, реализующий поиск файлов заданного расширения, в заданной директории.
 * По умолчанию - поиск файлов производится в корневом каталоге
 * для экономии потребляемых ресурсов используется лист <String> , содержащий AbsolutePath до файлов
 * 
 * @autor Sr Stekolnikov
 * @version 1.0
*/
public class SearcherRightExt {		
	 	
	/** Метод отвечает за реализацию выбора директории для поиска - Используется только он
	 *if( dir.equals("") ): search на локальных дисках ? в заданном (String)AbsolutePath
     *
     *@return List<String> с AbsolutePath до файлов, которые соответствуют указанным параметрам (directory, extension) 
     * *@param String dir - директория для поиска, если передача пустой строки - поиск на локальных дисках
     *  *@param String extension - расширение искомых файлов
     */
	public List<String> searchExtension(String dir, String extension){
		if(dir.equals("")) {	//путь не указан -> по умолчанию			
			List <String> rightExt = new ArrayList<>();
		
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			File[] roots = fileSystemView.getRoots(); // Возвращает все корневые разделы этой системы.
			for(File f: roots) 
				rightExt.addAll(SearcherRightExt.searching(f, extension));
    	
			return rightExt;
		}
		else {//значит путь указан
			List <String> rightExt = new ArrayList<>();
			
			File directoryForSearching = new File(dir);
			rightExt.addAll(SearcherRightExt.searching(directoryForSearching, extension));
			
			return rightExt;
		}
		
	}
	
	/**Реализация, создана для упрощения кода - производит поиск файлов принятого расширения в заданной директории
     *
     *@return List<String> с AbsolutePath до файлов, которые соответствуют указанным параметрам (directory, extension) 
     * *@param File dir - директория для поиска
     *  *@param String extension - расширение искомых файлов
     */
	 private static List<String> searching(File rootDir, String extension) {
	        List<String> result = new ArrayList<>();
	 
	        LinkedList<File> dirList = new LinkedList<>();
	        if (rootDir.isDirectory()) {
	            dirList.addLast(rootDir); // добавляем его в коллекцию
	        }else {
	        	result.add("Path is not a directory");
	        	return result;
	        }
	        while (dirList.size() > 0) {
	            File[] filesList = dirList.getFirst().listFiles(); //лист файлов из листа с директориями
	            if (filesList != null) {
	                for (File path : filesList) {
	                    if (path.isDirectory()) {		//если он директория добавить в dirList
	                        dirList.addLast(path);
	                    } else {	//значит это файл
	                        String simpleFileName = path.getName(); // его имя	 
	                        if (simpleFileName.endsWith(extension))  //проверяем окончание на расширение
	                            result.add(path.getAbsolutePath().toString()); //добавляем его в лист result
	                    }
	                }
	            }
	            dirList.removeFirst();
	        }
	        return result;
	    }
}
