package com.pwste.gwt.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.google.gwt.user.client.Window;

@SuppressWarnings("serial")
public class FileUploadServlet extends HttpServlet {

    protected File createFolder() {
	File uploadDirectory = new File(
		"C:\\Users\\Gienek\\workspace\\app\\war\\app\\uploaded");
	if (!uploadDirectory.exists()) {
	    if (uploadDirectory.mkdir()) {
		Window.alert("Directory is created!");
	    } else {
		Window.alert("Failed to create directory!");
	    }
	}
	return uploadDirectory;
    }

    private static final String UPLOAD_DIRECTORY = "C:\\Users\\Gienek\\workspace\\app\\war\\app\\uploaded";

    @Override
    protected void doGet(HttpServletRequest getHttpServletRequest,
	    HttpServletResponse getHttpServletResponse)
	    throws ServletException, IOException {
	super.doGet(getHttpServletRequest, getHttpServletResponse);
    }

    @Override
    protected void doPost(HttpServletRequest postHttpServletRequest,
	    HttpServletResponse postHttpServletResponse)
	    throws ServletException, IOException {
	if (ServletFileUpload.isMultipartContent(postHttpServletRequest)) {
	    FileItemFactory postFileItemFactory = new DiskFileItemFactory();
	    ServletFileUpload postServletFileUpload = new ServletFileUpload(
		    postFileItemFactory);

	    try {
		List<FileItem> itemsList = postServletFileUpload
			.parseRequest(postHttpServletRequest);

		for (FileItem itemFileItem : itemsList) {
		    if (itemFileItem.isFormField()) {
			continue;
		    }

		    String fileName = itemFileItem.getName();
		    if (fileName != null) {
			fileName = FilenameUtils.getName(fileName);
		    }

		    File uploadedFile = new File(UPLOAD_DIRECTORY, fileName);
		    if (uploadedFile.createNewFile()) {
			itemFileItem.write(uploadedFile);
			postHttpServletResponse
				.setStatus(HttpServletResponse.SC_CREATED);
			postHttpServletResponse.getWriter().print(
				"The file was created successfully.");
			postHttpServletResponse.flushBuffer();
		    } else {
			throw new IOException(
				"The fileis already exists in repository.");
		    }
		}
	    } catch (Exception e) {
		postHttpServletResponse.sendError(
			HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
			"An error occurred while creating the file: "
				+ e.getMessage());
	    }
	} else {
	    postHttpServletResponse.sendError(
		    HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
		    "Request contents type is not supported by the servlet.");
	}
    }
}
