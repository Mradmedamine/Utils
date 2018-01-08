package com.mainsys.fhome.gui.util;

import java.beans.Introspector;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

public abstract class ReflectionUtils
{

	private ReflectionUtils()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 *  Create new instance with no-arg constructor
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz)
	{
		try
		{
			return clazz.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException("error occured while instantiating object  of " + clazz.getSimpleName() + "Exception :" + e.getMessage());
		}
	}

	/**
	 *  Create new instance with one-arg constructor
	 *
	 * @param clazz
	 * @param argumentClass
	 * @param argument
	 * @return
	 */
	public static <T, U> T newInstance(Class<T> clazz, Class<U> argumentClass, U argument)
	{
		try
		{
			Constructor<T> constructor = clazz.getConstructor(argumentClass);
			return constructor.newInstance(argument);
		}
		catch (Exception e)
		{
			throw new RuntimeException("error occured while instantiating object  of " + clazz.getSimpleName() + "Exception :" + e.getMessage());
		}
	}

	/**
	 * Capitalizes the field name unless one of the first two characters are uppercase. This is in accordance with java
	 * bean naming conventions in JavaBeans API spec section 8.8.
	 *
	 * @param fieldName
	 * @return the capitalised field name
	 * @see Introspector#decapitalize(String)
	 */
	public static String capatalizeFieldName(String fieldName)
	{
		final String result;
		if (StringUtils.isNotBlank(fieldName)
			&& Character.isLowerCase(fieldName.charAt(0))
			&& (fieldName.length() == 1 || Character.isLowerCase(fieldName.charAt(1))))
		{
			result = StringUtils.capitalize(fieldName);
		}
		else
		{
			result = fieldName;
		}
		return result;
	}

	public static <T> Method findFieldGetter(String fieldName, Class<T> clazz)
	{
		String getterName = findFieldGetterName(fieldName);
		try
		{
			return clazz.getMethod(getterName);
		}
		catch (Exception e)
		{
			throw new RuntimeException("error occured while calling method " + getterName + " on class " + clazz.getSimpleName() + " Exeption :" + e.getMessage());
		}
	}

	public static <T> String findFieldGetterName(String fieldName)
	{
		return "get" + capatalizeFieldName(fieldName);
	}

	public static <T, U> Method findFieldSetter(String fieldName, Class<T> clazz)
	{
		String setterName = "set" + capatalizeFieldName(fieldName);
		try
		{
			return org.springframework.util.ReflectionUtils.findMethod(clazz, setterName, (Class<?>[]) null);
		}
		catch (Exception e)
		{
			throw new RuntimeException(
				"error occured while calling method " + setterName + " on class " + clazz.getSimpleName() + " Exception :" + e.getMessage());
		}
	}

	public static Class<?> getFieldType(Class<?> clazz, String fieldName)
	{
		try
		{
			return org.springframework.util.ReflectionUtils.findField(clazz, fieldName).getType();
		}
		catch (Exception e)
		{
			throw new RuntimeException(
				"error occured while getting field " + fieldName + " type on class " + clazz.getSimpleName() + " Exception :" + e.getMessage());
		}
	}
}
