package com.rawad.gamehelpers.fileparser.xml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.fileparser.event.FileParseEvent;
import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.gamehelpers.utils.Util;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;

public class EntityFileParser extends FileParser {
	
	
	private static final String PACKAGE_SEPARATOR = ":";
	
	/** When set with {@link Marshaller#setProperty(String, Object)} as the key, the value will be placed. */
	private static final String PROPERTY_ADD_STRING = "com.sun.xml.internal.bind.xmlHeaders";
	/** Sets the {@code NamespacePrefixMapper} for the {@code Marshaller}; used to remove reduntant namespaces here. */
	private static final String PROPERTY_NAMESPACE_PREFIX_MAPPER = "com.sun.xml.internal.bind.namespacePrefixMapper";
	
	private static final String DEFAULT_CONTEXT = EntityFileParser.class.getPackage().getName() + PACKAGE_SEPARATOR;
	
	private Entity e = Entity.createEntity();
	
	private EventManager eventManager = new EventManager();
	
	private String[] contextPaths = new String[0];
	
	@Override
	public void parseFile(BufferedReader reader) {
		
		e = Entity.createEntity();
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DEFAULT_CONTEXT + 
					Util.getStringFromLines(PACKAGE_SEPARATOR, false, contextPaths));
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Components components = (Components) unmarshaller.unmarshal(reader);
			
			for(Component comp: components.getComponents()) {
				
				eventManager.submitEvent(new FileParseEvent(e, comp));
				
				e.addComponent(comp);
				
			}
			
		} catch(JAXBException ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	protected void parseLine(String line) {}
	
	@Override
	public String getContent() {
		return null;
	}
	
	public void saveEntityBlueprint(Entity e, String entitySaveFileLocation) {
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DEFAULT_CONTEXT + 
					Util.getStringFromLines(PACKAGE_SEPARATOR, false, contextPaths));
			
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// So format looks nice.
			marshaller.setProperty(PROPERTY_ADD_STRING, "<!DOCTYPE " + Entity.class.getSimpleName() + ">" + Util.NL);
			// Helps make document Valid and/or Well-formed.
			
			marshaller.setProperty(PROPERTY_NAMESPACE_PREFIX_MAPPER, new NamespacePrefixMapper() {
				
				@Override
				public String[] getPreDeclaredNamespaceUris() {
					return new String[] {
							XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI
					};
				}
				
				@Override
				public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
					if (namespaceUri.equals(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI)) return "xsi";
					if (namespaceUri.equals(XMLConstants.W3C_XML_SCHEMA_NS_URI)) return "xs";
					if (namespaceUri.equals(WellKnownNamespace.XML_MIME_URI)) return "xmime";
					return suggestion;
				}
			});
			
			Components components = new Components();
			components.getComponents().addAll(e.getComponents().values());
			marshaller.marshal(components, new PrintWriter(entitySaveFileLocation));
			
		} catch (JAXBException | FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void setContextPaths(String... contextPaths) {
		this.contextPaths = contextPaths;
	}
	
	public Entity getEntity() {
		return e;
	}
	
	/**
	 * @return the eventManager
	 */
	public EventManager getEventManager() {
		return eventManager;
	}
	
}
