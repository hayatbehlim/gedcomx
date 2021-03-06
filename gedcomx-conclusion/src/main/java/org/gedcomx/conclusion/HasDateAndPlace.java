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
package org.gedcomx.conclusion;

import org.gedcomx.rt.CommonModels;
import org.gedcomx.rt.RDFSubClassOf;

import javax.xml.bind.annotation.XmlType;


/**
 * Conclusion data that has a date and place.
 *
 * @author Ryan Heaton
 */
@RDFSubClassOf ( CommonModels.DUBLIN_CORE_TYPE_NAMESPACE + "Event" )
@XmlType ( name = "HasDateAndPlace" )
public interface HasDateAndPlace {

  /**
   * The date.
   *
   * @return The date.
   */
  Date getDate();

  /**
   * The date.
   *
   * @param date The date.
   */
  void setDate(Date date);

  /**
   * The place.
   *
   * @return The place.
   */
  Place getPlace();

  /**
   * The place.
   *
   * @param place The place.
   */
  void setPlace(Place place);
}
