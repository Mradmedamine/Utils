package com.mainsys.fhome.gui.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.UnknownHttpStatusCodeException;

public abstract class HttpClientUtils
{
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	private HttpClientUtils()
	{
		throw new UnsupportedOperationException();
	}

	public static HttpStatus getHttpStatusCode(ClientHttpResponse response)
		throws IOException
	{
		HttpStatus statusCode;
		try
		{
			statusCode = response.getStatusCode();
		}
		catch (IllegalArgumentException ex)
		{
			throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), getResponseBody(response),
				getCharset(response));
		}
		return statusCode;
	}

	public static String getResponseBodyAsString(ClientHttpResponse response)
		throws UnsupportedEncodingException
	{
		return new String(getResponseBody(response), getCharset(response));
	}

	public static byte[] getResponseBody(ClientHttpResponse response)
	{
		try
		{
			InputStream responseBody = response.getBody();
			if (responseBody != null)
			{
				return FileCopyUtils.copyToByteArray(responseBody);
			}
		}
		catch (IOException ex)
		{
			// ignore
		}
		return new byte[0];
	}

	public static String getRequestBody(byte[] body)
		throws UnsupportedEncodingException
	{
		if (body != null && body.length > 0)
		{
			return new String(body, UTF_8);
		}
		else
		{
			return StringUtils.EMPTY;
		}
	}

	public static Charset getCharset(ClientHttpResponse response)
	{
		HttpHeaders headers = response.getHeaders();
		MediaType contentType = headers.getContentType();
		return contentType != null && contentType.getCharSet() != null ? contentType.getCharSet() : UTF_8;
	}

}
