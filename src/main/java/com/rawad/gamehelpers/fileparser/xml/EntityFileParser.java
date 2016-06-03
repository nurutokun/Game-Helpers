package com.rawad.gamehelpers.fileparser.xml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

public final class EntityFileParser {
	
	/** When set with {@link Marshaller#setProperty(String, Object)} as the key, the value will be placed. */
	private static final String ADD_STRING_PROPERTY = "com.sun.xml.internal.bind.xmlHeaders";
	
	private EntityFileParser() {}
	
	@SafeVarargs
	public static Entity parseEntityFile(Class<? extends Object> clazz, String entityFileName, 
			Class<? extends Component>... classes) {
		
		Entity e = Entity.createEntity();
		
		try {
			
//			Class<? extends Object>[] classesArray = (Class<? extends Object>[]) classes.toArray();
			
			JAXBContext jaxbContext = JAXBContext.newInstance(classes);// TODO: Fix parsing.
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Components components = unmarshaller.unmarshal(new StreamSource(Loader.getEntityBlueprintAsStream(clazz,
					entityFileName)), Components.class).getValue();
			
			for(Component comp: components.getComponents()) {
				Logger.log(Logger.DEBUG, "Adding comp: " + comp);
				e.addComponent(comp);
			}
			
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		
		return e;
	}
	
	@SafeVarargs
	public static void saveEntityBlueprint(Entity e, String entitySaveFileLocation, 
			Class<? extends Component>... classes) {
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(Util.append(classes, Components.class));
			
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// So format looks nice.
			marshaller.setProperty(ADD_STRING_PROPERTY, "<!DOCTYPE " + Entity.class.getSimpleName() + ">" + Util.NL);
			// Helps make document Valid and/or Well-formed.
			
			Components components = new Components();
			components.getComponents().addAll(e.getComponentsAsList());
			
			marshaller.marshal(components, new PrintWriter(entitySaveFileLocation));
			
		} catch (JAXBException | FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
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
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
						IllegalArgumentException | InvocationTargetException | NoSuchMethodException | 
						SecurityException ex1) {
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
