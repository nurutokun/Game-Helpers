package com.rawad.gamehelpers.fileparser.xml;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;
import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;

public final class EntityFileParser {
	
	private static final String PACKAGE_SEPARATOR = ":";
	
	/** When set with {@link Marshaller#setProperty(String, Object)} as the key, the value will be placed. */
	private static final String ADD_STRING_PROPERTY = "com.sun.xml.internal.bind.xmlHeaders";
	/** Sets the {@code NamespacePrefixMapper} for the {@code Marshaller}; used to remove reduntant namespaces here. */
	private static final String NAMESPACE_PREFIX_MAPPER_PROPERTY = "com.sun.xml.internal.bind.namespacePrefixMapper";
	private static final String DEFAULT_CONTEXT = EntityFileParser.class.getPackage().getName() + PACKAGE_SEPARATOR
//			+ Component.class.getPackage().getName() + PACKAGE_SEPARATOR
			;
	
	private EntityFileParser() {}
	
	public static Entity parseEntityFile(Class<? extends Object> entityFileContext, String entityFileName, 
			String... contextPaths) {
		
		Entity e = Entity.createEntity();
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DEFAULT_CONTEXT + Util.getStringFromLines(contextPaths,
					PACKAGE_SEPARATOR, false));
			
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			
			Components components = unmarshaller.unmarshal(new StreamSource(Loader.getEntityBlueprintAsStream(
					entityFileContext, entityFileName)), Components.class).getValue();
			
			for(Component comp: components.getComponents()) {
				e.addComponent(comp);
			}
			
		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		
		return e;
	}
	
	public static void saveEntityBlueprint(Entity e, String entitySaveFileLocation, String... contextPaths) {
		
		try {
			
			JAXBContext jaxbContext = JAXBContext.newInstance(DEFAULT_CONTEXT + Util.getStringFromLines(contextPaths,
					PACKAGE_SEPARATOR, false));
			
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// So format looks nice.
			marshaller.setProperty(ADD_STRING_PROPERTY, "<!DOCTYPE " + Entity.class.getSimpleName() + ">" + Util.NL);
			// Helps make document Valid and/or Well-formed.
			
			marshaller.setProperty(NAMESPACE_PREFIX_MAPPER_PROPERTY, new NamespacePrefixMapper() {
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
			components.getComponents().addAll(e.getComponentsAsList());
			
			marshaller.marshal(components, new PrintWriter(entitySaveFileLocation));
			
		} catch (JAXBException | FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
	}
	
}
