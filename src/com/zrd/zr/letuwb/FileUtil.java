package com.zrd.zr.letuwb;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class FileUtil {
	
	/**
	 * calculate size of files or directories, in MB
	 * @param file target resource, type：java.io.File
	 * @return size, in MB
	 */
	public static double getSize(String path) {
		File file = new File(path);
		//see if file exists
		if (file.exists()) {
			//recursively get a directory size, or get size of a file directly
			if (!file.isFile()) {
				//get size of file
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl)
					ss += getSize(f.getAbsolutePath());
				return ss;
			} else {
				double ss = (double) file.length() / 1024 / 1024;
				System.out.println(file.getName() + " : " + ss + "MB");
				return ss;
			}
		} else {
			System.out.println("File or directory does not exist, please check the path.");
			return 0.0;
		}
	}
	
    /**
     * get free size of SD card
     * @return size of free space, in MB
     */
    public static double getFreeSizeOfSd() {
    	File root = Environment.getRootDirectory();
    	StatFs sf = new StatFs(root.getPath());
    	long blockSize = sf.getBlockSize();
    	long availCount = sf.getAvailableBlocks();
    	return availCount * blockSize / 1024 /1024;
    }
    
	/**  
     * delete a file or a directory  
     * @param   fileName  
     * @return true or false  
     */  
    public static boolean delete(String fileName){   
        File file = new File(fileName);   
        if(!file.exists()){   
            System.out.println("Failed to delete "+fileName+".");   
            return false;   
        }else{   
            if(file.isFile()){   
                   
                return deleteFile(fileName);   
            }else{   
                return deleteDirectory(fileName);   
            }   
        }   
    }   
       
    /**  
     * delete a single file  
     * @param   fileName
     * @return true or false 
     */  
    public static boolean deleteFile(String fileName){   
        File file = new File(fileName);   
        if(file.isFile() && file.exists()){   
            file.delete();   
            System.out.println("File "+fileName+" deleted.");   
            return true;   
        }else{   
            System.out.println("File "+fileName+" not deleted.");   
            return false;   
        }   
    }   
       
    /**  
     * delete all, including files and all the sub directories  
     * @param   dir  
     * @return  true or false  
     */  
    public static boolean deleteDirectory(String dir){   
        //if "dir" is not ended with directory seperator, we add it
        if(!dir.endsWith(File.separator)){   
            dir = dir+File.separator;   
        }   
        File dirFile = new File(dir);   
        //if dir does not exist 
        if(!dirFile.exists() || !dirFile.isDirectory()){   
            System.out.println("Directory "+dir+" doesn't exist.");   
            return false;   
        }   
        boolean flag = true;   
        //delete them all!   
        File[] files = dirFile.listFiles();   
        for(int i=0;i<files.length;i++){   
            //delete files   
            if(files[i].isFile()){   
                flag = deleteFile(files[i].getAbsolutePath());   
                if(!flag){   
                    break;   
                }   
            }   
            //delete all the sub directories recursively   
            else{   
                flag = deleteDirectory(files[i].getAbsolutePath());   
                if(!flag){   
                    break;   
                }   
            }   
        }   
           
        if(!flag){   
            System.out.println("Failed to delete the directory.");   
            return false;   
        }   
           
        //delete dir   
        if(dirFile.delete()){   
            System.out.println("Directory "+dir+" deleted.");   
            return true;   
        }else{   
            System.out.println("Directory "+dir+" not deleted.");   
            return false;   
        }   
    }   
}
