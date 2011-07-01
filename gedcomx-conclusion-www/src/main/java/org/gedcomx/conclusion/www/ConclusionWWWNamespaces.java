/**
 * Copyright 2011 Intellectual Reserve, Inc.
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
package org.gedcomx.conclusion.www;

import org.gedcomx.rt.Namespace;
import org.gedcomx.rt.Namespaces;

import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Ryan Heaton
 */
@XmlTransient
@Namespaces ( {
  @Namespace (
    id = "gxcw",
    uri = ConclusionWWWNamespaces.GEDCOMX_CONCLUSION_WWW_NAMESPACE,
    label = "Conclusion WWW Namespace",
    description = "The conclusion www namespace contains the objects necessary for modeling conclusion data exposed via the World Wide Web.",
    version = "v1",
    xmlMediaType = "application/gedcomx-conclusion-www-v1+xml",
    jsonMediaType = "application/gedcomx-conclusion-www-v1+json"
  )
} )
public class ConclusionWWWNamespaces {

  private ConclusionWWWNamespaces() {}

  public static final String GEDCOMX_CONCLUSION_WWW_NAMESPACE = "http://gedcomx.org/conclusion/www/v1";

}