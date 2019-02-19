package sr_stekolnikov.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

/** 
 * �����, ����������� ����� ������ ��������� ����������, � �������� ����������.
 * �� ��������� - ����� ������ ������������ � �������� ��������
 * ��� �������� ������������ �������� ������������ ���� <String> , ���������� AbsolutePath �� ������
 * 
 * @autor Sr Stekolnikov
 * @version 1.0
*/
public class SearcherRightExt {		
	 	
	/** ����� �������� �� ���������� ������ ���������� ��� ������ - ������������ ������ ��
	 *if( dir.equals("") ): search �� ��������� ������ ? � �������� (String)AbsolutePath
     *
     *@return List<String> � AbsolutePath �� ������, ������� ������������� ��������� ���������� (directory, extension) 
     * *@param String dir - ���������� ��� ������, ���� �������� ������ ������ - ����� �� ��������� ������
     *  *@param String extension - ���������� ������� ������
     */
	public List<String> searchExtension(String dir, String extension){
		if(dir.equals("")) {	//���� �� ������ -> �� ���������			
			List <String> rightExt = new ArrayList<>();
		
			FileSystemView fileSystemView = FileSystemView.getFileSystemView();
			File[] roots = fileSystemView.getRoots(); // ���������� ��� �������� ������� ���� �������.
			for(File f: roots) 
				rightExt.addAll(SearcherRightExt.searching(f, extension));
    	
			return rightExt;
		}
		else {//������ ���� ������
			List <String> rightExt = new ArrayList<>();
			
			File directoryForSearching = new File(dir);
			rightExt.addAll(SearcherRightExt.searching(directoryForSearching, extension));
			
			return rightExt;
		}
		
	}
	
	/**����������, ������� ��� ��������� ���� - ���������� ����� ������ ��������� ���������� � �������� ����������
     *
     *@return List<String> � AbsolutePath �� ������, ������� ������������� ��������� ���������� (directory, extension) 
     * *@param File dir - ���������� ��� ������
     *  *@param String extension - ���������� ������� ������
     */
	 private static List<String> searching(File rootDir, String extension) {
	        List<String> result = new ArrayList<>();
	 
	        LinkedList<File> dirList = new LinkedList<>();
	        if (rootDir.isDirectory()) {
	            dirList.addLast(rootDir); // ��������� ��� � ���������
	        }else {
	        	result.add("Path is not a directory");
	        	return result;
	        }
	        while (dirList.size() > 0) {
	            File[] filesList = dirList.getFirst().listFiles(); //���� ������ �� ����� � ������������
	            if (filesList != null) {
	                for (File path : filesList) {
	                    if (path.isDirectory()) {		//���� �� ���������� �������� � dirList
	                        dirList.addLast(path);
	                    } else {	//������ ��� ����
	                        String simpleFileName = path.getName(); // ��� ���	 
	                        if (simpleFileName.endsWith(extension))  //��������� ��������� �� ����������
	                            result.add(path.getAbsolutePath().toString()); //��������� ��� � ���� result
	                    }
	                }
	            }
	            dirList.removeFirst();
	        }
	        return result;
	    }
}
