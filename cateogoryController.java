package com.vnrit.mykidsdrawing.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnrit.mykidsdrawing.exception.UnauthorisedException;
import com.vnrit.mykidsdrawing.model.Admin;
import com.vnrit.mykidsdrawing.model.category;
import com.vnrit.mykidsdrawing.repository.AdminRepository;
import com.vnrit.mykidsdrawing.repository.categoryRepository;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@CrossOrigin(origins="*",allowedHeaders="*")
@RestController
public class cateogoryController {
	
	@Autowired
	private HttpServletRequest request;
	
	@Value("${topicImage.upload-dir}")
	private String uploadPath;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private categoryRepository categoryRepository;

	@PostMapping("/api/create-quiz-category")
	public category createCategory(@RequestParam(name="file",required = false) MultipartFile file,@ModelAttribute category category,HttpServletRequest request) 
	{
	
		
         String headerToken = request.getHeader("apiToken");
		
		int verifyapiToken = adminRepository.verifyapiTokens(headerToken);
		
		if(verifyapiToken == 0) {
			
			String error = "UnAuthorised Admin";
			String message = "Not Successful";
			
			throw new UnauthorisedException(401, error, message);
		}
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		if(file!=null) {
			long unique = new Date().getTime();
			String fileName = unique + "-" + file.getOriginalFilename().replace(" ", "_");
			String thumbnailName = unique + "-thumbnail-" + file.getOriginalFilename().replace(" ", "_");
			OutputStream opStream = null;
			category.setFilename(fileName);
			category.setTumbnailName(thumbnailName);
			try {
				byte[] byteContent = file.getBytes();
				File myFile = new File(uploadPath + fileName); // destination path
				System.out.println("fileName is " + myFile);
				// check if file exist, otherwise create the file before writing
				if (!myFile.exists()) {
					myFile.createNewFile();
				}
				opStream = new FileOutputStream(myFile);
				opStream.write(byteContent);
				opStream.flush();
				opStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				File destinationDir = new File(uploadPath);
				Thumbnails.of(uploadPath + fileName).size(900, 800).toFiles(destinationDir, Rename.NO_CHANGE);
				Thumbnails.of(new File(uploadPath + fileName)).size(348, 235).toFile(uploadPath + thumbnailName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Boolean status = category.getStatus();
		Boolean isTopicComplete = category.getIsTopicComplete();
		category.setStatus(true);
		if(isTopicComplete==null) {
			category.setIsTopicComplete(false);
		}else {
			category.setIsTopicComplete(true);
		}
		categoryRepository.save(category);
		return category;
	}
}
