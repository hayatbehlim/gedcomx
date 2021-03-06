/**
 * Copyright 2011-2012 Intellectual Reserve, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.build.enunciate;

import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MemberDeclaration;
import com.sun.mirror.type.*;
import net.sf.jelly.apt.decorations.TypeMirrorDecorator;
import net.sf.jelly.apt.decorations.declaration.DecoratedMethodDeclaration;
import net.sf.jelly.apt.decorations.declaration.PropertyDeclaration;
import net.sf.jelly.apt.decorations.type.DecoratedClassType;
import net.sf.jelly.apt.decorations.type.DecoratedInterfaceType;
import net.sf.jelly.apt.decorations.type.DecoratedTypeMirror;
import org.codehaus.enunciate.apt.EnunciateFreemarkerModel;
import org.codehaus.enunciate.config.SchemaInfo;
import org.codehaus.enunciate.contract.jaxb.*;
import org.codehaus.enunciate.contract.jaxb.types.KnownXmlType;
import org.codehaus.enunciate.contract.validation.BaseValidator;
import org.codehaus.enunciate.contract.validation.ValidationResult;
import org.codehaus.enunciate.qname.XmlQNameEnum;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.gedcomx.rt.*;
import org.gedcomx.rt.json.JsonElementWrapper;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ryan Heaton
 */
public class GedcomxValidator extends BaseValidator {

  private final Map<String, Declaration> jsonNameDeclarations = new HashMap<String, Declaration>();

  @Override
  public ValidationResult validate(EnunciateFreemarkerModel model) {
    ValidationResult result = super.validate(model);
    for (SchemaInfo schemaInfo : model.getNamespacesToSchemas().values()) {
      for (Registry registry : schemaInfo.getRegistries()) {
        Collection<LocalElementDeclaration> localElements = registry.getLocalElementDeclarations();
        for (LocalElementDeclaration localElement : localElements) {
          JsonElementWrapper elementWrapper = localElement.getAnnotation(JsonElementWrapper.class);
          if (elementWrapper != null) {
            String jsonName = elementWrapper.namespace() + elementWrapper.name();
            Declaration previous = this.jsonNameDeclarations.put(jsonName, localElement);
            if (previous != null) {
              result.addError(localElement, "JSON name conflict with " + String.valueOf(previous.getPosition()));
            }
          }
        }
      }
    }
    return result;
  }

  private boolean suppressWarning(Declaration declaration, String warning) {
    SuppressWarnings suppressionInfo = declaration.getAnnotation(SuppressWarnings.class);
    return suppressionInfo != null && Arrays.asList(suppressionInfo.value()).contains(warning);
  }

  @Override
  public ValidationResult validateSimpleType(SimpleTypeDefinition simpleType) {
    return validateTypeDefinition(simpleType);
  }

  @Override
  public ValidationResult validateEnumType(EnumTypeDefinition enumType) {
    return validateTypeDefinition(enumType);
  }

  @Override
  public ValidationResult validateRootElement(RootElementDeclaration rootElementDeclaration) {
    ValidationResult result = super.validateRootElement(rootElementDeclaration);
    String namespace = rootElementDeclaration.getNamespace();
    if (namespace == null) {
      namespace = "";
    }

    if (namespace.isEmpty()) {
      result.addError(rootElementDeclaration, "Root element should not be in the empty namespace.");
    }

    if (rootElementDeclaration.getName().toLowerCase().startsWith("web")) {
      result.addWarning(rootElementDeclaration, "You probably don't want a root element that starts with the name 'web'. Consider renaming using the @XmlRootElement annotation.");
    }

    JsonElementWrapper elementWrapper = rootElementDeclaration.getAnnotation(JsonElementWrapper.class);
    if (namespace.startsWith(CommonModels.GEDCOMX_DOMAIN) && elementWrapper == null) {
      result.addWarning(rootElementDeclaration, "Root elements in the '" + CommonModels.GEDCOMX_DOMAIN + "' namespace should probably be annotated with @" + JsonElementWrapper.class.getSimpleName() + ".");
    }

    if (elementWrapper != null) {
      String jsonName = elementWrapper.namespace() + elementWrapper.name();
      Declaration previous = this.jsonNameDeclarations.put(jsonName, rootElementDeclaration);
      if (previous != null) {
        result.addError(rootElementDeclaration, "JSON name conflict with " + String.valueOf(previous.getPosition()));
      }
    }

    return result;
  }

  public ValidationResult validateTypeDefinition(TypeDefinition typeDef) {
    ValidationResult result = new ValidationResult();

    //heatonra: using @XmlSeeAlso for a QName enum doesn't actually include the xmlns declaration, so there's no reason to validate it here...
    //heatonra: I couldn't figure out a good way to validate the use of @XmlSeeAlso for extended types at build-time. I think we'll have to rely on unit tests to validate this.

    if ("".equals(typeDef.getNamespace())) {
      result.addError(typeDef, "Type definition should not be in the empty namespace.");
    }

    if (typeDef.getName().toLowerCase().startsWith("web")) {
      result.addWarning(typeDef, "You probably don't want a type definition that starts with the name 'web'. Consider renaming using the @XmlType annotation.");
    }

    Collection<Attribute> attributes = typeDef.getAttributes();
    if (attributes != null && !attributes.isEmpty()) {
      for (Attribute attribute : attributes) {
        boolean isURI = ((DecoratedTypeMirror) TypeMirrorDecorator.decorate(attribute.getAccessorType())).isInstanceOf("org.gedcomx.common.URI");
        if (isURI && !KnownXmlType.ANY_URI.getQname().equals(attribute.getBaseType().getQname())) {
          result.addError(attribute, "Accessors of type 'org.gedcomx.common.URI' should of type xs:anyURI. Please annotate the attribute with @XmlSchemaType(name = \"anyURI\", namespace = XMLConstants.W3C_XML_SCHEMA_NS_URI)");
        }

        if ("id".equalsIgnoreCase(attribute.getName())) {
          if (!attribute.isXmlID()) {
            result.addError(attribute, "Id attributes should be annotated as @XmlID.");
          }
        }

        TypeMirror accessorType = attribute.getAccessorType();
        if (accessorType instanceof EnumType && ((EnumType) accessorType).getDeclaration().getAnnotation(XmlQNameEnum.class) != null) {
          result.addError(attribute, "Accessors should not reference QName enums directly. You probably want to annotate this accessor with @XmlTransient.");
        }
      }
    }

    Value value = typeDef.getValue();
    if (value != null) {
      TypeMirror accessorType = value.getAccessorType();
      if (accessorType instanceof EnumType && ((EnumType) accessorType).getDeclaration().getAnnotation(XmlQNameEnum.class) != null) {
        result.addError(value, "Accessors should not reference QName enums directly. You probably want to annotate this accessor with @XmlTransient.");
      }

      boolean isURI = ((DecoratedTypeMirror) TypeMirrorDecorator.decorate(accessorType)).isInstanceOf("org.gedcomx.common.URI");
      if (isURI && !KnownXmlType.ANY_URI.getQname().equals(value.getBaseType().getQname())) {
        result.addError(value, "Accessors of type 'org.gedcomx.common.URI' should of type xs:anyURI. Please annotate the value with @XmlSchemaType(name = \"anyURI\", namespace = XMLConstants.W3C_XML_SCHEMA_NS_URI)");
      }
    }

    Collection<Element> elements = typeDef.getElements();
    if (elements != null && !elements.isEmpty()) {
      for (Element element : elements) {
        for (Element choice : element.getChoices()) {
          boolean isURI = ((DecoratedTypeMirror) TypeMirrorDecorator.decorate(choice.getAccessorType())).isInstanceOf("org.gedcomx.common.URI");
          if (isURI && !KnownXmlType.ANY_URI.getQname().equals(choice.getBaseType().getQname())) {
            result.addError(choice, "Accessors of type 'org.gedcomx.common.URI' should of type xs:anyURI. Please annotate the element with @XmlSchemaType(name = \"anyURI\", namespace = XMLConstants.W3C_XML_SCHEMA_NS_URI)");
          }

          if ("href".equals(choice.getName())) {
            result.addError(choice, "Entity links should be make with an attribute named 'href'. You probably need to apply @XmlAttribute.");
          }

          TypeMirror accessorType = choice.getAccessorType();
          if (accessorType instanceof EnumType && ((EnumType) accessorType).getDeclaration().getAnnotation(XmlQNameEnum.class) != null) {
            result.addError(choice, "Accessors should not reference QName enums directly. You probably want to annotate this accessor with @XmlTransient.");
          }

          QName ref = choice.getRef();
          String ns = ref != null ? ref.getNamespaceURI() : choice.getNamespace();
          if (ns == null || "".equals(ns)) {
            result.addError(choice, "Choice should not reference the empty namespace.");
          }
        }

        if (element.isCollectionType()) {
          if (!element.isWrapped() && element.getName().endsWith("s")) {
            if (!suppressWarning(element, "gedcomx:plural_xml_name")) {
              result.addWarning(element, "You may want to use @XmlElement to change the name to a non-plural form.");
            }
          }
          else {
            //make sure collection types have a proper json name.
            String jsonMemberName = element.getJsonMemberName();
            if (!jsonMemberName.endsWith("s")) {
              if (!suppressWarning(element, "gedcomx:non_plural_json_name")) {
                result.addWarning(element, "Collection element should probably have a JSON name that ends with 's'. Consider annotating it with @JsonName.");
              }
            }
            else if (!element.isWrapped()) {
              if (element.getDelegate() instanceof PropertyDeclaration) {
                DecoratedMethodDeclaration getter = ((PropertyDeclaration) element.getDelegate()).getGetter();
                if (getter == null || getter.getAnnotation(JsonProperty.class) == null || !jsonMemberName.equals(getter.getAnnotation(JsonProperty.class).value())) {
                  result.addWarning(element, "Collection element is annotated with @JsonName, but the getter needs to also be annotated with @JsonProperty(\"" + jsonMemberName + "\").");
                }
                DecoratedMethodDeclaration setter = ((PropertyDeclaration) element.getDelegate()).getSetter();
                if (setter == null || setter.getAnnotation(JsonProperty.class) == null || !jsonMemberName.equals(setter.getAnnotation(JsonProperty.class).value())) {
                  result.addWarning(element, "Collection element is annotated with @JsonName, but the setter needs to also be annotated with @JsonProperty(\"" + jsonMemberName + "\").");
                }
              }
              else if (element.getDelegate() instanceof FieldDeclaration) {
                if (element.getAnnotation(JsonProperty.class) == null || !jsonMemberName.equals(element.getAnnotation(JsonProperty.class).value())) {
                  result.addWarning(element, "Collection element is annotated with @JsonName, but the field needs to also be annotated with @JsonProperty(\"" + jsonMemberName + "\").");
                }
              }
            }
          }
        }
      }
    }

    AnyElement anyElement = typeDef.getAnyElement();
    if (anyElement != null) {
      if (!isInstanceOf(typeDef, SupportsExtensionElements.class.getName())) {
        result.addError(anyElement, "Type definitions that supply the 'any' element must implement " +
          SupportsExtensionElements.class.getName() + " so the 'any' elements can be serialized to/from JSON.");
      }

      if (!"extensionElements".equals(anyElement.getSimpleName())) {
        if (!suppressWarning(anyElement, "gedcomx:unconventional_any_element_name")) {
          result.addWarning(anyElement, "The 'any' element might be better named 'extensionElements' to conform to convention.");
        }
      }

      JsonIgnore getterIgnore = anyElement.getDelegate() instanceof PropertyDeclaration ?
        ((PropertyDeclaration) anyElement.getDelegate()).getGetter().getAnnotation(JsonIgnore.class) :
        anyElement.getAnnotation(JsonIgnore.class);
      JsonIgnore setterIgnore = anyElement.getDelegate() instanceof PropertyDeclaration ?
        ((PropertyDeclaration) anyElement.getDelegate()).getGetter().getAnnotation(JsonIgnore.class) :
        getterIgnore;
      if (getterIgnore == null || setterIgnore == null) {
        String message = "Properties annotated with @XmlAnyElement should be annotated with @JsonIgnore.";
        if (anyElement.getDelegate() instanceof PropertyDeclaration) {
          message += " (On both the getter and the setter.)";
        }
        result.addError(anyElement, message);
      }
    }

    if (typeDef.isHasAnyAttribute()) {
      if (!isInstanceOf(typeDef, SupportsExtensionAttributes.class.getName())) {
        result.addError(anyElement, "Type definitions that supply the 'any' attribute must implement " +
          SupportsExtensionAttributes.class.getName() + " so the 'any' attributes can be serialized to/from JSON.");
      }

      MemberDeclaration anyAttribute = null;
      for (PropertyDeclaration prop : typeDef.getProperties()) {
        if (prop.getAnnotation(XmlAnyAttribute.class) != null) {
          anyAttribute = prop;
        }
      }

      if (anyAttribute == null) {
        //must be a field.
        for (FieldDeclaration field : typeDef.getFields()) {
          if (field.getAnnotation(XmlAnyAttribute.class) != null) {
            anyAttribute = field;
          }
        }
      }

      if (anyAttribute != null) {
        if (!"extensionAttributes".equals(anyAttribute.getSimpleName())) {
          if (!suppressWarning(anyElement, "gedcomx:unconventional_any_attribute_name")) {
            result.addWarning(anyElement, "The 'any' attribute might be better named 'extensionAttributes' to conform to convention.");
          }
        }

        JsonIgnore getterIgnore = anyAttribute instanceof PropertyDeclaration ?
          ((PropertyDeclaration) anyAttribute).getGetter().getAnnotation(JsonIgnore.class) :
          anyAttribute.getAnnotation(JsonIgnore.class);
        JsonIgnore setterIgnore = anyAttribute instanceof PropertyDeclaration ?
          ((PropertyDeclaration) anyAttribute).getGetter().getAnnotation(JsonIgnore.class) :
          getterIgnore;
        if (getterIgnore == null || setterIgnore == null) {
          String message = "Properties annotated with @XmlAnyAttribute should be annotated with @JsonIgnore.";
          if (anyAttribute instanceof PropertyDeclaration) {
            message += " (On both the getter and the setter.)";
          }
          result.addError(anyAttribute, message);
        }
      }
    }

    return result;
  }

  private boolean isInstanceOf(TypeDefinition typeDef, String name) {
    for (InterfaceType interfaceType : typeDef.getSuperinterfaces()) {
      if (((DecoratedInterfaceType)TypeMirrorDecorator.decorate(interfaceType)).isInstanceOf(name)) {
        return true;
      }
    }

    ClassType superclass = typeDef.getSuperclass();
    if (superclass != null && ((DecoratedClassType) TypeMirrorDecorator.decorate(superclass)).isInstanceOf(name)) {
      return true;
    }

    return false;
  }
}
