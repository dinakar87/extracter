package com.etl.pro.Extractor.components;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Component
public class ReaderComponent {
    
	private static final Logger logger = LoggerFactory.getLogger(ReaderComponent.class);
    
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    Producer producer;
    
    @Value("${ftp.src.path}")
    private String srcPath;
    
    @Value("${ftp.proc.path}")
    private String procPath;
    

    @Scheduled(cron = "${scheduling.job.cron}")
    public void filePicker() throws  IOException {
    	
    	List<String> result =null;
    	
    	JsonObject paramObject=null;
    	
    	//FileWalker 
    	try (Stream<Path> walk = Files.walk(Paths.get(srcPath))) {
    		

    		result = walk.filter(Files::isRegularFile)
    				.map(x -> x.toString()).collect(Collectors.toList());

    		for(String id:result){
    			paramObject = new JsonObject();
        		paramObject.addProperty("client_id", id.substring(id.lastIndexOf(File.separator)+1, id.lastIndexOf("_")-1));
        		paramObject.addProperty("time_stamp", LocalDateTime.now()+"");
    			producer.sendMessage("inbound.queue", paramObject.toString());
    		}
    		
    		//Move the files to a folder after Processing it
    		for(String id:result) {
    			Path temp = Files.move 
    			        (Paths.get(id),  
    			        Paths.get(procPath+id.substring(id.lastIndexOf(File.separator), id.length()))); 
    			if(temp != null) 
    	        { 
    				logger.info("File Moved Sucessfully ");
    	        } 
    	        else
    	        { 
    	        	logger.info("Fail to move the File "+id); 
    	        }  
    		}
    		

    	} catch (IOException e) {
    		e.printStackTrace();
    		throw e;
    	}
    	    	
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }
    
    
    
}