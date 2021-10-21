# Length Conversion Service

A simple length conversion service implementation using Kotlin and Spring Boot.   
The service loads a conversion table for length units from a CSV file 
and provides a REST API for length conversion operations.
In the current version, database persistence is not supported. 


## Functionality


- Load length units from [CSV file](src/main/resources/length-units.csv)
- Convert between the base length unit `meter` to any other supported length unit (e.g., `mm` or `miles`)
- Convert between supported length units (e.g. `km` to `miles`)


## Usage

The Spring Boot service can be started from an IDE by running the
[LengthConversionServiceApplication](src/main/kotlin/me/david/lengthconversionservice/LengthConversionServiceApplication.kt)
class or from the console by executing `./gradlew bootRun`.


## Swagger UI

The REST API can be conveniently viewed and used through a [Swagger UI](https://swagger.io/tools/swagger-ui/).
A Swagger UI for this service, when run on `localhost`, can be found [here](http://localhost:8080/api/length-conversion-service/v1/swagger-ui.html).


## Copyright notice

Copyright (c) 2021 David Trachtenherz

Published under the [MIT License](https://opensource.org/licenses/MIT). 


