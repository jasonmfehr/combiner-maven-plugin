package com.jfehr.combiner.factory;

import static com.jfehr.combiner.logging.LogHolder.getParamLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import com.jfehr.tojs.exception.NotAssignableException;
import com.jfehr.tojs.exception.ObjectInstantiationException;

public abstract class ObjectFactory {

	@Requirement
    private PlexusContainer container;
	
	protected abstract Class<?> getObjectClass();
	protected abstract String getDefaultPackage();
	
	public <T> T buildObject(final String classOrRole) {
		final String fullClassOrRoleName;
		T obj;
		Object plexusObj;
		
		if(classOrRole == null){
			throw new NullPointerException("classOrRole is null");
		}
		
		fullClassOrRoleName = this.buildFullyQualified(classOrRole);
		
		plexusObj = this.attemptPlexusRetrieve(fullClassOrRoleName);
		
		if(plexusObj != null){
			//generics in Java are a compile time check, but this class needs to do runtime type checks, thus 
			//the call to checkAssignability is necessary to ensure the object who's class was determined at 
			//runtime can be assigned to the class specified by this factory's getObjectClass method
			if(this.checkAssignability(plexusObj.getClass())){
				getParamLogger().debugWithParams("Found object with class {0} in the plexus container, returning that object", plexusObj.getClass().getCanonicalName());
				return (T)plexusObj;
			}else{
				getParamLogger().debugWithParams("Could not cast object with class {0} that was retrieved from the plexus container to the expected type {1} of this factory, moving ahead with creating a new object", plexusObj.getClass().getCanonicalName(), this.getObjectClass().getCanonicalName());
			}
		}
		
		obj = this.constructObject(fullClassOrRoleName);

		return obj;
	}
	
	public <T> List<T> buildObjectList(final List<String> classOrRoleNames) {
		final List<T> objectList = new ArrayList<T>();
		
		for(String cN : classOrRoleNames){
			//TODO figure out why this has to be cast
			objectList.add((T)this.buildObject(cN));
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
	
	private String buildFullyQualified(final String classOrRole) {
		final String fullyQualified;
		
		if(classOrRole.contains(".")){
			getParamLogger().debugWithParams("Provided class or role {0} is already fully qualified", classOrRole);
			fullyQualified = classOrRole;
		}else{
			fullyQualified = this.getDefaultPackage() + "." + classOrRole;
			getParamLogger().debugWithParams("Instantiating class {0} after adding default package of {1}", fullyQualified, this.getDefaultPackage());
		}
		
		return fullyQualified;
	}
	
	private Object attemptPlexusRetrieve(String fullyQualifiedObjectName) {
		Object obj = null;
		
		try {
			getParamLogger().debugWithParams("Attempting lookup from plexus container using value {0}", fullyQualifiedObjectName);
			obj = this.container.lookup(fullyQualifiedObjectName);
		} catch (ComponentLookupException e) {
			getParamLogger().debugWithParams("Did not find component {0} in plexus container", fullyQualifiedObjectName);
		}
		
		return obj;
	}
	
	private <T> T constructObject(final String fullyQualifiedName) {
		final Class<?> constructionClass;
		T obj;
		
		getParamLogger().debugWithParams("Attempting to load class {0}", fullyQualifiedName);
		try {
			constructionClass = ClassUtils.getClass(fullyQualifiedName);
		} catch (ClassNotFoundException e) {
			throw new ObjectInstantiationException(fullyQualifiedName, e);
		}
		
		if(!this.checkAssignability(constructionClass)){
			throw new NotAssignableException(fullyQualifiedName);
		}
		
		try{
			getParamLogger().debugWithParams("Attempting to instantiate an object with class {0} using a constructor that takes only a org.apache.maven.plugin.logging.Log parameter", fullyQualifiedName);
			//try invoking the constructor that has a single org.apache.maven.plugin.logging.Log argument
			obj = this.instantiateObject(constructionClass, getParamLogger());
		}catch(NoSuchMethodException e){
			try {
				//no single argument constructor was found, try the default no argument constructor
				getParamLogger().debugWithParams("Could not find a single argument constructor that takes a org.apache.maven.plugin.logging.Log parameter, attempting to use the default constructor to instantiate an object with class {0}", fullyQualifiedName);
				obj = this.instantiateObject(constructionClass);
			} catch (NoSuchMethodException e1) {
				throw new ObjectInstantiationException(fullyQualifiedName, e);
			}
		}
		
		getParamLogger().debugWithParams("Successfully instantiated an object with class {0}", fullyQualifiedName);
		
		return obj;
	}
	
	/**
	 * Determines if the class of an object with the provided class can be cast to the class specified in the {@link #getObjectClass()} method.
	 * 
	 * @param o {@link Class} that will be checked for type compatibility with the class returned from {@link #getObjectClass()}
	 * @return {@code boolean} with {@code true} indicating an object with the specified class can be cast or {@code false} indicating it cannot be cast
	 */
	private boolean checkAssignability(Class<?> clazz) {
		return ClassUtils.isAssignable(clazz, this.getObjectClass());
	}
	
}
