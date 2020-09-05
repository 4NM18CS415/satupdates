package com.vnrit.mykidsdrawing.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vnrit.mykidsdrawing.exception.UnauthorisedException;
import com.vnrit.mykidsdrawing.model.Admin;
import com.vnrit.mykidsdrawing.model.QuizQuestions;
import com.vnrit.mykidsdrawing.model.category;
import com.vnrit.mykidsdrawing.repository.AdminRepository;
import com.vnrit.mykidsdrawing.repository.QuizQuestionsRepository;
import com.vnrit.mykidsdrawing.repository.SchoolDetailsRepository;
import com.vnrit.mykidsdrawing.repository.UserRepository;
import com.vnrit.mykidsdrawing.repository.categoryRepository;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@CrossOrigin(origins="*",allowedHeaders="*")
@RestController
public class QuizQuestionsController {
	
	@Autowired
	private QuizQuestionsRepository quizQuestionsRepository;
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private SchoolDetailsRepository schoolDetailsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private categoryRepository categoryRepository;
	

	
	@Value("${questionImage.upload-dir}")
	private String uploadPath;
	
	@Value("${topicImage.upload-dir}")
	private String uploadPath1;
	
	
	@PostMapping("/api/create-quiz-questions/{subcategoryId}")
	public QuizQuestions storeFile(@RequestParam(name="file",required = false) MultipartFile file,@ModelAttribute QuizQuestions question,@PathVariable Long subcategoryid,HttpServletRequest request) 
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
			question.setFilename(fileName);
			question.setTumbnailName(thumbnailName);
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
		Boolean status = question.getStatus();
		Boolean isquestionComplete = question.getIsquestionComplete();
		question.setStatus(true);
		if(isquestionComplete==null) {
			question.setIsquestionComplete(false);
		}else {
			question.setIsquestionComplete(true);
		}
		category subcategoryId =  categoryRepository.getOne(subcategoryid);
		question.setSubcategoryId(subcategoryId);
		quizQuestionsRepository.save(question);
		return question;
	}
	
	@GetMapping("/api/getQuestion/{topic}")
	public List<String> getQuestion(@PathVariable String topic) throws Exception
	{
		List<String> questions = quizQuestionsRepository.getQuestionByTopic(topic);
		if(questions.isEmpty())
			throw new Exception("SubCategory is not avilable for You");
		return questions;		
	}
	
	@GetMapping("/api/getImage/{question}")
	public File getImage(@PathVariable String question) throws IOException
	{
			  String filename=quizQuestionsRepository.getImage(question);
			  String destinationDir = uploadPath + filename;
			  System.out.println(filename);
			  File image = ResourceUtils.getFile(destinationDir);
			  return image;
	}
	
//	@PutMapping("/api/updateQuestion/{question}")
//	public QuizQuestions updateQuestion(@RequestParam(name="file",required = false) MultipartFile file,@PathVariable String question,String updatedQuestion) throws FileStorageException, ResourceNotFoundException
//	{
//		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		try {
//	           
//		       // Check if the file's name contains invalid characters
//	           if(fileName.contains("..") || fileName.isEmpty()) { 
//	        	   throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);	
//	               
//	           }
//	    }
//		catch(FileStorageException ex)
//		{
//			  throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
//		}
//		long unique = new Date().getTime();
//		String tumbnailName = unique + "-thumbnail-" + file.getOriginalFilename().replace(" ", "_"); 
//		OutputStream opStream = null;
//		int id=quizQuestionsRepository.getId(question);
//		QuizQuestions selectedQuestion = quizQuestionsRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Quwstion not found :: " + id));
//		selectedQuestion.setFilename(fileName);
//		selectedQuestion.setTumbnailName(tumbnailName);
//		Path path=Paths.get(uploadPath).toAbsolutePath().normalize();
//	    try {
//			byte[] byteContent = file.getBytes();
//			Files.createDirectories(path);
//			File myFile = new File(uploadPath + fileName);
//			// destination path
//			System.out.println("fileName is " + myFile);
//			// check if file exist, otherwise create the file before writing
//			if (!myFile.exists()) {
//				myFile.createNewFile();
//			}
//			opStream = new FileOutputStream(myFile);
//			opStream.write(byteContent);
//			opStream.flush();
//			opStream.close();
//		} 
//	    catch (IOException e) {
//			e.printStackTrace();
//		}
//		try {
//			File destinationDir = new File(uploadPath);
//			Thumbnails.of(uploadPath + fileName).size(900, 800).toFiles(destinationDir, Rename.NO_CHANGE);
//			Thumbnails.of(new File(uploadPath + fileName)).size(348, 235).toFile(uploadPath + tumbnailName);
//		} 
//		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		selectedQuestion.setQuestion(updatedQuestion);
//		QuizQuestions updatedquestion=quizQuestionsRepository.save(selectedQuestion);
//		return updatedquestion;
//	}
	
	/*@PatchMapping("/api/update-quiz-questions/{id}")
	public QuizQuestions updateQuizQuestions(@PathVariable Long id,@RequestBody QuizQuestions quizQuestions) {
		
		String headerToken = request.getHeader("apiToken");
        int adminApiCount = adminRepository.verifyapiTokens(headerToken);
		
        //UnauthorisedException thrown if apiToken doesn't match admin ApiToken
		if( adminApiCount==0 ) {
			
			String error = "UnAuthorised User";
			String message = "Not Successful";
			
			throw new UnauthorisedException(401, error, message);
		}
		
		QuizQuestions quizQuestionsObj = quizQuestionsRepository.getOne(id);
		String category = quizQuestions.getCategory();
		String subcategory = quizQuestions.getSubcategory();
		String question = quizQuestions.getQuestion();
		String grade = quizQuestions.getGrade();
		Boolean status = quizQuestions.getStatus();
		Boolean isTopicComplete = quizQuestions.getIsTopicComplete();
		if(category!=null) {
			quizQuestionsObj.setCategory(category);
		}else {
			quizQuestionsObj.setCategory(quizQuestionsObj.getCategory());
		}
		if(subcategory!=null) {
			quizQuestionsObj.setSubcategory(subcategory);
		}else {
			quizQuestionsObj.setSubcategory(quizQuestionsObj.getSubcategory());
		}
		if(question!=null) {
			quizQuestionsObj.setQuestion(question);
		}else {
			quizQuestionsObj.setQuestion(quizQuestionsObj.getQuestion());
		}
		if(grade!=null) {
			quizQuestionsObj.setGrade(grade);
		}else {
			quizQuestionsObj.setGrade(quizQuestionsObj.getGrade());
		}
		if(status!=null) {
			quizQuestionsObj.setStatus(status);
		}else {
			quizQuestionsObj.setStatus(quizQuestionsObj.getStatus());
		}
		if(isTopicComplete!=null) {
			quizQuestionsObj.setIsTopicComplete(isTopicComplete);
		}else {
			quizQuestionsObj.setIsTopicComplete(quizQuestionsObj.getIsTopicComplete());
		}
		quizQuestionsRepository.save(quizQuestionsObj);
		return quizQuestionsObj;
		
	}
	*/
	@DeleteMapping("/api/delete-quiz-questions/{id}")
	public String deleteQuizQuestions(@PathVariable Long id) {
		
		String headerToken = request.getHeader("apiToken");
        int adminApiCount = adminRepository.verifyapiTokens(headerToken);
		
        //UnauthorisedException thrown if apiToken doesn't match admin ApiToken
		if( adminApiCount==0 ) {
			
			String error = "UnAuthorised User";
			String message = "Not Successful";
			
			throw new UnauthorisedException(401, error, message);
		}
		
		String filename = quizQuestionsRepository.getFilenameById(id);
		quizQuestionsRepository.deleteQuizquestions(id);
		
		 String destinationDir = uploadPath + filename;
		 try { 
             File file = new File(destinationDir);
             if(file.delete()) { 
                System.out.println(file.getName() + " is deleted!");
             } else {
                System.out.println("Delete operation is failed.");
                }
          }
            catch(Exception e)
            {
                System.out.println("Failed to Delete image !!");
            }
		 return "Successfully Deleted";
	}
	@PostMapping("/api/create-quiz-questions")
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
