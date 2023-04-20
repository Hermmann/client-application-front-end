package com.distributed.programming.client.application;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;




@Controller
public class Controllers {
	
	@GetMapping("/userApp")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "greeting";
	}
	
	@GetMapping("/download")
    public ResponseEntity<byte[]> handleFileDownload() throws IOException {
        
        // Read the file from the server
        File file = new File("/path/to/your/file.txt");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        
        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", file.getName());
        
        // Return the file as a response
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(fileBytes);
    }
    

	    
	    @PostMapping("/upload")
	    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
	        
	        // Send the file to another application using an HTTP POST request
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	        body.add("file", new ByteArrayResource(file.getBytes()) {
	            @Override
	            public String getFilename() {
	                return file.getOriginalFilename();
	            }
	        });
	        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
	        String response = restTemplate.postForObject("http://other-application.com/upload", requestEntity, String.class);
	        
	        return "File uploaded successfully";
	    }
	    
	


}
