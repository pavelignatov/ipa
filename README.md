##Short description
######The project is an example which contains spring and react/redux technologies as well might be useful as boilerplate template.
##What is there
* Spring boot
* Gradle
* Multiple modules
* Hibernate
* JPA with auditing
* Querydsl
* Hazelcast cashe/session managment
* JSR-107 cache standart support
* Several datasources
* Jdbc supports
* Swagger
* Authentication
* Groovy for testing
* Thymeleaf
* Logback
* React JS / Redux
* Hot reloading
* Useful dev tools, for example: reactotron for mac os
 
##How to run it
####Intellij Idea
* Download lombok plugin
* Enable annotation processing for database in db profile module and choice "Module content root" for db profile for other profiles choice only "Enable annotation processor" option. 
* __For javascript:__ Go to Settings > Languages & Frameworks > JavaScript and choice "React JSX" here
* In run/debug configurations choice "Spring boot" configuration then start it
*  Frontend module can be run `./npmw start`

####Gradle
`./gradlew :web:assemble && java -jar web/build/libs/ipa-web-0.1.0.jar `