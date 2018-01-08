package com.mainsys.fhome.gui.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public abstract class MappingUtils
{
	public static boolean hasNullAttributes(Object source)
	{
		return getNullPropertyNames(source).length == 0;
	}

	public static String[] getNullPropertyNames(Object source)
	{
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<String>();
		for (java.beans.PropertyDescriptor pd : pds)
		{
			Object srcValue = src.getPropertyValue(pd.getName());
			if (srcValue == null)
			{
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	public static Object cloneBean(Object obj)
	{
		try
		{
			return org.apache.commons.beanutils.BeanUtils.cloneBean(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException("error cloning bean class: " + obj.getClass(), e);
		}
	}

	public static <T extends Serializable> T deepCloneBean(T obj)
	{
		try
		{
			return SerializationUtils.clone(obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException("error deep cloning bean class: " + obj.getClass(), e);
		}
	}

	public static Object mapNotNull(final Object source, final Object dest)
	{
		return map(source, dest, false);
	}

	public static Object map(final Object source, final Object dest)
	{
		return map(source, dest, true);
	}

	private static Object map(final Object source, final Object dest, boolean mapNull)
	{
		try
		{
			if (source != null)
			{
				String[] nullproperties = mapNull ? ArrayUtils.EMPTY_STRING_ARRAY : getNullPropertyNames(source);
				BeanUtils.copyProperties(source, dest, nullproperties);
			}
		}
		catch (Exception e)
		{
			String message = String.format("error while mapping properties From Object type %s to Object %s Exception: %s", //
				source.getClass().getSimpleName(), dest.getClass().getSimpleName(), e.getMessage());
			throw new RuntimeException(message, e);
		}
		return dest;
	}

	@SuppressWarnings("unchecked")
	public static <T, U> U map(Object source, final Class<U> destType)
	{
		if (source != null)
		{
			return (U) mapNotNull(source, ReflectionUtils.newInstance(destType));
		}
		return null;
	}

	public static <T, U> List<U> map(final List<T> source, final Class<U> destType)
	{
		final List<U> dest = new ArrayList<U>();

		for (T element : source)
		{
			if (element != null)
			{
				dest.add(map(element, destType));
			}
		}
		return dest;
	}

}
