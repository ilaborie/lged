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

### Shelves

On <http://localhost:8080/org.ilaborie.pineneedles/rest/shelves>

#### Create/Update a Shelf

	 curl -XPUT -H "Content-Type: application/json" http://localhost:8080/org.ilaborie.pineneedles/rest/shelves --data-binary '{"name":"aze","description":"arf"}'
	 
Should return the created/updated shelf

#### Get Shelf

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/shelves/037599ab-9557-4cff-a85d-aea9f6526550

#### Delete Shelf

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/shelves/037599ab-9557-4cff-a85d-aea9f6526550


#### Get all Shelves

	curl -XGET http://localhost:8080/org.ilaborie.pineneedles/rest/shelves
	
### Sources

Read on <http://localhost:8080/org.ilaborie.pineneedles/rest/sources>
Update folder on <http://localhost:8080/org.ilaborie.pineneedles/rest/folders>
Update links on <http://localhost:8080/org.ilaborie.pineneedles/rest/links>

### Get Source
### Get all Sources
### Delete Source
### Create/Update folder source
### Create/Update link source

Ideas
-----

### Web Check

* Check on Firefox
* Check on Safari
* Fail when not OK (no html5, not valid browser, no js, ...)

### Search

* use field for Chrome search

### Administration

* Add Breadcrumb: **Shelves > Shelf > Sources > Source**
* Add a .label for source type: folder~warn  link~info
* Change source dialog title func(kind)
* Focus on Source Dialog
* Focus on Detail Shelf
* Focus on Detail Source
* Enter on Source dialog field

* Handle exclude Pattern for Folder Sources
* Handle Field info for links source textarea
* Disable 'Update' btw if not changed
* Handle invalid field UI ?
* Allow Markdown as description formatting

### About

* Shiny HTML5 Canvas animation (pine cone fall, + rotating)

### Possible Background

<https://secure.flickr.com/photos/24557420@N05/3975359536/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/nouspique/4633864261/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/tomtolkien/4670166552/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/wxmom/3541976351/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/martinlabar/3380449660/sizes/o/in/photostream/>
