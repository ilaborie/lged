PineNeeldes
===========

Goals
-----
The main goal of this project is to provided an easy way to retrieve some documents with a single search criteria.

Documents could be a binary file (pdf, ppt, odt, …) or a Web page (blog, wikipedia, …)

I want to quickly configure what can be indexed (a folder, a file, a web site, a web page, …)

Status
------
In development, not usable at the time.


REST API
--------
### Search

#### Basic Search

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/search?q=<query>

#### Advanced Search

### Indexing

#### Clear index

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/index/clear

#### Synchronize index

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/index/synchronize

### Administration
#### Shelves

On <http://localhost:8080/org.ilaborie.pineneedles/rest/shelves>

##### Create/Update a Shelf

	 curl -XPUT -H "Content-Type: application/json" h--data-binary '{"name":"aze","description":"arf"}'
	 
Should return the created/updated shelf

##### Get Shelf

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/shelves/<id>

##### Delete Shelf

	curl -XDELETE http://localhost:8080/org.ilaborie.pineneedles/rest/shelves/<id>


##### Get all Shelves

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/shelves
	
#### Sources

Read on <http://localhost:8080/org.ilaborie.pineneedles/rest/sources>

Update folder on <http://localhost:8080/org.ilaborie.pineneedles/rest/folders>

Update links on <http://localhost:8080/org.ilaborie.pineneedles/rest/links>

##### Get Source

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/sources/<id>

##### Get Sources by Shelf

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/sources/self/<id>
	
##### Delete Source

	curl -XDELETE http://localhost:8080/org.ilaborie.pineneedles/rest/sources/<id>
	
##### Create/Update folder source

	curl -XPUT -H "Content-Type: application/json" http://localhost:8080/org.ilaborie.pineneedles/rest/folders/ -d '{ "name":"plop","description":"desc","path":"/Users/plop/Documents","recursive":true}'
	
##### Create/Update link source

	curl -XPUT -H "Content-Type: application/json" http://localhost:8080/org.ilaborie.pineneedles/rest/links/ -d '{ "name":"plop","description":"desc","links":"…"}'
	
TODO / Ideas
------------

### Web Check
* Check on Firefox
* Check on Safari
* Fail when not OK (no html5, not valid browser, no js, ...)

### Search

* use field for Chrome search
#### Basic Search
* paging UI
* empty
* Highlight
* Faceting
  * Tag
  * Shelf
  * Source
  
* Alternate paging, <http://www.codinghorror.com/blog/2012/03/the-end-of-pagination.html>

#### Advanced search
* Faceting
  * date
  * type
* Shelf filtering
* Source filtering
* Tag filtering
* Type filtering


### Index

#### Clear
* delete all elements

#### Scheduling

#### Indexation
* Split Keywords for update source elements
* Bulk request ?
* Node name
* Configuration mapping (analyser)

### Administration
#### UI
* Extract favicon from links
* Handle Field info for links source textarea, folder, tags
* Disable 'Update' btn if not changed

#### Tags
* Add Tag support for source folder
* Add managing Tag page
* Thesaurus
* Synonymes

#### Sources
* Handle exclude Pattern for Folder Sources
* Handle Element status (Active/Inactive)
* Provide Twitter Source
* Provide RSS/ATOM Source

#### Monitoring
* Index status
* Add Monitor Indexing page
* handle messages (Lvl, message, Error, Timestamp)


### About
* Shiny HTML5 animation (pine cone fall, + rotating)
