package sr_stekolnikov.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
/** 
 * Класс, реализующий поиск заданного текста в файлах заданного формата, в заданной директории.
 * По умолчанию - поиск всех файлов заданного расширения производится в корневом каталоге
 * для экономии потребляемых ресурсов используется лист <String> , содержащий AbsplutePath до файла
 * 
 * @autor Sr Stekolnikov
 * @version 2.0
*/
public class SearcherWord {	
	private String directory = "";
	private String searchWord = "";
	private String extension = ".log"; // default extension
	
	 /**Поиск заданного текста в файлах заданного формата, в заданной директории.
     *version 2.0 - использовать только этот метод (другой - реализация, создана для упрощения кода)
     *@return List<String> с файлами нужного extension, содержащими searchWord или (если слово не указано) файлы нужного extension
     *@throws IOException
     */
	//synchronized for JTabbedPane
	public synchronized List<String> searching() throws IOException{
		List<String> rightExt = new ArrayList<>();
		SearcherRightExt s = new SearcherRightExt();		
			//if (directory.equals("") 
				//directory =  DEFAULT_DIR		
			//еlse - 
				//this.directory = user_dir			
			
			//if (extension.equals(""))
				//	extension = DEFAULT_EXTENTION (.log)
			//else
				// this.extension = user_ext	
		
	//поиск файлов нужного расширения
			rightExt = s.searchExtension(this.directory, this.extension); 
				
	
			if(!searchWord.equals("")) {	//слово указано, в листе rightExt поиск искомого слова			
				List<String> results = new ArrayList<>();
				results.addAll(this.searchWord(rightExt));
				return results;
			}
			else {	//слово не указано, вернуть список файлов нужного расширения
				return rightExt;
			}
		}
		 
	 /** 
     *реализация, создана для упрощения кода - производит поиск слова в файлах принятого List<String>
     *@return List<String> с файлами, содержащими искомое слово
     *@param rightEx - список String с AbsolutePath до файлов, в которых нужно произвести поиск слова
     *@throws IOException
     */
	private List <String> searchWord(List<String> rightExt) throws IOException {
		List <String> results = new ArrayList<>();				
		for(String s: rightExt) {
			File file = new File(s);
			if (file.canRead()) {
				try(FileReader fr = new FileReader(file);Scanner scn = new Scanner(fr)){
						while(scn.hasNextLine()) {
						String nextLine = scn.nextLine();				
							if(nextLine.contains(searchWord)){
								//searchWord is found - add in results
								results.add(file.getAbsolutePath());								
								break;
							}
						} 
				}catch(IOException t) {
					JOptionPane.showMessageDialog(null, t.toString(), t.getMessage(), JOptionPane.ERROR_MESSAGE);
				}
				//end of reading
			}else 
			   continue;	// next file		
		}
		if(results.size() != 0)
			return results;
		else {
			results.add("No mathches with your parametres.");
			return results;// send the message "NoMatches"			
		}		
			
	}
	

	//setters for fields
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public void setSearch_word(String search_word) {
		this.searchWord = search_word;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}