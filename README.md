# Embedded Tomcat REST API - JSON Mapping Lab

## Project Overview

This project demonstrates building a lightweight RESTful web service using **Apache Tomcat Embedded** with **Jersey JAX-RS** and **Jackson** for JSON serialization. The application showcases advanced JSON mapping techniques for handling complex object relationships, similar to JPA entity mappings.

---

## Credits & Acknowledgments

**Original Project Framework:** Algonquin College - CST 8277  
**Course Instructor:** Mike Norman  
**Student Implementation:** JEFPERRY ACHU CHI  
**Semester:** [NOVEMBER 2025]

### Division of Work

- **Algonquin College/Instructor Provided:**

  - Project skeleton and Maven configuration (`pom.xml`)
  - Main application setup (`StartEmbeddedTomcat.java`)
  - REST endpoint implementations (all classes in `restendpoints` package)
  - Entity class structure (`PojoBase`, `EntityA`-`EntityF`)
  - Custom serializers (`SetOfEntityCeesSerializer`, `SetOfEntityDeesSerializer`)
  - JUnit test suite (`TestEntitiesSystem.java`)
  - Module configuration (`module-info.java`)

- **Student Implementation (My Work):**
  - Implemented Jackson annotations for polymorphic type discrimination using `@JsonTypeInfo` and `@JsonSubTypes` in `PojoBase.java`
  - Added `@JsonInclude(JsonInclude.Include.NON_NULL)` annotation to exclude null fields from JSON output
  - Configured `@JsonProperty("msg")` annotation to rename field in JSON serialization
  - Implemented `@JsonManagedReference` and `@JsonBackReference` annotations to handle bidirectional OneToMany/ManyToOne relationships
  - Applied `@JsonSerialize` annotations to utilize custom serializers for ManyToMany relationships
  - Successfully resolved all circular reference issues in complex object graphs

---

## Technologies Used

- **Java:** 17
- **Build Tool:** Maven 3.8.1
- **Server:** Apache Tomcat Embedded 9.0.56
- **JAX-RS Implementation:** Jersey 2.35
- **JSON Processing:** Jackson 2.13.1
- **Logging:** SLF4J 1.7.32 + Logback 1.2.9
- **Testing:** JUnit 5.7.0

---

## Project Structure

```
EmbeddedTomcat/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── module-info.java     # Java module configuration
│   │   │   └── com/algonquincollege/cst8277/lab/
│   │   │       ├── StartEmbeddedTomcat.java         # Main application entry point
│   │   │       ├── modelentities/                   # Entity/POJO classes
│   │   │       │   ├── PojoBase.java               # Abstract base with JSON annotations
│   │   │       │   ├── EntityA.java - EntityF.java # Concrete entities
│   │   │       │   ├── SimpleEntity.java           # Simple POJO example
│   │   │       │   └── *Serializer.java            # Custom JSON serializers
│   │   │       └── restendpoints/                   # JAX-RS REST resources
│   │   │           ├── HelloWorldRestResource.java
│   │   │           ├── GoodbyeWorldRestResource.java
│   │   │           ├── EchoRestResource.java
│   │   │           ├── ManagedRefRestResource.java
│   │   │           ├── C2DRestResource.java
│   │   │           └── D2CRestResource.java
│   │   ├── resources/
│   │   │   ├── logback.xml                          # Logback configuration
│   │   │   └── logback-access.xml
│   │   └── webapp/
│   │       └── index.html                           # Welcome page
│   └── test/
│       └── java/com/algonquincollege/cst8277/lab/
│           ├── AllTestCases.java                    # Test suite
│           ├── MyObjectMapperProvider.java          # Jackson config for tests
│           └── TestEntitiesSystem.java              # Integration tests
```

---

## Key Features Implemented

### 1. **Polymorphic Type Handling**

- Used `@JsonTypeInfo` and `@JsonSubTypes` to add discriminator field (`entity-type`) to JSON
- Enables Jackson to distinguish between `EntityA`, `EntityB`, `EntityC`, etc. in JSON payloads

### 2. **JSON Field Customization**

- `@JsonProperty("msg")` - Renames Java field `foobar` to `msg` in JSON output
- `@JsonInclude(JsonInclude.Include.NON_NULL)` - Excludes null fields from JSON
- `@JsonIgnore` - Prevents specific fields from being serialized

### 3. **Bidirectional Relationship Handling**

- `@JsonManagedReference` - Marks the "forward" part of relationship
- `@JsonBackReference` - Marks the "back" reference (omitted from JSON to prevent infinite loops)
- Prevents stack overflow errors in OneToMany/ManyToOne relationships

### 4. **ManyToMany Custom Serialization**

- Custom serializers create "hollow" copies of entities
- Breaks circular references by nulling reverse collections
- Maintains data integrity while avoiding infinite recursion

---

## REST API Endpoints

All endpoints are prefixed with `/api/v1/`

| Endpoint        | Method | Description                         | Response                       |
| --------------- | ------ | ----------------------------------- | ------------------------------ |
| `/helloworld`   | GET    | Returns EntityA instance            | JSON with typeA                |
| `/goodbyeworld` | GET    | Returns EntityB instance            | JSON with typeB                |
| `/echo`         | POST   | Echoes back posted message          | JSON with message + timestamp  |
| `/managed-ref`  | GET    | Demonstrates OneToMany relationship | JSON with nested entities      |
| `/c2d`          | GET    | Shows ManyToMany from C to D        | JSON with custom serialization |
| `/d2c`          | GET    | Shows ManyToMany from D to C        | JSON with custom serialization |

---

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Build the Project

```powershell
mvn clean package
```

### Run the Application

```powershell
# From IDE: Run StartEmbeddedTomcat.java main method
# Or from command line:
mvn exec:java -Dexec.mainClass="com.algonquincollege.cst8277.lab.StartEmbeddedTomcat"
```

The server starts on **port 9090**

### Test the Endpoints

```powershell
# Test HelloWorld
curl http://localhost:9090/api/v1/helloworld

# Test GoodbyeWorld
curl http://localhost:9090/api/v1/goodbyeworld

# Test Echo (POST)
curl -d 'Hello World!' -H "Content-Type: text/plain" -X POST http://localhost:9090/api/v1/echo

# Test Managed Reference
curl http://localhost:9090/api/v1/managed-ref

# Test ManyToMany
curl http://localhost:9090/api/v1/c2d
curl http://localhost:9090/api/v1/d2c
```

### Run Tests

```powershell
mvn test
```

---

## Learning Outcomes

This lab demonstrates:

1. **Alternative to Full Java EE:** Building REST services with minimal dependencies (30 components vs 500+ in traditional servers)
2. **Cloud-Ready Architecture:** Optimized for low memory footprint (128MB vs 512MB+)
3. **Jackson JSON Mapping:** Advanced annotations parallel to JPA entity mapping
4. **Complex Relationship Handling:** Solving circular reference issues in object graphs
5. **Modern Java Development:** Module system, JAX-RS, dependency injection patterns

---

## Technical Insights

### Why Embedded Tomcat?

- **Lightweight:** Significantly smaller dependency tree
- **Fast Startup:** No heavy application server overhead
- **Cloud-Friendly:** Lower memory requirements = reduced cloud hosting costs
- **Self-Contained:** Single JAR deployment, no external server needed

### Jackson vs JPA Parallels

| JPA Annotation                          | Jackson Equivalent                             | Purpose                     |
| --------------------------------------- | ---------------------------------------------- | --------------------------- |
| `@Inheritance` + `@DiscriminatorColumn` | `@JsonTypeInfo` + `@JsonSubTypes`              | Type discrimination         |
| `@OneToMany` + `@ManyToOne`             | `@JsonManagedReference` + `@JsonBackReference` | Bidirectional relationships |
| `@Transient`                            | `@JsonIgnore`                                  | Exclude from serialization  |
| `@Column(name="...")`                   | `@JsonProperty("...")`                         | Field renaming              |

---

## Challenges Solved

1. **Infinite Recursion:** Circular references in bidirectional relationships

   - **Solution:** Strategic use of `@JsonBackReference` and custom serializers

2. **ManyToMany Complexity:** Multiple circular paths in object graph

   - **Solution:** Custom serializers that create "hollow" entity copies

3. **Type Identification:** Distinguishing polymorphic types in JSON
   - **Solution:** `@JsonTypeInfo` adds discriminator field automatically

---

## Resources & References

- [Jackson Documentation](https://github.com/FasterXML/jackson-docs)
- [Jersey User Guide](https://eclipse-ee4j.github.io/jersey/)
- [Apache Tomcat Embedded](https://tomcat.apache.org/tomcat-9.0-doc/)
- Lab Instructions: "JSON Mapping with Embedded Tomcat" - Algonquin College CST 8277

---

## License

Academic project for educational purposes.  
© 2025 Algonquin College

---

## Contact

**Student:** Jefperry Achu  
