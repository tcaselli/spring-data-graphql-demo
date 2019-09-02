# Java data GraphQL

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

## Documentation

This [spring-data-graphql-demo](https://github.com/tcaselli/sring-data-graphql-demo) project is intended to provide an easy to understand usage showcase of [java-data-graphql](https://github.com/tcaselli/java-data-graphql).

The [java-data-graphql](https://github.com/tcaselli/java-data-graphql) library is a layer above the great [graphql-java](https://github.com/graphql-java/graphql-java) library. It generates a graphql schema out of a given configuration which grants the possibility to :
* execute CRUD operations on entities (getById, getAll, save, delete).
* deal with persisted entities and embedded data objects.
* handle dynamic attributes on any entity.
* add custom methods (queries or mutations) to enrich your schema.
* deal with paging, sorting, filtering on queries.
* easily create "data fetchers" for linking this library to the persistence layer of your choice.

### How to launch the demo

First, checkout the code from github.

-> From command line 

```sh
mvn install
# Replace XXX with current version (example : XXX = 1.1-SNAPSHOT)
java -jar target/myapplication-XXX.jar
```

-> From your IDE

Run ```com.daikit.graphql.spring.demo.ApplicationStarter``` as a Java application

### How to browse the demo

You are now able to browse your schema with graphiql web user interface available at http://localhost:8080/graphiql/index.html  

![Graphiql web UI](/docs/doc-graphiql-01.png)

You can run queries and mutations with variables. See next section for samples.

![Graphiql web UI query](/docs/doc-graphiql-02.png)

### Sample queries and mutations

You can run these sample queries and mutations in graphiql. Variables are to be set in lower left area of the user interface.  

#### Get entity by ID 

```graphql
# query
query getEntity1($id: ID!) {
  getEntity1(id: $id) {
    id
    intAttr
    longAttr
    stringAttr
    embeddedData1 {
      stringAttr
    }
  }
}
# variables
{
    "id":"3"
}
```

#### Get list of entities

```graphql
# query retrieving all entities
query getAllEntity5 {
  getAllEntity5 {
    data {
        id
        intAttr
        stringAttr
    }
  }
}

# query with paging
query getAllEntity5 {
  getAllEntity5(
    paging: { limit: 2, offset: 1 }
  ) {
    data {
        id
        intAttr
        stringAttr
    }
  }
}

# query with ordering
query getAllEntity5 {
  getAllEntity5(
    orderBy: [
        { field: "intAttr", direction: DESC },
        { field: "stringAttr", direction: DESC }
    ]
  ) {
    data {
        id
        intAttr
        stringAttr
    }
  }
}

# query with filtering
# /!\ Filtering only works on int and String types.. 
# filtering on other types has not been implemented in this demo
query getAllEntity5 {
  getAllEntity5(
    filter: { 
        intAttr: { operator: le, value: 5 },
        stringAttr: { operator: endsWith, value: "-4" }
    }
  ) {
    data {
        id
        intAttr
        stringAttr
    }
  }
}
```

#### Save entity

```graphql
# mutation
mutation saveEntity1($id: ID, $intAttr: Int, $embeddedData1: EmbeddedData1InputType) {
  saveEntity1(data: { id: $id, intAttr: $intAttr, embeddedData1: $embeddedData1}) {
    id
    intAttr
    embeddedData1 {
      stringAttr
    }
  }
}
# variables
{
    "id": "3",
    "intAttr": 150,
    "embeddedData1": {
        "stringAttr": "test data1"
    }
}
```

#### Delete entity

```graphql
# mutation
mutation deleteEntity1($id: ID!) {
  deleteEntity1(id: $id) {
    id
    typename
  }
}
# variables
{
    "id": "2"
}
```

#### Custom method mutation

```graphql
# mutation
mutation customMethodMutation1($arg1: String) {
    customMethodMutation1(arg1: $arg1) {
        stringAttr
    }
}
# variables
{
    "arg1": "This value should be returned"
}
```

#### Custom method query

```graphql
# query
query customMethodQuery1($arg1: String) {
    customMethodQuery1(arg1: $arg1) {
        intAttr
        stringAttr
        embeddedData1 {
            stringAttr
        }
    }
}
# variables
{
    "arg1": "This string is set in both returned stringAttr"
}
```

```graphql
# query
query customMethodQuery2(
    $arg1: String, 
    $arg2: EmbeddedData1InputType
) {
    customMethodQuery2(arg1: $arg1, arg2: $arg2) {
        intAttr
        stringAttr
        embeddedData1 {
            intAttr
            stringAttr
        }
    }
}
# variables
{
    "arg1": "This string is set in returned stringAttr",
    "arg2": {
        "intAttr": 2,
        "stringAttr": "This string is set in returned embeddedData1"
    }
}
```

```graphql
# query
query customMethodQuery3(
    $arg1: Enum1, 
    $arg2: [String], 
    $arg3: [Enum1], 
    $arg4: [EmbeddedData1InputType], 
    $arg5: String
) {
    customMethodQuery3(
        arg1: $arg1, 
        arg2: $arg2, 
        arg3: $arg3, 
        arg4: $arg4, 
        arg5: $arg5
    ) {
    enumAttr
    stringList
    enumList
    embeddedData1s {
        stringAttr
    }
    stringAttr
  }
}
# variables
{
    "arg1": "VAL2",
    "arg2": ["string1", "string2"],
    "arg3": ["VAL1", "VAL2"],
    "arg4": [{"stringAttr": "data1"}, {"stringAttr": "data2"}],
    "arg5": null
}
# TIP : there is a check on arg5:
# -> if null the set "NULLVALUE" otherwise set as null
```

#### Dynamic attribute getter

```graphql
# query
query getEntity1($id: ID!) {
  getEntity1(id: $id) {
    id
    dynamicAttribute1
  }
}
# variables
{
    "id": "3"
}
```

#### Dynamic attribute setter

```graphql
# mutation
mutation saveEntity1($id: ID, $dynamicAttribute2: String) {
  saveEntity1(data: { id: $id, dynamicAttribute2: $dynamicAttribute2 }) {
    id
    stringAttr
  }
}
# variables
{
    "id": "3",
    "dynamicAttribute2": "This string is set in stringAttr"
}
```

## Contributing

We accept Pull Requests via GitHub. There are some guidelines which will make applying PRs easier for us:
+ No spaces :) Please use tabs for indentation.
+ Respect the code style.
+ Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source code should be reformatted create a separate PR for this change.

## License

This code is under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0).
