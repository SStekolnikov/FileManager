package sr_stekolnikov.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;
/** 
 * �����, ����������� ����� ��������� ������ � ������ ��������� �������, � �������� ����������.
 * �� ��������� - ����� ���� ������ ��������� ���������� ������������ � �������� ��������
 * ��� �������� ������������ �������� ������������ ���� <String> , ���������� AbsplutePath �� �����
 * 
 * @autor Sr Stekolnikov
 * @version 2.0
*/
public class SearcherWord {	
	private String directory = "";
	private String searchWord = "";
	private String extension = ".log"; // default extension
	
	 /**����� ��������� ������ � ������ ��������� �������, � �������� ����������.
     *version 2.0 - ������������ ������ ���� ����� (������ - ����������, ������� ��� ��������� ����)
     *@return List<String> � ������� ������� extension, ����������� searchWord ��� (���� ����� �� �������) ����� ������� extension
     *@throws IOException
     */
	//synchronized for JTabbedPane
	public synchronized List<String> searching() throws IOException{
		List<String> rightExt = new ArrayList<>();
		SearcherRightExt s = new SearcherRightExt();		
			//if (directory.equals("") 
				//directory =  DEFAULT_DIR		
			//�lse - 
				//this.directory = user_dir			
			
			//if (extension.equals(""))
				//	extension = DEFAULT_EXTENTION (.log)
			//else
				// this.extension = user_ext	
		
	//����� ������ ������� ����������
			rightExt = s.searchExtension(this.directory, this.extension); 
				
	
			if(!searchWord.equals("")) {	//����� �������, � ����� rightExt ����� �������� �����			
				List<String> results = new ArrayList<>();
				results.addAll(this.searchWord(rightExt));
				return results;
			}
			else {	//����� �� �������, ������� ������ ������ ������� ����������
				return rightExt;
			}
		}
		 
	 /** 
     *����������, ������� ��� ��������� ���� - ���������� ����� ����� � ������ ��������� List<String>
     *@return List<String> � �������, ����������� ������� �����
     *@param rightEx - ������ String � AbsolutePath �� ������, � ������� ����� ���������� ����� �����
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