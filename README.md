![Coverage Status](https://coveralls.io/repos/github/oltyant/immerger/badge.svg?branch=master) ![Travis CI master status](https://travis-ci.org/oltyant/immerger.svg?branch=master)

# Immerger (development in early phase)

The name "Immerger" has two origins:
* Religious (christian) - From the baptism latin word (which means submerge, immerse, immerge (french word))
* In many culture the ablution (wash yourself with water) means both spiritual and material clean up
As a result the traits of the submerged, washed thing can be seen more clearly.
This is the idea of this project. Through solving and making statistics about different problems - clean the programming languages up and see their true natures. The immerger as a web application provide a way to clean up programming languages.

# Why we need to clean up a language?

It is true that you can read information about programming languages, yet you might think about it whether the writer has unbiased opinion about it. Also you can think about whether the writer has enough experience with other programming languages in order to believe in his appraisal or not. If you never think about these things, probably you haven't tried out different programming paradigms, different programming languages. Just like a religious argument, some software engineer believe in Java, some software engineer hate it. Who has right? Can you decide whether Haskell is better for solving mathematical problems than Java? How can you decide if you do not have experience with it? You have to experiement, you have to "submerge" the languages and clean them from the dirt that the overbias, popularity put on the languages. This will be an application which will provide the possiblity to solve different problems with different languages, most preferable in an idiomatic way (as we would like to use them in a natural way not "rape" and hack them, right?). As a result you will get a more and more statistics about the data that can be measured in an objective way.

# Prerequisites

* Linux Operating system
* MongoDB 3+ installed on it
* Leiningen for running the web application 

After you clone this repo, go into its root directory and execute the following command:
```
lein run
```

You should see somthing similar:
```
2016-10-11 18:45:48.346:INFO::main: Logging initialized @2375ms
2016-10-11 18:45:50.204:INFO:oejs.Server:main: jetty-9.2.10.v20150310
2016-10-11 18:45:50.332:INFO:oejs.ServerConnector:main: Started ServerConnector@57edfa89{HTTP/1.1}{0.0.0.0:8080}
2016-10-11 18:45:50.333:INFO:oejs.Server:main: Started @4363ms
```
Visit http://localhost:8080

Currently only the first features ready which means that the application only can read/write the local MongoDB.

# Planned features

* Create new problems with enclosed description, test inputs and expected outputs
* Upload source files for executing them on a problem
* Get source code and run statistics
* Create new measures which can be bound to all problems/new problems only
* Check statistics, comparision charst/diagrams
