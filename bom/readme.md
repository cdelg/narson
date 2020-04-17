# Narson :: BOM

This projects builds a set of BOM files useful to manage project
	 dependencies and to create OSGi repository indexes.

## Links

* [Documentation](https://github.com/org.narson/org.narson.reactor.bom)
* [Source Code](https://github.com/narson/org.narson.reactor.bom/) (clone with `scm:git:git@github.com/narson.git/org.narson.reactor.bom`)

## Built Artifacts

* [**Narson :: BOM :: Compile**](org.narson.bom.compile): This project defines a BOM file with the dependencies
	 used to compile the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
* [**Narson :: BOM :: Bundles**](org.narson.bom.bundles): This project defines a BOM file of the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
* [**Narson :: BOM :: Runtime**](org.narson.bom.runtime): This project defines a BOM file with the dependencies
	 used to run the Narson bundles. An OSGi repository index is created at
	  build time and can be used to provision the resolver.
* [**Narson :: BOM :: Test**](org.narson.bom.test): This project defines a BOM file with the dependencies
	 used to test the Narson bundles. An OSGi repository index is created at
	  build time and used during the integration tests.

## Developers

* **Clément Delgrange** (cdelg) / [delgrange@pm.me](mailto:delgrange@pm.me) @ [Narson](https://github.com/narson) - *developer*

## Licenses

**Apache License, Version 2.0**
  > The Apache License, Version 2.0
  >
  > For more information see [http://www.opensource.org/licenses/apache2.0.php](http://www.opensource.org/licenses/apache2.0.php).

---
Narson - [https://github.com/org.narson](https://github.com/org.narson)