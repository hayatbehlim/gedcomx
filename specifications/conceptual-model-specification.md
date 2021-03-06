# The GEDCOM X Conceptual Model

## Status

This document specifies a standard conceptual model for exchanging genealogical data,
and requests discussion and suggestions for improvements.

## Copyright Notice

Copyright 2011-2012 Intellectual Reserve, Inc.

## License

This document is distributed under a Creative Commons Attribution-ShareAlike license.
For details, see:

http://creativecommons.org/licenses/by-sa/3.0/

# 1. Introduction

The GEDCOM X conceptual model is a specification of formal concepts and types
that are used to provide a standard model and vocabulary for describing genealogical
data. Genealogical data is structured by data types such as persons, 
relationships, and sources.

## 1.1 Identifier and Version

The identifier for this specification is:

`http://gedcomx.org/conceptual-model/v1`

For convenience, the GEDCOM X conceptual model may be referred to as "GEDCOM X 1.0".
This specification uses "GEDCOM X" internally.

## 1.2 Notational Conventions

This specification uses the term "data type" to refer to a formal description of
a data structure, including the properties that define valid instances of the
data type. For example, information about a person might be contained within a 
data structure that supplies the person's name, birth date, and gender. A "data 
type" defines the formal properties of the data structure.

When a property of a data type is specified as being of a particular data type, the
property inherits the corresponding requirements from that data type's definition.
When a data type is specified as an extension of another data type, the extending
data type inherits the corresponding requirements from the extended data type's
definition.

Data types are defined by the following sections:

1. The *identifier* for the data type, which takes the form of a URI.
2. The *extension* of the data type (if any) which specifies which data type
   is extended by the data type.
3. The *properties* of the data type, which define the information the data type
   encapsulates.

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT",
"SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this
document are to be interpreted as described in BCP 14, 
[RFC2119](http://tools.ietf.org/html/rfc2119), as scoped to those conformance 
targets.



# 2. Common Data Types

Many data types of the GEDCOM X conceptual model share common structures. This section
defines the data types of those shared structures and their requirements for convenient
reference by the other data type definitions.


<a id="uri"/>

## 2.1 The URI

The Uniform Resource Identifier ("URI") is fundamental to the GEDCOM X conceptual model.
The URI is used to identify specific resources, such as persons, relationships, and 
sources. The URI is also used to identify the data structures that describe those resources
and even the data types that define those data structures. The URI is specified by
[RFC3986](http://tools.ietf.org/html/rfc3986).


<a id="identifier-type"/>

## 2.2 The "Identifier" Data Type

The `Identifier` data type defines the data structure used to supply an identifier of a 
genealogical resource in a specific data set.

### identifier

The identifier for the "Identifier" data type is:

`http://gedcomx.org/conclusion/v1/Identifier`

### properties

name  | description | data type
------|-------------|----------
value | The value of the identifier. | string (possibly interpreted as a URI, depending on the type of the identifier).
type  | URI identifying the type of the identifier. | [URI](#uri) - MUST resolve to an identifier type. Refer to the list of [known identifier types](#known-identifier-types).

<a id="known-identifier-types"/>

### known identifier types

The following identifier types are defined by GEDCOM X.

URI | description
----|------------
`http://gedcomx.org/Forwarded` |

### examples

todo: fill in some examples.


<a id="attribution"/>

## 2.3 The "Attribution" Data Type

The `Attribution` data type defines the data structure used to supply the attribution (including
who, when, and why) of genealogical data.

### identifier

The identifier for the "Attribution" data type is:

`http://gedcomx.org/Attribution`

### properties

name  | description | data type
------|-------------|----------
contributor | Reference to the contributor to whom the attributed data is attributed. | [URI](#uri) - MUST resolve to an instance of [`http://xmlns.com/foaf/0.1/Person`](#foaf-person) or [`http://xmlns.com/foaf/0.1/Organization`](#organization).
modified | Timestamp of when the attributed data was contributed. | timestamp
confidence  | Reference to the confidence level of the contributor of the attributed data. | [URI](#uri) - MUST resolve to a confidence level. Refer to the list of [known confidence levels](#known-confidence-levels).
changeMessage | A statement of why the attributed data is being provided by the contributor. | string

<a id="known-confidence-levels"/>

### known confidence levels

The following confidence levels are defined by GEDCOM X. For more information, refer to 
Mills, Elizabeth Shown. "Fundamentals of Evidence Analysis." <i>Evidence Explained.</i> 2nd ed. 
(Baltimore, Maryland: Genealogical Publishing Company, 2009), 19-20 (Section 1.6).

URI | description
----|------------
`http://gedcomx.org/Certainly`|The contributor has no reasonable doubt about the assertion, based upon sound research and good evidence.
`http://gedcomx.org/Probably`|The contributor feels the assertion is more likely than not, based upon sound research and good evidence.
`http://gedcomx.org/Possibly`|The contributor feels some evidence supports the assertion, but the assertion is far from proved.
`http://gedcomx.org/Likely`|The contributor feels the odds weigh at least slightly in favor of the assertion.
`http://gedcomx.org/Apparently`|The contributor has formed an impression or presumption, typically based upon common experience, but has not tested the matter.
`http://gedcomx.org/Perhaps`|The contributor suggests that an idea is plausible, although it remains to be tested.

### examples

todo:


<a id="formal-value"/>

## 2.4 The "FormalValue" Data Type

The `FormalValue` data type defines the data structure used to supply a normalized and/or
standardized value. The formal value is used to supply a formal interpretation of a
value that has been supplied by a user. If the value has been reformatted for the purpose of easier
processing (e.g. for display purposes), it is said to be "normalized". If the value has
been resolved to a discrete, machine-identifiable value based on a specific standard, it is
said to be "standardized".

The `FormalValue` data type does NOT support extension properties (see [Extension Properties](#extension-properties)).

### identifier

The identifier for the "FormalValue" data type is:

`http://gedcomx.org/conclusion/v1/FormalValue`

### properties

name  | description | data type
------|-------------|----------
value | A string supplying the value of the formal value. If the value has been standardized, a datatype will be supplied to identify how the string is to be parsed. | string
datatype  | URI identifying the way the value is to be processed according to a specific standard. | [URI](#uri)
resource | URI identifying the resource to which the formal value has been standardized. | [URI](#uri)

If a value is supplied for the `datatype` property, a value SHALL NOT be supplied for the `resource` property.
If a value is supplied for the `resource` property, a value SHALL NOT be supplied for the `datatype` property.

### examples

* A user supplies the text "jan 1 1890" as the value of a genealogical date. If a process
  (either automated or by user-interaction) normalizes this text to "1 January 1890", the formal
  value containing the results of this normalization will have a value of "1 January 1890"
  and no values specified for either the datatype or the resource.
* A user supplies the text "jan 1 1890" as the value of a genealogical date. If a process
  (either automated or by user-interaction) standardizes this text to a specific date such as
  midnight UTC of the first day of January of the year 1890 A.D. of the Gregorian calendar,
  the formal value containing the results of this standardization might have a value of
  "1890-01-01T00:00:00Z" and a datatype of "http://www.w3.org/2001/XMLSchema#dateTime" and no value
  specified for the resource.
* A user supplies "boston, MA" as the value for a genealogical place. If a process (either
  automated or by user-interaction) standardizes this place to a unique resource (e.g. the
  actual city know today as Boston, Massachusetts) identified by a specific URI (e.g.
  "http://places.com/12345"), the formal value containing the results of this standardization
  will have "http://places.com/12345" as the value of its "resource" property. The formal value
  MAY also supply a value for the "value" property that contains the results of normalizing
  the user-supplied text (e.g. "Boston, Suffolk, Massachusetts, United States").


<a id="note"/>

## 2.5 The "Note" Data Type

The `Note` data type defines a note that was contributed from genealogical research.

Notes are not intended to contain genealogical conclusions.  Notes are only associated with a single genealogical resource.

### identifier

The identifier for the "Note" data type is:

`http://gedcomx.org/Note`

### properties

name  | description | data type
------|-------------|----------
text | The text of the note. | [`http://gedcomx.org/TextValue`](#text-value)
attribution | The attribution of this note. | [`http://gedcomx.org/Attribution`](#attribution)


<a id="literal-value"/>

## 2.6 The "LiteralValue" Data Type

The `LiteralValue` data type defines a literal value.

### identifier

The identifier for the "LiteralValue" data type is:

`http://gedcomx.org/Literal`

### properties

name  | description | data type
------|-------------|----------
datatype  | URI identifying the way the value is to be processed according to a specific standard. | [URI](#uri)
lang | The language of the literal value. | `http://www.w3.org/XML/1998/namespace#lang`
value | The literal value. A datatype MAY be supplied to identify how the string is to be parsed. | string


<a id="text-value"/>

## 2.7 The "TextValue" Data Type

The `TextValue` data type defines a literal value.

### identifier

The identifier for the "TextValue" data type is:

`http://gedcomx.org/TextValue`

### properties

name  | description | data type
------|-------------|----------
lang | The language of the literal value. | `http://www.w3.org/XML/1998/namespace#lang`
value | The literal value. A datatype MAY be supplied to identify how the string is to be parsed. | string



# 3. Data Types for Describing Sources

Citing sound evidence is a critical component to the exchange of genealogical data. The source of
a piece of genealogical data has to be completely and accurately described in order to be
consistently evaluated by other genealogical researchers. Many sources do not have a digital
representation (e.g. books or other physical artifacts). Sources are instead _described_
and their _description_ is provided as the digital representation of a source.

This section defines the data types that are used for describing and referencing sources.


<a id="source-description"/>

## 3.1 The "SourceDescription" Data Type

The `SourceDescription` data type defines a description of a source.

### identifier

The identifier for the "SourceDescription" data type is:

`http://gedcomx.org/source/v1/SourceDescription`

### properties

name | description | data type
-----|-------------|----------
id | A local, transient identifier for the resource being described. Note that as a local, transient identifier, the id may only be used to resolve references to the resource within a well-defined scope (such as a single web service request or a single file). | string
citation | The citation for this source. | [`http://gedcomx.org/source/v1/SourceCitation`](#source-citation) - REQUIRED
about | A uniform resource identifier (URI) for the resource being described. | [URI](#uri) - OPTIONAL
mediator | A reference to the entity that mediates access to the described source. | [URI](#uri) - OPTIONAL; MUST resolve to an instance of [`http://xmlns.com/foaf/0.1/Person`](#foaf-person) or [`http://xmlns.com/foaf/0.1/Organization`](#organization).
sources | A list of references to any sources from which this source is derived. | List of [`http://gedcomx.org/source/v1/SourceReference`](#source-reference) - OPTIONAL
componentOf | A reference to the source that contains this source -- its parent context; this is for cases where this description is not complete without the description of its parent context | [`http://gedcomx.org/source/v1/SourceReference`](#source-reference) - OPTIONAL
displayName | A display name for this source. | string - OPTIONAL
alternateNames | A list of alternate display names for this source. | List of [`http://gedcomx.org/TextValue`](#text-value) - OPTIONAL
notes  | A list of notes about a source. | List of [`http://gedcomx.org/Note`](#note) - OPTIONAL
attribution | The attribution of this source description. | [`http://gedcomx.org/Attribution`](#attribution)

<a id="source-citation"/>

## 3.2 The "SourceCitation" Data Type

The `SourceCitation` data type defines a container for the metadata necessary to identify a source(s)
and from which bibliographic citation strings may be rendered.

### identifier

The identifier for the "SourceCitation" data type is:

`http://gedcomx.org/source/v1/SourceCitation`

### properties

name | description | data type
-----|-------------|----------
value | A rendering of the full (working) citation as a string. | string - REQUIRED
citationTemplate | The identifier of the citation template by which this citation may be interpreted. | [URI](#uri) - OPTIONAL;  MUST resolve to an instance of [`http://gedcomx.org/source/v1/CitationTemplate`](#citation-template).
fields  | A list of citation fields about a source. | List of [`http://gedcomx.org/source/v1/CitationField`](#citation-field) - OPTIONAL

<a id="citation-field"/>

## 3.3 The "CitationField" Data Type

The `CitationField` data type defines a piece of metadata (e.g., author, volume, page, publisher, etc.)
necessary to identify a source.

### identifier

The identifier for the "CitationField" data type is:

`http://gedcomx.org/source/v1/CitationField`

### properties

name | description | data type
-----|-------------|----------
name | The identifier for the citation detail -- defined by a citation template or a citation template library. | [URI](#uri) - REQUIRED
value | The value of the citation detail. | string - OPTIONAL


<a id="source-reference"/>

## 3.4 The "SourceReference" Data Type

The `SourceReference` data type defines a reference to a source.  A genealogical conclusion or a
derivate source [the referring object -- the object holding the `SourceReference` instance] cites
its supporting source(s) [the target source(s)] using an instance(s) of `SourceReference`.

### identifier

The identifier for the "SourceReference" data type is:

`http://gedcomx.org/source/v1/SourceReference`

### properties

name | description | data type
-----|-------------|----------
sourceDescription  | Reference to a _description_ of the target source. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/source/v1/SourceDescription`](#source-description)
attribution | The attribution of this source reference. | [`http://gedcomx.org/Attribution`](#attribution)

### examples

todo:

<a id="citation-template"/>

## 3.5 The "CitationTemplate" Data Type

todo: define citation templates and any associated infrastructure

<a id="note-about-citation-templates"/>
<table>
  <tr>
    <td style="background:#F0F0FF;border-color:#F0F0FF; padding-bottom:0px; font-size:18px">
<b>a note about citation templates (not part of this specification)</b>
    </td>
  </tr>
  <tr>
    <td style="background:#F0F0FF;border-color:#F0F0FF; padding-top:0px">
<p>
Building source citations is said to be an "art" and requires a great deal of flexibility.  While citation style
guides exist (e.g. Chicago style, Turabian, Evidence Style -- see <i>Evidence Explained</i> by Elizabeth Shown Mills,
etc.), they are considered guides and execution within their guidelines allows for flexibility.
No one style has been universally accepted, nor will a style become universally accepted in the forseeable future.  Therefore,
the approach to collecting citation metadata and the exchange of this data needs to support flexibility.
</p><p/><p>
The citation metadata is collected as a set of name-value pairs (see the CitationField data type), but there still remains
a set of difficult questions, including:</p>
<ul>
  <li>What is the set of valid "names" (e.g. controlled vocabulary)?</li>
  <li>What "values" are valid values for a given "name"?</li>
  <li>What fields (name-value pairs) are required, and under what circumstances might they be optional?</li>
  <li>What metadata needs to be collected to properly site data in a specific record?  For example, is a citation for the 1900 US Census
different from a citation for the 1910 US Census and from a citation for the 1911 England and Wales Census?</li>
</ul>
<p/><p>
Further questions remain about how to arrange the fields into citations. For example, given a set of metadata, can it be expressed
in the Chicago style?  The Evidence style?  What about a lesser-used or custom style?
</p><p/><p>
To address these design issues, we define <b><i>citation templates</i></b>.  A citation template defines the metadata that should
be collected for a given source and the semantics associated with that metadata.  Templates also specify how the metadata
is rendered into specific citation styles, such as Chicago style or Evidence style.  Templates could be designed to
address specific record types and may be grouped by type (e.g., a group of templates for census records, or newspapers, or
US census records, or UK census records, etc.).  Templates could also be associated with other templates into libraries
such that a specific piece of citation metadata (e.g., a citation field named "Volume") has a shared semantic meanign across the set
of associated templates -- a template library. Perhaps a standard template library could be developed that would address the most
common citation needs.
</p><p/><p>
Citation templates have not been fully specified and are therefore outlined here in broad terms.  For now, this
section functions as a place holder for the needed specifications.  We will likely consider ideas like those expressed
at http://sourcetemplates.org/ when we define this portion of the specification.
</p>
    </td>
  </tr>
</table>



# 4. Data Types for Describing Contributors

Genealogical research is performed and data is gathered by a living person or organization.
This section describes the data types that are used to provide information about
contributors of genealogical data.

GEDCOM X refers to the [Friend of a Friend](http://www.foaf-project.org/) project to define the
data types for describing contributors. The data types are duplicated here for convenience
and consistency.


<a id="online-account"/>

## 4.1 The "OnlineAccount" Data Type

The `OnlineAccount` data type defines a description of an account in an online web application.

### identifier

The identifier for the `OnlineAccount` data type is:

`http://xmlns.com/foaf/0.1/OnlineAccount`

### properties

name | description | data type
-----|-------------|----------
id | A local identifier for the online account. Note that this id MUST NOT be processed as an identifier for the resource being referenced, but instead as a transient identifier for the reference itself. | string
serviceHomepage  | The home page of the service. | [URI](#uri)
accountName | The name of the account. | [`http://gedcomx.org/Literal`](#literal-value)
displayName | A display name for the account. | [`http://gedcomx.org/Literal`](#literal-value)

<a id="address"/>

## 4.2 The "Address" Data Type

The `Address` data type defines a street address of a person or organization. The `Address` data type is
defined by the [Contact](http://www.w3.org/2000/10/swap/pim/contact) schema.

### identifier

The identifier for the `Address` data type is:

`http://www.w3.org/2000/10/swap/pim/contact#Address`

### properties

name | description | data type
-----|-------------|----------
id | A local identifier for the address. Note that this id MUST NOT be processed as an identifier for the resource being referenced, but instead as a transient identifier for the reference itself. | string
city | The city. | string
country | The country. | string
postalCode | The postal code. | string
stateOrProvince | The state or province. | string
street | The street. | string
street2 | The street (second line). | string
street3 | The street (third line). | string


## 4.3 The "Agent" Data Type

The `Agent` data type defines data that is common to a living entity, such as a person (user)
or organization. The `Agent` data type use used as an abstraction to share properties
between contributor data types such as FOAF `Person` and `Organization`.

### identifier

The identifier for the `Agent` data type is:

`http://xmlns.com/foaf/0.1/Agent`

### properties

name | description | data type
-----|-------------|----------
id | A local identifier for the agent. Note that this id MUST NOT be processed as an identifier for the resource being referenced, but instead as a transient identifier for the reference itself. | string
name | The name of the person or organization. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)
homepage | The homepage of the person or organization. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)
openid  | The [openid](http://openid.net/) of the person or organization. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)
accounts  | The online accounts of the person or organization. | List of [`http://xmlns.com/foaf/0.1/OnlineAccount`](#online-account). Order is preserved.
emails  | The email addresses of the person or organization. | List of [URI](#uri) - MUST resolve to a valid e-mail address (e.g. "mailto:someone@gedcomx.org"). Order is preserved.
phones  | The phones (voice, fax, mobile) of the person or organization. | List of [URI](#uri) - MUST resolve to a valid phone number (e.g. "tel:+1-201-555-0123"). Order is preserved.
addresses  | The addresses of the person or organization. | List of [`http://www.w3.org/2000/10/swap/pim/contact#Address`](#address). Order is preserved.


<a id="organization"/>

## 4.4 The "Organization" Data Type

The `Organization` data type defines data describing an organization.

### identifier

The identifier for the `Organization` data type is:

`http://xmlns.com/foaf/0.1/Organization`

### extension

This data type extends the following data type:

`http://xmlns.com/foaf/0.1/Agent`

### properties

The `Organization` data type defines no additional properties beyond those defined by
its extended type.


<a id="foaf-person"/>

## 4.5 The "FOAF Person" Data Type

The `Person` data type defines data that is common to a living person (user). The `Person`
data type is an extension of the `Agent` data type.

### identifier

The identifier for the `Person` data type is:

`http://xmlns.com/foaf/0.1/Person`

### extension

This data type extends the following data type:

`http://xmlns.com/foaf/0.1/Agent`

### properties

name | description | data type
-----|-------------|----------
familyName | The family name of the person. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)
givenName | The given name of the person. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)
language | The language of the person. | [`http://www.w3.org/2000/01/rdf-schema#Literal`](#rdf-literal)



# 5. Data Types for Describing Conclusions

This section describes the data types that are used to define genealogical conclusions.


<a id="conclusion"/>

## 5.1 The "Conclusion" Data Type

The `Conclusion` data type defines the base conceptual model for basic genealogical conclusions.

### identifier

The identifier for the `Conclusion` data type is:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
id | A local identifier for the conclusion. Note that this id MUST NOT be processed as an identifier for the resource being referenced, but instead as a transient identifier for the reference itself. | string
sources | The list of references to the sources of related to this conclusion. The sources of a conclusion MUST also be sources of the conclusion's containing entity (i.e. [`Person`](#person) or [`Relationship`](#relationship) ).| List of [`http://gedcomx.org/conclusion/v1/SourceReference`](#source-reference). Order is preserved.
notes  | A list of notes about a conclusion. | List of [`http://gedcomx.org/Note`](#note) - OPTIONAL
attribution | The attribution of this conclusion. | [`http://gedcomx.org/Attribution`](#attribution)


<a id="document"/>

## 5.2 The "Document" Data Type

The `Document` data type defines the base conceptual model for genealogical conclusions that are managed as textual documents.  The `Document` data type extends the `Conclusion` data type.

### identifier

The identifier for the `Document` data type is:

`http://gedcomx.org/conclusion/v1/Document`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
text | The text of the document. | [`http://gedcomx.org/TextValue`](#text-value)


<a id="abstract-document"/>

## 5.2.1 The "AbstractDocument" Data Type

The `AbstractDocument` data type is used to represent document abstracts.  The `AbstractDocument` data type extends the `Document` data type.

### identifier

The identifier for the `AbstractDocument` data type is:

`http://gedcomx.org/conclusion/v1/AbstractDocument`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Document`

### properties

The `AbstractDocument` data type defines no additional properties beyond those defined by its extended type.


<a id="transcription-document"/>

## 5.2.2 The "TranscriptionDocument" Data Type

The `TranscriptionDocument` data type is used to represent document transcriptions -- to include partial transcriptions (also called extracts).  The `TranscriptionDocument` data type extends the `Document` data type.

### identifier

The identifier for the `TranscriptionDocument` data type is:

`http://gedcomx.org/conclusion/v1/TranscriptionDocument`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Document`

### properties

The `TranscriptionDocument` data type defines no additional properties beyond those defined by its extended type.


<a id="translation-document"/>

## 5.2.3 The "TranslationDocument" Data Type

The `TranslationDocument` data type is used to represent document translations.  The `TranslationDocument` data type extends the `Document` data type.

### identifier

The identifier for the `TranslationDocument` data type is:

`http://gedcomx.org/conclusion/v1/TranslationDocument`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Document`

### properties

The `TranslationDocument` data type defines no additional properties beyond those defined by its extended type.


<a id="analysis-document"/>

## 5.2.4 The "AnalysisDocument" Data Type

The `AnalysisDocument` data type is used to represent documents with evidence analysis -- e.g., a genealogical proof statement.  The `AnalysisDocument` data type extends the `Document` data type.

### identifier

The identifier for the `AnalysisDocument` data type is:

`http://gedcomx.org/conclusion/v1/AnalysisDocument`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Document`

### properties

The `AnalysisDocument` data type defines no additional properties beyond those defined by its extended type.


<a id="gender-conclusion"/>

## 5.3 The "Gender" Data Type

The `Gender` data type defines a conclusion about the gender of a person. the `Gender` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Gender` data type is:

`http://gedcomx.org/conclusion/v1/Gender`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the gender. | [URI](#uri) - MUST resolve to a gender type. Refer to the list of [known gender types](#known-gender-types).

<a id="known-gender-types"/>

### known gender types

The following gender types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Male`|
`http://gedcomx.org/Female`|
`http://gedcomx.org/Unknown`|


<a id="name-conclusion"/>

## 5.4 The "Name" Data Type

The `Name` data type defines a conclusion about a name of a person. The `Name` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Name` data type is:

`http://gedcomx.org/conclusion/v1/Name`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the name. | [URI](#uri) - MUST resolve to a name type. Refer to the list of [known name types](#known-name-types).
preferred | Whether this name is preferred above the other names of a person. | boolean
primaryForm | The primary form of the name. | `http://gedcomx.org/conclusion/v1/NameForm`
alternateForms | The alternate forms of the name. | List of [`http://gedcomx.org/conclusion/v1/NameForm`](#name-form). Order is preserved.

<a id="known-name-types"/>

### known name types

The following name types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Name`|
`http://gedcomx.org/BirthName`|
`http://gedcomx.org/DeathName`|
`http://gedcomx.org/MarriedName`|
`http://gedcomx.org/AlsoKnownAs`|
`http://gedcomx.org/MaidenName`|
`http://gedcomx.org/Nickname`|
`http://gedcomx.org/Adoptive`|
`http://gedcomx.org/Formal`|
`http://gedcomx.org/Religious`|


<a id="fact-conclusion"/>

## 5.5 The "Fact" Data Type

The `Fact` data type defines a conclusion about a fact of the life of a person or
the nature of a relationship. The `Fact` data type extends the `Conclusion` data type.

### identifier

The identifier for the `Fact` data type is:

`http://gedcomx.org/conclusion/v1/Fact`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the fact. | [URI](#uri) - MUST resolve to a fact type. See the list of [known fact types](#known-fact-types).
date | The date of applicability of the fact. | [`http://gedcomx.org/conclusion/v1/Date`](#conclusion-date)
place | The place of applicability of the fact. | [`http://gedcomx.org/conclusion/v1/Place`](#conclusion-place)
original | The value of the fact as supplied by the contributor. | string
formal | The formal value of the fact. | [`http://gedcomx.org/conclusion/v1/FormalValue`](#formal-value)

<a id="known-fact-types"/>

### known fact types

The following fact types are defined by GEDCOM X:

URI | description | scope
----|-------------|------
`http://gedcomx.org/Adoption`| | person
`http://gedcomx.org/AdultChristening`| | person
`http://gedcomx.org/Affiliation`| | person
`http://gedcomx.org/Age`| | person
`http://gedcomx.org/Baptism`| | person
`http://gedcomx.org/BarMitzvah`| | person
`http://gedcomx.org/BatMitzvah`| | person
`http://gedcomx.org/Birth`| | person
`http://gedcomx.org/Blessing`| | person
`http://gedcomx.org/Burial`| | person
`http://gedcomx.org/CasteName`| | person
`http://gedcomx.org/Census`| | person
`http://gedcomx.org/Christening`| | person
`http://gedcomx.org/Circumcision`| | person
`http://gedcomx.org/Citizenship`| | person
`http://gedcomx.org/ClanName`| | person
`http://gedcomx.org/Confirmation`| | person
`http://gedcomx.org/CountOfChildren`| | person
`http://gedcomx.org/CountOfMarriages`| | person
`http://gedcomx.org/Cremation`| | person
`http://gedcomx.org/Death`| | person
`http://gedcomx.org/DiedBeforeEight`| | person
`http://gedcomx.org/Dwelling`| | person
`http://gedcomx.org/Education`| | person
`http://gedcomx.org/Emigration`| | person
`http://gedcomx.org/Ethnicity`| | person
`http://gedcomx.org/Excommunication`| | person
`http://gedcomx.org/FirstCommunion`| | person
`http://gedcomx.org/Flourish`| | person
`http://gedcomx.org/Funeral`| | person
`http://gedcomx.org/GedcomUUID`| | person
`http://gedcomx.org/Illness`| | person
`http://gedcomx.org/Immigration`| | person
`http://gedcomx.org/Interment`| | person
`http://gedcomx.org/Living`| | person
`http://gedcomx.org/MaritalStatus`| | person
`http://gedcomx.org/MilitaryAward`| | person
`http://gedcomx.org/MilitaryCompany`| | person
`http://gedcomx.org/MilitaryDischarge`| | person
`http://gedcomx.org/MilitaryRank`| | person
`http://gedcomx.org/MilitaryRegiment`| | person
`http://gedcomx.org/MilitaryService`| | person
`http://gedcomx.org/MilitaryServiceBranch`| | person
`http://gedcomx.org/Mission`| | person
`http://gedcomx.org/Move`| | person
`http://gedcomx.org/NameOfShip`| | person
`http://gedcomx.org/Naturalization`| | person
`http://gedcomx.org/Namesake`| | person
`http://gedcomx.org/NationalId`| | person
`http://gedcomx.org/Nationality`| | person
`http://gedcomx.org/NeverHadChildren`| | person
`http://gedcomx.org/NeverMarried`| | person
`http://gedcomx.org/NotAccountable`| | person
`http://gedcomx.org/Occupation`| | person
`http://gedcomx.org/Ordinance`| | person
`http://gedcomx.org/Ordination`| | person
`http://gedcomx.org/PhysicalDescription`| | person
`http://gedcomx.org/PortOfDeparture`| | person
`http://gedcomx.org/PreviousResidence`| | person
`http://gedcomx.org/Probate`| | person
`http://gedcomx.org/Property`| | person
`http://gedcomx.org/Race`| | person
`http://gedcomx.org/RelationshipToHead`| | person
`http://gedcomx.org/Religion`| | person
`http://gedcomx.org/Residence`| | person
`http://gedcomx.org/Retirement`| | person
`http://gedcomx.org/SocialSecurityNumber`| | person
`http://gedcomx.org/Stillborn`| | person
`http://gedcomx.org/TitleOfNobility`| | person
`http://gedcomx.org/TribeName`| | person
`http://gedcomx.org/Twin`| | person
`http://gedcomx.org/Will`| | person
`http://gedcomx.org/Annulment`| | couple relationship
`http://gedcomx.org/CommonLawMarriage`| | couple relationship
`http://gedcomx.org/CurrentlySpouses`| | couple relationship
`http://gedcomx.org/Divorce`| | couple relationship
`http://gedcomx.org/DivorceFiling`| | couple relationship
`http://gedcomx.org/Engagement`| | couple relationship
`http://gedcomx.org/Marriage`| | couple relationship
`http://gedcomx.org/MarriageBanns`| | couple relationship
`http://gedcomx.org/MarriageContract`| | couple relationship
`http://gedcomx.org/MarriageIntent`| | couple relationship
`http://gedcomx.org/MarriageLicense`| | couple relationship
`http://gedcomx.org/MarriageNotice`| | couple relationship
`http://gedcomx.org/MarriageSettlement`| | couple relationship
`http://gedcomx.org/NumberOfChildren`| | couple relationship
`http://gedcomx.org/Separation`| | couple relationship
`http://gedcomx.org/UniversalId`| | couple relationship
`http://gedcomx.org/Biological`| | parent-child relationship
`http://gedcomx.org/Adopted`| | parent-child relationship
`http://gedcomx.org/Step`| | parent-child relationship
`http://gedcomx.org/Foster`| | parent-child relationship
`http://gedcomx.org/Guardianship`| | parent-child relationship


<a id="person"/>

## 5.6 The "Person" Data Type

The `Person` data type defines a description of a person. The `Person` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Person` data type is:

`http://gedcomx.org/conclusion/v1/Person`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
identifiers | Identifiers for the person. | List of [`http://gedcomx.org/conclusion/v1/Identifier`](#identifier-type). Order is preserved.
living | Whether the person is considered living. | boolean
gender | The conclusion about the gender of the person. | [`http://gedcomx.org/conclusion/v1/Gender`](#gender)
names | The conclusions about the names of the person. | List of [`http://gedcomx.org/conclusion/v1/Name`](#name-conclusion). Order is preserved.
facts | The conclusions about the facts of the life of the person. | List of [`http://gedcomx.org/conclusion/v1/Fact`](#fact-conclusion). Order is preserved.


<a id="relationship"/>

## 5.7 The "Relationship" Data Type

The `Relationship` data type describes the relationship between two persons. The `Relationship` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Relationship` data type is:

`http://gedcomx.org/conclusion/v1/Relationship`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the relationship. | [URI](#uri) - MUST resolve to a relationship type. Refer to the list of [known relationship types](#known-relationship-types)
person1 | Reference to the first person in the relationship. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/conclusion/v1/Person`](#person)
person2 | Reference to the second person in the relationship. | [URI](#uri) - MUST resolve to an instance of [`http://gedcomx.org/conclusion/v1/Person`](#person)
facts | The conclusions about the facts of the life of the relationship. | List of [`http://gedcomx.org/conclusion/v1/Fact`](#fact-conclusion). Order is preserved.

Note: when a relationship type implies direction, the relationship is said to
be *from* person1 *to* person2. For example, in a parent-child relationship, the
`person1` property refers to the parent and the `person2` property refers to the
child.

<a id="known-relationship-types"/>

### known relationship types

The following relationship types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Couple`|
`http://gedcomx.org/ParentChild`|


<a id="conclusion-event-role"/>

## 5.8 The "EventRole" Data Type

The `EventRole` data type defines a role played in an event by a person.  The `EventRole` data type extends the `Conclusion` data type.

### identifier

The identifier for the `EventRole` data type is:

`http://gedcomx.org/conclusion/v1/EventRole`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
person | Reference to the person playing the role in the event. | [`URI`](#uri) - MUST resolve to an instance of [`http://gedcomx.org/conclusion/v1/Person`](#person)
type | Reference to the role type. | [`URI`](#uri) - MUST resolve to a role type. Refer to the list of [known role types](#known-roles).
details | Details about the role of the person in the event. | string

<a id="known-roles"/>

### known role types

The following role types are defined by GEDCOM X:

URI | description
----|------------
`http://gedcomx.org/Principal`|
`http://gedcomx.org/Participant`|
`http://gedcomx.org/Official`|
`http://gedcomx.org/Witness`|


<a id="event"/>

## 5.9 The "Event" Data Type

The `Event` data type defines a description of a historical event. The `Event` data type
extends the `Conclusion` data type.

### identifier

The identifier for the `Event` data type is:

`http://gedcomx.org/conclusion/v1/Event`

### extension

This data type extends the following data type:

`http://gedcomx.org/conclusion/v1/Conclusion`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the event. | [URI](#uri). MUST resolve to an event type. Refer to the list of [known event types](#known-event-types)
date | The date of the event. | [`http://gedcomx.org/conclusion/v1/Date`](#conclusion-date)
place | The place of the event. | [`http://gedcomx.org/conclusion/v1/Place`](#conclusion-place)
roles | The roles of the persons in the event. | List of [`http://gedcomx.org/conclusion/v1/EventRole`](#conclusion-event-role). Order is preserved.

<a id="known-event-types"/>

### known event types

The following event types are defined by GEDCOM X:

URI | description
----|------------
`http://gedcomx.org/Adoption`|
`http://gedcomx.org/AdultChristening`|
`http://gedcomx.org/Annulment`|
`http://gedcomx.org/Arrival`|
`http://gedcomx.org/Baptism`|
`http://gedcomx.org/BarMitzvah`|
`http://gedcomx.org/BatMitzvah`|
`http://gedcomx.org/Birth`|
`http://gedcomx.org/Blessing`|
`http://gedcomx.org/Burial`|
`http://gedcomx.org/Census`|
`http://gedcomx.org/Christening`|
`http://gedcomx.org/Circumcision`|
`http://gedcomx.org/Confirmation`|
`http://gedcomx.org/Cremation`|
`http://gedcomx.org/Death`|
`http://gedcomx.org/Departure`|
`http://gedcomx.org/Divorce`|
`http://gedcomx.org/DivorceFiling`|
`http://gedcomx.org/Education`|
`http://gedcomx.org/Engagement`|
`http://gedcomx.org/Emigration`|
`http://gedcomx.org/Excommunication`|
`http://gedcomx.org/FirstCommunion`|
`http://gedcomx.org/Funeral`|
`http://gedcomx.org/Graduation`|
`http://gedcomx.org/Immigration`|
`http://gedcomx.org/Interment`|
`http://gedcomx.org/Marriage`|
`http://gedcomx.org/MilitaryAward`|
`http://gedcomx.org/MilitaryDischarge`|
`http://gedcomx.org/Mission`|
`http://gedcomx.org/Move`|
`http://gedcomx.org/Ordinance`|
`http://gedcomx.org/Ordination`|
`http://gedcomx.org/Retirement`|


<a id="conclusion-date"/>

## 5.10 The "Date" Data Type

The `Date` data type defines the value of a genealogical date.

### identifier

The identifier for the `Date` data type is:

`http://gedcomx.org/conclusion/v1/Date`

### properties

name | description | data type
-----|-------------|----------
original | The original value of the date as supplied by the contributor. | string
formal | The formal value of the date. | [`http://gedcomx.org/FormalValue`](#formal-value)

### known date formats

The following date formats are recognized by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/GEDCOM_5_5` | The date format specified by the GEDCOM 5.5 specification.
`iso:8601` | The date format specified by [ISO 8601](http://en.wikipedia.org/wiki/ISO_8601).


<a id="conclusion-place"/>

## 5.11 The "Place" Data Type

The `Place` data type defines the value of a genealogical place.

### identifier

The identifier for the `Place` data type is:

`http://gedcomx.org/conclusion/v1/Place`

### properties

name | description | data type
-----|-------------|----------
original | The original value of the place as supplied by the contributor. | string
formal | The formal value of the place. | [`http://gedcomx.org/FormalValue`](#formal-value)


<a id="name-part"/>

## 5.12 The "NamePart" Data Type

The `NamePart` data type defines a part of a name of a person.

### identifier

The identifier for the `NamePart` data type is:

`http://gedcomx.org/conclusion/v1/NamePart`

### properties

name | description | data type
-----|-------------|----------
type | URI identifying the type of the name part. | [URI](#uri) - MUST resolve to a name part type. Refer to the list of [known name part types](#known-name-part-types).
value | The value of the name part. | string

<a id="known-name-part-types"/>

### known name part types

The following name part types are defined by GEDCOM X:

URI | description
----|-------------
`http://gedcomx.org/Prefix`|
`http://gedcomx.org/Suffix`|
`http://gedcomx.org/Given`|
`http://gedcomx.org/Surname`|

<a id="name-form"/>

## 5.13 The "NameForm" Data Type

The `NameForm` data type defines a form of a name of a person.

### identifier

The identifier for the `NameForm` data type is:

`http://gedcomx.org/conclusion/v1/NameForm`

### properties

name | description | data type
-----|-------------|----------
fullText | The full text of the name form. | string
parts | The parts of the name form. | List of [`http://gedcomx.org/conclusion/v1/NamePart`](#name-part). Order is preserved.


# 6. Extensibility

## Extensions from Non-GEDCOM X Vocabularies

This specification provides the data types and properties that define GEDCOM X.
Other data types and vocabularies ("foreign vocabularies") can be used in
GEDCOM X data types.

## Extensions to the GEDCOM X Vocabulary

The GEDCOM X namespaces are reserved for future forward-compatible revisions
of GEDCOM X. Future versions of this specification could add new data types and
properties to the GEDCOM X vocabulary.  Software written to conform to this
version of the specification will not be able to process such extensions correctly
and, in fact, will not be able to distinguish such extensions from error.  For
the purposes of this discussion, unrecognized data types and properties from the
GEDCOM X vocabulary will be considered "foreign vocabularies".

## Processing Foreign Vocabularies

GEDCOM X Processors that encounter foreign vocabularies in a location that is
legal according to this specification MUST NOT stop processing or
signal an error.  It might be the case that the GEDCOM X Processor is
able to process the foreign vocabularies correctly and does so.  Otherwise,
such vocabularies are termed "unknown foreign vocabularies".

When unknown foreign vocabularies are encountered, GEDCOM X Processors MAY
bypass the foreign properties, type references, and textual content and
MUST NOT change their behavior as a result of the presence of the vocabulary.

<a id="extension-properties"/>

## Extension Properties

GEDCOM X allows properties of foreign vocabularies in any data type, except
where it is explicitly forbidden.

## Extension Types

GEDCOM X allows data types of foreign vocabularies to be referenced anywhere
data types are defined to be referenced. Examples of data type references include
name types, fact types, resource types, etc. GEDCOM X supplies its own set of
"known" data types and implementors are encouraged to use the list of known data
types as much as possible to preserve maximum compatibility.

## User-Defined Types

Some applications MAY allow data types to be provided. For example, a genealogy
application may provide a feature that allows a user to supply a "custom" fact
about the life of a person when the list of known fact types doesn't contain
the type of fact the user may want to add.

In the case where a user has supplied a title or description instead of selecting
a known data type, GEDCOM X recognizes the data URI scheme as defined by
[RFC 2397](http://tools.ietf.org/html/rfc2397).

# 7. Miscellaneous To Do

todo: add details about which properties are required.

todo: supply details about how GEDCOM X defines its evidence model.
