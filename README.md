## CS 474 - Object Oriented Languages and Environments
## Homework 1 - GitHub Search Application using GraphQL API

---

### Overview

The objective of this homework was to design a client program that consumes GitHub's GraphQL API leveraging object orientation techniques and design patterns taught in the course.

My implementation for this homework is a **GitHub Search Application** that allows users to search GitHub for users and repositories. The application displays 5 search results at a time *(configurable)*. The user is allowed to keep on viewing the next set of results until either all the search results have been viewed, or the user wants to quit viewing the results.

The application design leverages the use of **Builder**, **Factory**, **Adapter**, and **Dependency Injection** design patterns. The **Singleton** design pattern is also used multiple times by means of `object`s in Scala.

### Instructions

#### Prerequisites

- [SBT](https://www.scala-sbt.org/) installed on your system
- A [GitHub](https://github.com) Account
- A [personal access token](https://github.com/settings/tokens) associated with a GitHub account *(No special scopes needed)*

#### Running the application

1. Generate a *[personal access token](https://github.com/settings/tokens)* on GitHub, if not already generated. Refer to this [guide](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line) on how to create one. The app works with the *public access* scope, meaning, *no special scopes need to be added* to the token
2. Clone or download this repository onto your system
3. Open the Command Prompt (if using Windows) or the Terminal (if using Linux/Mac) and browse to the project directory
4. Build the project using SBT and run tests
    
    ```
    sbt clean compile test
    ```
    
5. Run the project while passing your **personal access token** as the first *command-line argument*

    ```
    sbt "run your_personal_access_token_goes_here"
    ``` 

6. Follow the prompts to use the application

### Application Design

![Class Diagram](https://mrasto3.people.uic.edu/cs474/hw1/class_diagram.png)

#### The `Query` and `QueryResult` classes

The `Query` class models the payload for making a GraphQL query. The JSON representation of this class forms the payload for sending GraphQL queries to a GraphQL API server. It takes 2 parameters:

1. **`query`**: The GraphQL query.
2. **`variables`**: A mapping of variable names to their values, for all variables used in the `query`.

 Apart from the above two parameters, the `Query` class also takes in a type parameter `T`, which denotes the `type` of the model class of the expected response from the server as a result of executing this `query`.
 
The `QueryResult` class models a response from a GraphQL API server with a parameter `data` at its root. It also takes a type parameter `T` which specifies the `type` of `data` which is received from the GraphQL server. Its type is same as the type used while creating the `Query`. 

#### The `GraphQLClient` and `GraphQLClient.Builder` classes

The `GraphQLClient` is an HTTP client that can be used to send queries to any server that supports the GraphQL API spec. It takes in 3 parameters:

1. **`apiEndpoint`**: The URL of the GraphQL server.
2. **`jsonConverter`**: An instance of `JsonConverter` trait that the client will use to use for (de)serializing JSON payloads.
3. **`headersPopulator`**: A lambda function that returns the headers to be added for a given `Query`. This can be used, for example, to add authentication headers to API calls.

The `executeQuery` method of this class is a generic method that takes an instance of `Query` (with expected `type` of the response), builds an Http request, serializes the `query` to JSON, posts the data to the server, and lastly, deserializes the response to a `QueryResult` with the same type `T` as specified in the `Query`.

The `GraphQLClient` class comes with a *companion object*, which exposes a `GraphQLClient.Builder` instance using the `newBuilder` method. This follows the **Builder** design pattern to let users easily configure and create a new instance of `GraphQLClient`. 

#### The `JsonConverter` and `JsonConverterFactory` traits

The `JsonConverter` trait defines an interface used by the `GraphQLClient` to convert Scala objects to JSON and vice-versa. This allows users to use their favorite JSON (de)serializing library with `GraphQLClient` for performing the JSON conversion.

Moreover, a `JsonConverterFactory` trait is also defined for writing factories for instantiating and configuring a `JsonConverter` using the **Abstract Factory** design pattern.

Three implementations are provided for each of these traits which wrap 3 popular JSON (de)serialization libraries - **Gson**, **Lift-Web JSON**, and **Jackson**. These wrappers ***adapt*** these popular libraries to the `JsonConverter` trait using the **Adapter** design pattern. The configuration of these libraries is taken care of by the corresponding ***factories***.

The use of `JsonConverter` makes the `GraphQLClient` agnostic of the JSON library being used. The dependency for the desired JSON library can be ***injected*** into the `GraphQLClient` using the **dependency injection** design pattern. This is demonstrated more clearly in `JsonConverterTest`, where the `JsonConverterBehavior` defines the test cases for any implementation of `JsonConverter`, and `JsonConverterTest` uses dependency injection to run those tests for all the three implementations of `JsonConverter`. 

The tests for *Gson* and *Lift-Web JSON* were failing due to the problems highlighted below, and have hence been commented.

> **Jackson Scala Module** was chosen the default JSON library for this project since there were issues with the other two libraries - Gson and Lift-Web JSON.
>
> **Gson** had two problems. First, it doesn't work with Scala Lists and Maps. Second, it was not able to handle deserialization of nested generic objects, possibly due to type erasure.
>
> **Lift-Web JSON** did not provide a way to omit serialization of fields with null values in the resulting JSON. Moreover, and more importantly, it was not able to handle fields in the model objects, during deserialization, that were missing in the input JSON. This was a deal-breaker because the model class could have extra fields which might not be needed to be fetched for a particular query.

#### The GitHub GraphQL API *model* classes

The `com.mayankrastogi.cs474.hw1.github` package contains the model classes for modelling responses from the GitHub GraphQL API. It further divided into the `connections`, `enums`, `interfaces`, and `objects` sub-packages that partially mimic the GitHub GraphQL API schema.

Only models used in the GraphQL queries have been created. The list of fields defined in these models is also not exhaustive and is restricted to the ones that were needed by this application.

#### The `GitHubGraphQLQueries` object

This **Singleton** object provides the definitions of all queries supported by this application, along with the types of results returned by them.

There are two queries defined. Both of these queries take variables for the *search term*, *search type*, *number of results*, and *cursor to next page*.

**For Searching Repositories** *- Returns `Query[RepositorySearchResult]`* 

```graphql

query search($query: String!, $type: SearchType!, $numOfResults: Int!, $nextPageCursor: String) {
  search(type: $type, query: $query, first: $numOfResults, after: $nextPageCursor) {
    pageInfo {
      hasNextPage
      endCursor
    }
    nodes {
      ... on Repository {
        name
        nameWithOwner
        description
        watchers {
          totalCount
        }
        stargazers {
          totalCount
        }
        languages(first: 100) {
          nodes {
            name
          }
        }
        repositoryTopics(first: 100) {
          nodes {
            topic {
              name
            }
          }
        }
        sshUrl
        url
      }
    }
  }
}

```

**For Searching Users** *- Returns `Query[UserSearchResult]`*

```graphql

query search($query: String!, $type: SearchType!, $numOfResults: Int!, $nextPageCursor: String) {
  search(type: $type, query: $query, first: $numOfResults, after: $nextPageCursor) {
    pageInfo {
      hasNextPage
      endCursor
    }
    nodes {
      ... on User {
        name
        login
        location
        bio
        url
        followers {
          totalCount
        }
        following {
          totalCount
        }
        repositories(first: 100) {
          nodes {
            languages(first: 100) {
              nodes {
                name
              }
            }
            repositoryTopics(first: 100) {
              nodes {
                topic {
                  name
                }
              }
            }
          }
          totalCount
        }
        repositoriesContributedTo(first: 100) {
          nodes {
            languages(first: 100) {
              nodes {
                name
              }
            }
            repositoryTopics(first: 100) {
              nodes {
                topic {
                  name
                }
              }
            }
          }
          totalCount
        }
      }
    }
  }
}

```

#### The `GitHubSearchApp` and the `GitHubSearchAppUtils` objects

The `GitHubSearchApp` is the **main** class of the program and provides a menu-based command-line user interface for searching GitHub for users and repositories.

It accepts the *GitHub personal access token* as the first command-line argument, loads the configuration from the Typesafe config files using the `Settings` utility, creates a `GraphQLClient` for GitHub GraphQL API with the help of `GitHubSearchAppUtils` object, asks the user what to search, executes the query, and prints the results while allowing the user to page through the results.

The `createGitHubGraphQLClient` method defined in the `GitHubSearchAppUtils` object creates a new `GraphQLClient` using the **Builder** design pattern, and configures it for querying GitHub's GraphQL API. It is also responsible for defining that the authorization header, with the provided personal access token, should be added to all the HTTP requests sent to the GitHub GraphQL API server. The `GitHubSearchAppUtilsTest` provides a test case for verifying that the `GraphQLClient` created by this method is configured properly. 

### Output and Performance ###

#### Search for user "Mayank Rastogi"

```text

===============================================================================================================
                                         GitHub Search Application
===============================================================================================================
This application allows you to search for users or repositories on GitHub.

First choose what you want to search (Users or Repositories) and then specify the search term. You will be able
to page through the results by entering 'N' to go to the next page. The number of results to show in a page can
be configured using the "github-graphql-client.number-of-results-in-page" setting in the config file.

---------------------------------------------------------------------------------------------------------------


What do you want to search?

1. Users
2. Repositories
0. Exit

Please enter your choice:
1
Please enter your search term:
Mayank Rastogi


Name: Mayank K Rastogi
Username: mayankrastogi
Bio: Programmer | Full-stack developer | Gamer
Followers: 9
Following: 6
Repositories Owned: 17
Repositories Collaborated On: 1
Languages Used: JavaScript, Shell, CSS, Scala, C#, Python, ShaderLab, Jupyter Notebook, C, HTML, Smalltalk, GLSL, Mask, Java, Dockerfile
Topics Interested In: cloudsim-plus, grpc-java, personal-website, hr-management-system, typesafe-config, jekyll, scalatest, grpc, graphviz, aws-emr, logback, chess, unity3d, arcade-game, room-scale-vr, sbt-multi-project, scikit-learn, mapreduce, junit, hadoop-mapreduce, capstan, blender, rest-api, osv, inlineedit, osv-openjdk8, java-rmi, protobuf, cloudsim, aws-lambda, java, html-table, vrtk, virtual-reality, augmented-reality, jupyter-notebook, docker, sbt, hadoop, spark, xml, ibm-db2, inline-editing, halloween, scala, pandas, spring-boot, libgdx, unikernel, platform-game, python, jquery, classification, flash, numpy, aws-api-gateway, vuforia, ibm-websphere-portal, cloud-computing, machine-learning, java-ee, game, jquery-plugin, virtual-appliance, markdown
Profile URL: https://github.com/mayankrastogi


Name: Mayank Rastogi
Username: mrast2
Bio: null
Followers: 0
Following: 1
Repositories Owned: 3
Repositories Collaborated On: 0
Languages Used: JavaScript, CSS, C#, XSLT, HTML, ASP
Topics Interested In: 
Profile URL: https://github.com/mrast2


Name: Mayank Rastogi
Username: rastogi-mayank
Bio: 
Followers: 0
Following: 0
Repositories Owned: 1
Repositories Collaborated On: 0
Languages Used: PLSQL, JavaScript, Shell, CSS, Groovy, Batchfile, PLpgSQL, SQLPL, HTML, Java, Dockerfile
Topics Interested In: 
Profile URL: https://github.com/rastogi-mayank


Name: Mayank Rastogi
Username: mayankrastogi99
Bio: 
Followers: 0
Following: 0
Repositories Owned: 1
Repositories Collaborated On: 1
Languages Used: 
Topics Interested In: 
Profile URL: https://github.com/mayankrastogi99


Process finished with exit code 0

```

![User Search Performance](https://mrasto3.people.uic.edu/cs474/hw1/01.png)

#### Search for repository "Cloud Simulation" *(Showing 2 pages of results)*

```text

===============================================================================================================
                                         GitHub Search Application
===============================================================================================================
This application allows you to search for users or repositories on GitHub.

First choose what you want to search (Users or Repositories) and then specify the search term. You will be able
to page through the results by entering 'N' to go to the next page. The number of results to show in a page can
be configured using the "github-graphql-client.number-of-results-in-page" setting in the config file.

---------------------------------------------------------------------------------------------------------------


What do you want to search?

1. Users
2. Repositories
0. Exit

Please enter your choice:
2
Please enter your search term:
Cloud Simulation

Name: cloudsim
Owner: Cloudslab
Description: CloudSim: A Framework For Modeling And Simulation Of Cloud Computing Infrastructures And Services
Watchers: 50
Stargazers: 305
Languages: Java
Topics: 
GitHub URL: https://github.com/Cloudslab/cloudsim
Clone URL: git@github.com:Cloudslab/cloudsim.git


Name: CloudReports
Owner: thiagotts
Description: An extensible simulation tool for energy-aware cloud computing environments
Watchers: 8
Stargazers: 31
Languages: Shell, Java, JavaScript, CSS, HTML
Topics: 
GitHub URL: https://github.com/thiagotts/CloudReports
Clone URL: git@github.com:thiagotts/CloudReports.git


Name: cloudsim-plus
Owner: manoelcampos
Description: A modern, full-featured, highly extensible and easier-to-use Java 8+ Framework for Cloud Computing Simulation
Watchers: 17
Stargazers: 107
Languages: Shell, Java
Topics: simulation-framework, cloud-computing, cloud-simulation, research, cloud-infrastructure, simulation, java, java8, test-bed, iaas, paas, saas, cloudsim, trace, google-cluster-data, workload, cloudsimplus, auto-scaling, load-balancing
GitHub URL: https://github.com/manoelcampos/cloudsim-plus
Clone URL: git@github.com:manoelcampos/cloudsim-plus.git


Name: CloudSimPy
Owner: RobertLexis
Description: CloudSimPy: Datacenter job scheduling simulation framework
Watchers: 0
Stargazers: 41
Languages: Python
Topics: cloud, reinforcement-learning, job-scheduling-algorithm, datacenter, schedule
GitHub URL: https://github.com/RobertLexis/CloudSimPy
Clone URL: git@github.com:RobertLexis/CloudSimPy.git


Name: pycles
Owner: pressel
Description: A python based infrastructure for cloud large eddy simulation. 
Watchers: 15
Stargazers: 58
Languages: C, Python, Shell, Fortran, C++
Topics: 
GitHub URL: https://github.com/pressel/pycles
Clone URL: git@github.com:pressel/pycles.git


Enter anything to show next page of results. Enter 'Q' to quit:
N

Name: firesim
Owner: firesim
Description: FireSim: Easy-to-use, Scalable, FPGA-accelerated Cycle-accurate Hardware Simulation in the Cloud
Watchers: 46
Stargazers: 253
Languages: Shell, Python, Makefile, Assembly, Scala, C++, Verilog, C, Batchfile
Topics: fpga, risc-v, simulation, datacenter, hardware, firesim, rocket-chip, boom, cloud
GitHub URL: https://github.com/firesim/firesim
Clone URL: git@github.com:firesim/firesim.git


Name: cloudsimsdn
Owner: Cloudslab
Description: CloudSimSDN is an SDN extension of CloudSim project to simulate SDN features in the context of a cloud data center.
Watchers: 9
Stargazers: 35
Languages: Java
Topics: cloudsim, sdn, simulation, cloud, data-center, vms, workload, vm-creation
GitHub URL: https://github.com/Cloudslab/cloudsimsdn
Clone URL: git@github.com:Cloudslab/cloudsimsdn.git


Name: HPCCloud
Owner: Kitware
Description: A Cloud/Web-Based Simulation  Environment
Watchers: 10
Stargazers: 35
Languages: JavaScript, HTML, Python, Shell, CMake
Topics: simulation-environment, cloud, hpc
GitHub URL: https://github.com/Kitware/HPCCloud
Clone URL: git@github.com:Kitware/HPCCloud.git


Name: simgrid
Owner: simgrid
Description: Framework for the simulation of distributed applications (Clouds, HPC, Grids, IoT and others)
Watchers: 15
Stargazers: 84
Languages: TeX, CMake, Perl, Shell, C, C++, R, Python, Java, Objective-C, Fortran, Lex, Yacc, TLA, Roff, XSLT, sed, Makefile
Topics: 
GitHub URL: https://github.com/simgrid/simgrid
Clone URL: git@github.com:simgrid/simgrid.git


Name: CSF
Owner: jianboqi
Description: LiDAR point cloud ground filtering / segmentation (bare earth extraction) method based on cloth simulation
Watchers: 11
Stargazers: 62
Languages: C++, MATLAB, Python, Makefile, CMake
Topics: 
GitHub URL: https://github.com/jianboqi/CSF
Clone URL: git@github.com:jianboqi/CSF.git


Enter anything to show next page of results. Enter 'Q' to quit:
Q

Process finished with exit code 0

```

![Repository Search Performance](https://mrasto3.people.uic.edu/cs474/hw1/02.png)