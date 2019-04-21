
package com.zdiv.jlib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

public class JLib {
	
	public static class Property {
		Properties prop;
		String filename = "config.properties";
		
		public Property() {
			prop = new Properties();
		}
		
	    public void set(String name, String value) {
	    	prop.setProperty(name, value);
	    }    
	    
	    public String get(String name, String value) {
	    	return prop.getProperty(name, value);
	    }    
	        
	    public void load() {
			try (InputStream input = new FileInputStream(filename)) {
	            prop.load(input);
			} catch (IOException ex) {
	            ex.printStackTrace();
			}
	    }
	 
	    public void save() {
	        try (OutputStream output = new FileOutputStream(filename)) {
	            prop.store(output, null);
	        } catch (IOException io) {
	            io.printStackTrace();
	        }
	    }
	}
	
	public static class StringUtility {
	    public static String makeFourDigit(int index) {
	        StringBuilder sb = new StringBuilder();
	        if( index < 10 ) {
	            sb.append('0');
	        } 
	        if( index < 100 ) {
	            sb.append('0');
	        } 
	        if( index < 1000 ) {
	            sb.append('0');
	        }
	        sb.append(String.valueOf(index));
	        return sb.toString();
	    }
	}
	

	public static class FileUtility {
		
		public static String readFromStream( InputStream is ) {
	            Scanner scanner;
	            scanner = new Scanner( is, "UTF-8" );
	            String s = scanner.useDelimiter("\\A").next();
	            scanner.close();
	            return s; 
		}
		
		public static String readFromStream2( InputStream is ) {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	            StringBuilder s = new StringBuilder();
	            String line;
	            try {
	                while ((line = reader.readLine()) != null) {
	                  s.append(line);
	                }
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return s.toString();
		}
		
		public static String readFromFileName( String filename ) {
	            try ( InputStream is = new FileInputStream( filename )) {
	            	return readFromStream( is );
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            } catch (IOException e1) {
					e1.printStackTrace();
				}
	            return "";
		}
		
		public static boolean writeToFileName( String filename, String data ) {
	            try {
	                BufferedWriter out = new BufferedWriter(new FileWriter(filename));
	                out.write(data);  
	                out.close();
	                return true;
	            } catch (IOException e)	{
	                    e.printStackTrace();
	            }
	            return false;
		}
        
        public static boolean mkdirs(String dir) {
            File f = new File(dir);
            return f.mkdirs();
        }
	}	
}
