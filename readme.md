# Project Convention

## Package Convention
```plain
├── HELP.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── readme.md
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── kdt_y_be_toy_project2
    │   │           ├── KdtYBeToyProject2Application.java
    │   │           ├── domain
    │   │           │   ├── itinerary
    │   │           │   │   ├── controller
    │   │           │   │   ├── domain
    │   │           │   │   ├── dto
    │   │           │   │   ├── exception
    │   │           │   │   ├── repository
    │   │           │   │   └── service
    │   │           │   ├── model
    │   │           │   │   ├── PlaceInfo.java
    │   │           │   │   └── TimeScheduleInfo.java
    │   │           │   └── trip
    │   │           │       ├── controller
    │   │           │       ├── domain
    │   │           │       ├── dto
    │   │           │       ├── exception
    │   │           │       ├── repository
    │   │           │       └── service
    │   │           └── global
    │   │               ├── config
    │   │               └── error
    │   └── resources
    │       ├── application.properties
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── kdt_y_be_toy_project2
                    └── KdtYBeToyProject2ApplicationTests.java

```
