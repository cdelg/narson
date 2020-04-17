# Narson

Narson is something.

## Links

* [Documentation](https://github.com/org.narson)
* [Source Code](https://github.com/narson/) (clone with `scm:git:git@github.com/narson.git`)

## Built Artifacts

* [**Narson :: BOM**](bom): This projects builds a set of BOM files useful to manage project
	 dependencies and to create OSGi repository indexes.
  * [**Narson :: BOM :: Compile**](bom/org.narson.bom.compile): This project defines a BOM file with the dependencies
	 used to compile the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
  * [**Narson :: BOM :: Bundles**](bom/org.narson.bom.bundles): This project defines a BOM file of the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
  * [**Narson :: BOM :: Runtime**](bom/org.narson.bom.runtime): This project defines a BOM file with the dependencies
	 used to run the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
  * [**Narson :: BOM :: Test**](bom/org.narson.bom.test): This project defines a BOM file with the dependencies
	 used to test the Narson bundles. An OSGi repository index is created at
	  build time and used during the integration tests.
* [**Narson :: Bundles**](bundles): This project builds the Narson bundles. This is where the magic happens.
  * [**Narson :: Bundles :: Api**](bundles/org.narson.api): This bundles provides the API contract for all the Narson bundles.
  * [**Narson :: Bundles :: Narsese**](bundles/org.narson.narsese.provider): This bundles provides the Narsese language object model 	 and an implementation of the Reader/Writer and the Parser/Generator services.
* [**Narson :: Integration Tests**](itests): This project builds a set of integration test bundles. They are not deployed.
  * [**Narson :: Integration Tests :: Narsese**](itests/org.narson.narsese.itests): This bundles tests functionalities related to the Narsese language object model,the Reader/Writer and the Parser/Generator services.
  * [**Narson :: Integration Tests :: OSGi Support**](itests/org.narson.osgisupport.library): This bundles provides a small library to help interact with the  	OSGi service registry and the service configurations during tests.
* [**Narson :: Features**](features): This project will build a set of features TODO.
* [**Narson :: Distro**](distro): This project will build a set of application runtime TODO.

## Developers

* **Clément Delgrange** (cdelg) / [delgrange@pm.me](mailto:delgrange@pm.me) @ [Narson](https://github.com/narson) - *developer*

## Licenses

**Apache License, Version 2.0**
  > The Apache License, Version 2.0
  >
  > For more information see [http://www.opensource.org/licenses/apache2.0.php](http://www.opensource.org/licenses/apache2.0.php).

---
Narson - [https://github.com/org.narson](https://github.com/org.narson)