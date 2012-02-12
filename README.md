# Local GED

## Goals
The main goal of this project is to provided an easy way to retrive some documents with a single search criteria.

Documents could be a binary file (pdf, ppt, odt, …) or a Web page (blog, wikipedia, …)

I want to quickly configure what can be indexed (a folder, a file, a web site, a web page, …)

## Status
In developpement, not usable at the time.

## Technical Views

### Server Side
The server side consist in a JavaEE application with an embedded SolR server.
The server should expose a REST interface to query the indexed part

### Client Side
Not clearly determine at the time:

* JavaFX client
* Eclipse RCP client
* Web application (Javascript + REST)
* Native Desktop client
* Native smartphone client

## Future?

In the Could ?
Replace SolR with ElasticSearch

