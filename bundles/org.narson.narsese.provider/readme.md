# Narson :: Bundles :: Narsese

This bundles provides the Narsese language object model 	 and an implementation of the Reader/Writer and the Parser/Generator services.

## Links

* [Documentation](https://github.com/org.narson/org.narson.reactor.bundles/org.narson.narsese.provider)
* [Source Code](https://github.com/narson/org.narson.reactor.bundles/org.narson.narsese.provider/) (clone with `scm:git:git@github.com/narson.git/org.narson.reactor.bundles/org.narson.narsese.provider`)

## Coordinates

### Maven

```xml
<dependency>
    <groupId>org.narson</groupId>
    <artifactId>org.narson.narsese.provider</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### OSGi

```
Bundle Symbolic Name: org.narson.narsese.provider
Version             : 0.1.0.202004171351
```

## Components

### org.narson.narsese.provider.NarseseLanguage - *state = enabled, activation = delayed*

#### Services - *scope = singleton*

|Interface name |
|--- |
|org.narson.api.narsese.Narsese |

#### Properties

|Name |Type |Value |
|--- |--- |--- |
|prefix.thresold |Integer |3 |
|buffer.size |Integer |1000 |
|charset |String |"UTF-8" |
|truthvalue.frequency |Double |1.0 |
|truthvalue.confidence |Double |0.9 |
|desirevalue.frequency |Double |1.0 |
|desirevalue.confidence |Double |0.9 |

#### Configuration - *policy = optional*

##### Factory Pid: `org.narson.narsese`

|Attribute |Value |
|--- |--- |
|Id |`prefix.thresold` |
|Required |**true** |
|Type |**Integer** |
|Default |3 |

|Attribute |Value |
|--- |--- |
|Id |`buffer.size` |
|Required |**true** |
|Type |**Integer** |
|Default |1000 |

|Attribute |Value |
|--- |--- |
|Id |`charset` |
|Required |**true** |
|Type |**String** |
|Default |"UTF-8" |

|Attribute |Value |
|--- |--- |
|Id |`truthvalue.frequency` |
|Required |**true** |
|Type |**Double** |
|Default |1.0 |

|Attribute |Value |
|--- |--- |
|Id |`truthvalue.confidence` |
|Required |**true** |
|Type |**Double** |
|Default |0.9 |

|Attribute |Value |
|--- |--- |
|Id |`desirevalue.frequency` |
|Required |**true** |
|Type |**Double** |
|Default |1.0 |

|Attribute |Value |
|--- |--- |
|Id |`desirevalue.confidence` |
|Required |**true** |
|Type |**Double** |
|Default |0.9 |

## Developers

* **Clément Delgrange** (cdelg) / [delgrange@pm.me](mailto:delgrange@pm.me) @ [Narson](https://github.com/narson) - *developer*

## Licenses

**Apache License, Version 2.0**
  > The Apache License, Version 2.0
  >
  > For more information see [http://www.opensource.org/licenses/apache2.0.php](http://www.opensource.org/licenses/apache2.0.php).

## Copyright

Narson

---
Narson - [https://github.com/org.narson](https://github.com/org.narson)