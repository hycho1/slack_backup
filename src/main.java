import java.util.List;

public class main {

	public static void main(String[] args) {
        
        fileProcess fi = new fileProcess("D:\\slackBackup\\Team_WI");
        
        // 폴더 경로 수집
        fi.listingSubdirectories(fi.getBackupFolderPath());
       
        // 전체 파일 목록 수집        
        fi.listAllfiles();
    
        // users.json의 사용자 id, real_name 확인 
        fi.getUserName();
        
        // 파일 OUT
        fi.writeToFile();

	}

}
