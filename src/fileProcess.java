import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


// 입력 받을 파일을 처리할 클래스 입니다.
public class fileProcess {
	
	private String backupFolderPath = null;
	private JSONArray nameList = null;
	private List<String> fileList = null;
	private String[] subdirectories = null;
	

	// 생성자 - 최상위 경로 입력
	public fileProcess(String backupFolderPath){
		this.backupFolderPath = backupFolderPath;
	}
	
	
	
	public String[] getSubdirectories() {
		return subdirectories;
	}

	public void setSubdirectories(String[] subdirectories) {
		this.subdirectories = subdirectories;
	}
	
	public List<String> getFileList() {
		return fileList;
	}
	
	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	
	// backupFolderPath GET
	public String getBackupFolderPath() {
		return backupFolderPath;
	}

	// backupFolderPath SET
	public void setBackupFolderPath(String backupFolderPath) {
		this.backupFolderPath = backupFolderPath;
	}

	// nameList GET
	public JSONArray getNameList() {
		return this.nameList;
	}

	// nameList SET
	public void setNameList(JSONArray nameList) {
		this.nameList = nameList;
	}
	

	// 전체 폴더 경로 확인
	public void listingSubdirectories(String path) {
        List<String> subdirectoryList = new ArrayList<>();
        subdirectoryList.add(path);
        listAllSubdirectories(path, subdirectoryList);
        setSubdirectories(subdirectoryList.toArray(new String[0]));
    }

	
	// 전체 폴더 경로 확인
    private void listAllSubdirectories(String path, List<String> subdirectoryList) {

        File directory = new File(path);

        // 경로가 폴더인지 확인합니다.
        if (directory.isDirectory()) {
            // 하위 파일 및 폴더 목록을 가져옵니다.
            File[] subdirectories = directory.listFiles(File::isDirectory);

            if (subdirectories != null) {
                for (File subdir : subdirectories) {
                	if(subdir.isDirectory()) {
	                    subdirectoryList.add(subdir.getAbsolutePath());
	                    listAllSubdirectories(subdir.getAbsolutePath(), subdirectoryList);
                	}
                }
            } else {
                System.out.println("하위 폴더를 가져오는 중 오류가 발생했습니다.");
            }
        } else {
            System.out.println("지정된 경로는 폴더가 아닙니다.");
        }
        
	}
	
    
    // 서브 디렉토리에서 전체 파일 목록 확인 
    public void listAllfiles() {
    	
    	String[] subdirectories = getSubdirectories();
    	
    	List<String> subFileList = new ArrayList<String>();
    	
		for (String subPath : subdirectories ) {
			
	        File directory = new File(subPath);
	        File[] filesList = directory.listFiles();
	        
	        if (filesList != null) {
	            for (File file : filesList) {
	                if (file.isFile()) {
	                	subFileList.add(file.getAbsolutePath());
	                }
	            }
	        } else {
		            System.out.println("지정된 경로는 폴더가 아닙니다.");
		        }	
		}
		
		setFileList(subFileList);
	}
    

    // 유저 정보 파일 : users.json
    public void getUserName() {
    	
    	String filePath = getBackupFolderPath();
    	String targetFile = "users.json";

    	JSONArray jarr = new JSONArray();
    	JSONObject jobj = new JSONObject();

    	JSONArray rtnValue = new JSONArray();
    	
        try {
        	JSONParser parser = new JSONParser();
        	
        	Reader reader = new FileReader(filePath + "\\" + targetFile);
        	
			try {
				jarr = (JSONArray) parser.parse(reader);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
        } catch (IOException e) {
        	 
            e.printStackTrace();
             
        }
      
       for (int i = 0; i < jarr.size(); i++) {
		
    	   jobj = (JSONObject) jarr.get(i); 
    	   if( ((boolean)jobj.get("is_bot") != true) && (jobj.get("real_name") != null)) {

    		   JSONArray temp_jarr = new JSONArray();
    		   JSONObject temp_jobj = new JSONObject();
    		   
    		   temp_jobj.put("id", jobj.get("id"));
    		   temp_jobj.put("real_name", jobj.get("real_name"));

    		   rtnValue.add(temp_jobj);
    	   }
       }

       setNameList(rtnValue);
       
   }
    
    
    // 파일 리스트를 받아서 대화 내용을 텍스트 파일로 저장
    public List<String> checkConvFile(List<String> fileList) {
    	
    	 List<String> filteredFileNames = new ArrayList<>();
         Pattern pattern = Pattern.compile(".*\\d{4}-\\d{2}-\\d{2}\\.json");

         for (String fileName : fileList) {
             Matcher matcher = pattern.matcher(fileName);
             if (matcher.matches()) {
                 filteredFileNames.add(fileName);
             }
         }
         return filteredFileNames;
    }
    
    public String epochTimeTrans(String ts) {
    	
    	String setLongtype = null;
	    
    	if (ts != null) {
	    	if(ts.indexOf(".") > 0) {
		    	setLongtype = ts.substring(0,ts.indexOf("."));
	    	} else {
		    	setLongtype = ts; 
		    }
	    }
		long unixTime = Long.parseLong(setLongtype);
		
	    // Unix 시간을 Instant 객체로 변환
	    Instant instant = Instant.ofEpochSecond(unixTime);
	
	    // Instant를 LocalDateTime 객체로 변환 (UTC 기준)
	    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
	    
	    // LocalDateTime을 원하는 포맷으로 출력, 9시간 더해줌
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedDateTime = dateTime.plusHours(9).format(formatter);
	
	    return formattedDateTime;
    }
    
    public void writeToFile() {
    	
    	JSONArray jarr = new JSONArray();
    	JSONObject jobj = new JSONObject();

    	List<String> checkConvFile = checkConvFile(getFileList());
    	
    	
    	
    	
    	for (int i = 0; i < checkConvFile.size(); i++) {
    		JSONArray rtnValue = new JSONArray();
	    	try {
	        	JSONParser parser = new JSONParser();

	        	Reader reader = new FileReader(checkConvFile.get(i));
	        	
				try {
					jarr = (JSONArray) parser.parse(reader);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
	        } catch (IOException e) {
	        	 
	            e.printStackTrace();
	             
	        }
	      
	       for (int j = 0; j < jarr.size(); j++) {
	    	   
	    	   jobj = (JSONObject) jarr.get(j); 

    		   JSONObject temp_jobj = new JSONObject();

	    		   temp_jobj.put("user", jobj.get("user"));
	   			   temp_jobj.put("ts", epochTimeTrans((String)jobj.get("ts")));
	    		   temp_jobj.put("text", jobj.get("text"));
	    		   
	    	   rtnValue.add(temp_jobj);
	    	   
	       }
	       
		   String [] seperatedfileName = seperateFileName(checkConvFile.get(i));
		   
	       writeJsonToTextFile(rtnValue, getBackupFolderPath() + "\\" + "backup" + "\\" + seperatedfileName[3] + "\\" + seperatedfileName[1] + ".txt");
		   
    	}
    	
    }
    
    public void writeJsonToTextFile(JSONArray rtnValue, String fileName) {
    	
    	File file = new File(fileName);
    	File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
    	
        
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
	        	
	        	for (int i = 0; i < rtnValue.size(); i++) {
	                JSONObject jObj = (JSONObject) rtnValue.get(i);
	                
	                String text = (String) jObj.get("text");
	                String timestamp = (String) jObj.get("ts");
	                String user = mappingIdName((String)jObj.get("user"));
	                
	                String formattedLine = String.format("[ %s ] [ %s ] : %s ", timestamp, user, text);
	                writer.write(formattedLine);
	                writer.newLine();
	                
	                
	        	}
	    
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    
	    	
    }
    
    private String[] seperateFileName(String fileName) {
    	     	
    	String [] name = new String[4];
    	
    	//1. 최상위 경로와 동일한 계층
    	
		int lastSeparatorIndex = getBackupFolderPath().lastIndexOf('\\');
       
		if (lastSeparatorIndex == -1) {
	        lastSeparatorIndex = getBackupFolderPath().lastIndexOf('/');
	    } 
	
    	name[0] = getBackupFolderPath().substring(0, lastSeparatorIndex + 1);
    	
    	
    	//2. 파일 이름
		int lastFileSeparatorIndex = fileName.lastIndexOf('\\');
	       
		if (lastFileSeparatorIndex == -1) {
	        lastFileSeparatorIndex = fileName.lastIndexOf('/');
	    } 
		
		int lastDotIndex = fileName.lastIndexOf('.');
		
		name[1] = fileName.substring(lastFileSeparatorIndex, lastDotIndex);
    	
		
    	//3. 파일 확장자
		name[2] = fileName.substring(lastDotIndex + 1);
		

    	//4. 최상위에서 타겟 파일의 상위 폴더(중간)
		name[3]  = fileName.substring(getBackupFolderPath().length(), lastFileSeparatorIndex);

	
		return name;
		
    }

    private String mappingIdName(String id) {
    	
    	JSONArray jarr_namelist = getNameList();
    	JSONObject temp_jobj = new JSONObject(); 
    	
    	for (int i = 0; i < jarr_namelist.size(); i++) {
			
    		temp_jobj = (JSONObject)jarr_namelist.get(i);
    		
    		if(id.equals(temp_jobj.get("id").toString())){
    			return temp_jobj.get("real_name").toString();
    		}
		}

    	return id;
    }





}


    
	
	
	