package com.mainsys.fhome.gui.util;

import java.io.File;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileUtils
{
	private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

	private FileUtils()
	{
		throw new UnsupportedOperationException();
	}

	public static boolean uploadFileToUrl(String url, File file)
	{
		try
		{
			if (StringUtils.isNotBlank(url) && file != null && file.length() > 0)
			{
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost uploadFile = new HttpPost(url);
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				builder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
				HttpEntity multipart = builder.build();
				uploadFile.setEntity(multipart);
				CloseableHttpResponse response = httpClient.execute(uploadFile);
				int responseStatus = response.getStatusLine().getStatusCode();
				int[] successStatusArray = new int[] { HttpStatus.SC_OK, HttpStatus.SC_ACCEPTED };
				return ArrayUtils.contains(successStatusArray, responseStatus);
			}
		}
		catch (Exception e)
		{
			log.error("error occured while uploading file to Url, Ex : {}", url, e.getMessage());
			throw new RuntimeException(e);
		}
		return false;
	}

	public static File convertToFile(MultipartFile multipart)
	{
		try
		{
			File convFile = new File(multipart.getOriginalFilename());
			multipart.transferTo(convFile);
			return convFile;
		}
		catch (Exception e)
		{
			log.error("error occured while converting MultipartFile {} to file, Ex : {}", multipart.getOriginalFilename(), e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String getFileExtension(String fileName)
	{
		if (StringUtils.isNotBlank(fileName))
		{
			fileName = StringUtils.trim(fileName);
			String[] exp = fileName.split("\\.");
			if (exp.length >= 2)
			{
				return exp[exp.length - 1].toUpperCase();
			}
		}
		return fileName;
	}

	public static String getFileNameWithoutExtension(String fileName)
	{
		if (StringUtils.isNotBlank(fileName))
		{
			String[] exp = fileName.split("\\.");
			return exp[0];
		}
		return fileName;
	}
}
