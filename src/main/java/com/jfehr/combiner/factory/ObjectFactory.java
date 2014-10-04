package com.jfehr.combiner.factory;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.jfehr.tojs.exception.NotAssignableException;
import com.jfehr.tojs.exception.ObjectInstantiationException;

public class ObjectFactory {

	public <T> T buildObject(final String className, final String defaultPackage, Class<T> superInterface) {
		T obj;
		final String fullClassName;
		final Class<?> constructionClass;
		
		if(className.contains(".")){
			fullClassName = className;
			getParamLogger().debugWithParams("Instantiation class {0}", fullClassName);
		}else{
			fullClassName = defaultPackage + (defaultPackage.endsWith(".") ? "" : ".") + className;
			getParamLogger().debugWithParams("Instantiating class {0} after adding default package of {1}", fullClassName, defaultPackage);
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
			obj = this.instantiateObject(constructionClass, getParamLogger());
		}catch(NoSuchMethodException e){
			try {
				obj = this.instantiateObject(constructionClass);
			} catch (NoSuchMethodException e1) {
				throw new ObjectInstantiationException(fullClassName, e);
			}
		}
		
		return obj;
	}
	
	public <T> List<T> buildObjectList(final List<String> classNames, final String defaultPackage, Class<T> superInterface) {
		final List<T> objectList = new ArrayList<T>();
		
		for(String cN : classNames){
			objectList.add(this.buildObject(cN, defaultPackage, superInterface));
		}
		
		return objectList;
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
