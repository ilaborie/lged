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

#### Advanced Search

### Indexing

#### Clear index

#### Synchronize index

### Administration
#### Shelves

On <http://localhost:8080/org.ilaborie.pineneedles/rest/shelves>

##### Create/Update a Shelf

	 curl -XPUT -H "Content-Type: application/json" http://localhost:8080/org.ilaborie.pineneedles/rest/shelves --data-binary '{"name":"aze","description":"arf"}'
	 
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

	curl -XPUT http://localhost:8080/org.ilaborie.pineneedles/rest/folders/ -d '{ "name":"plop","description":"desc","path":"/Users/plop/Documents","recursive":true}'
	
##### Create/Update link source

	curl -XPUT http://localhost:8080/org.ilaborie.pineneedles/rest/links/ -d '{ "name":"plop","description":"desc","links":"…"}'
	
TODO / Ideas
------------
* Custom background (photo? gradient) 
* Better Error dialog

### Web Check

* Check on Firefox
* Check on Safari
* Fail when not OK (no html5, not valid browser, no js, ...)

### Search

* use field for Chrome search

* rest/search?q=
* rest/search?q= + fq (on source, on shelve)
* paging
* opening

### Index

* rest/index/clear
* rest/index/sync

### Administration

* Support Links Source

* Handle exclude Pattern for Folder Sources
* Handle Field info for links source textarea, folder
* Disable 'Update' btn if not changed

### About

* Shiny HTML5 Canvas animation (pine cone fall, + rotating)

### Possible Background

* Search with brown color (like wooden texture) (Texture dusty wood)

<https://secure.flickr.com/photos/24557420@N05/3975359536/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/nouspique/4633864261/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/tomtolkien/4670166552/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/wxmom/3541976351/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/martinlabar/3380449660/sizes/o/in/photostream/>
