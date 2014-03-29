package com.jfehr.tojs.mojo;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.jfehr.tojs.exception.NotAssignableException;
import com.jfehr.tojs.exception.ObjectInstantiationException;
import com.jfehr.tojs.logging.ParameterizedLogger;

public class ObjectFactory {

	private final ParameterizedLogger logger;
	
	public ObjectFactory(final ParameterizedLogger logger) {
		this.logger = logger;
	}
	
	public <T> T buildObject(final String className, final String defaultPackage, Class<T> superInterface) {
		T obj;
		final String fullClassName;
		final Class<?> constructionClass;
		
		if(className.contains(".")){
			fullClassName = className;
			logger.debugWithParams("Instantiation class {0}", fullClassName);
		}else{
			fullClassName = defaultPackage + (defaultPackage.endsWith(".") ? "" : ".") + className;
			logger.debugWithParams("Instantiating class {0} after adding default package of {1}", fullClassName, defaultPackage);
		}
		
		try {
			constructionClass = ClassUtils.getClass(fullClassName);
		} catch (ClassNotFoundException e) {
			throw new ObjectInstantiationException(className, e);
		}
		
		if(!ClassUtils.isAssignable(constructionClass, superInterface)){
			throw new NotAssignableException(className);
		}
		
		try{
			obj = this.instantiateObject(constructionClass, this.logger);
		}catch(NoSuchMethodException e){
			try {
				obj = this.instantiateObject(constructionClass);
			} catch (NoSuchMethodException e1) {
				throw new ObjectInstantiationException(fullClassName, e);
			}
		}
		
		return obj;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T instantiateObject(Class<?> constructionClass, Object... args) throws NoSuchMethodException {
		final T obj;
		
		try{
			obj = (T) ConstructorUtils.invokeConstructor(constructionClass, args);
		} catch (IllegalAccessException e) {
			throw new ObjectInstantiationException(constructionClass.getName(), e);
		} catch (InvocationTargetException e) {
			throw new ObjectInstantiationException(constructionClass.getName(), e);
		} catch (InstantiationException e) {
			throw new ObjectInstantiationException(constructionClass.getName(), e);
		}
		
		return obj;
	}
}
