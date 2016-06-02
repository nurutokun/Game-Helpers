package com.rawad.gamehelpers.fileparser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.Loader;

public final class EntityFileParser {
	
	public Entity parseEntityFile(Class<? extends Object> clazz, String entityFileName) {
		Entity e = Entity.createEntity();
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Entity.class);
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			e = (Entity) unmarshaller.unmarshal(Loader.getEntityBlueprintFileLocation(clazz, entityFileName));
			
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		
		return e;
	}
	
	/*/
	private static final String ROOT_ELEMENT = "entity";
	
	private DocumentBuilderFactory docBuilderFactory;
	
	private DocumentBuilder docBuilder;
	
	private Class<? extends Object> clazz;
	
	public EntityFileParser(Class<? extends Object> clazz) {
		this.clazz = clazz;
		
		docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setValidating(true);
		
	}
	
	public Entity parseEntityFile(String entityFileName) {
		Entity e = Entity.createEntity();
		
		try {
			
			docBuilder = docBuilderFactory.newDocumentBuilder();
			
			Document doc = docBuilder.parse(Loader.getEntityBlueprintFileLocation(clazz, entityFileName));
			
			Node root = doc.getElementsByTagName(ROOT_ELEMENT).item(0);
			
			NodeList components = root.getChildNodes();
			
			for(int i = 0; i < components.getLength(); i++) {
				
				Node componentNode = components.item(i);
				
				try {
					Class<? extends Object> componentClass = Class.forName(clazz.getPackage().getName() 
							+ Util.PACKAGE_SEPARATOR + componentNode.getNodeName());
					// Make new instance w/ default constructor -> cast to component -> give info in file?
					
					Object component = componentClass.getConstructor().newInstance();
					
					NodeList valuesInComponent = componentNode.getChildNodes();
					
					for(int j = 0; j < valuesInComponent.getLength(); j++) {
						
						Node value = valuesInComponent.item(j);
						
						setFieldValue(component, value);
						
					}
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex1) {
					Logger.log(Logger.WARNING, "The component class " + componentNode.getNodeName() + " could not be found"
							+ " in the package " + clazz.getPackage().getName());
				}
				
			}
			
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			ex.printStackTrace();
		}
		
		return e;
	}
	
	/**
	 * Sets the {@code Field} with the name of {@code value} to the contents of {@code value} in the given 
	 * {@code component}, if it is present.
	 * 
	 * @param component
	 * @param value
	 * /
	private void setFieldValue(Object component, Node value) {
		
		try {
			
			Field field = component.getClass().getField(value.getNodeName());
			field.setAccessible(true);
			
			field.set(component, field.getType().cast(value.getTextContent()));
			
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
			Logger.log(Logger.WARNING, "The setter method for the value " + value.getNodeName() + " could not be found in"
					+ " the component " + component.getClass().getName());
		} catch(ClassCastException ex1) {
			Logger.log(Logger.WARNING, "Mismatch between field " + value.getNodeName() + " in "
					+ component.getClass().getName() + " and the type declared in the xml file " 
					+ value.getTextContent());
		}
		
	}/**/
	
}
