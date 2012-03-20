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
	

Ideas
-----

### Web Check

* Check on Firefox
* Check on Safari
* Fail when not OK (no html5, not valid browser, no js, ...)

### Search

* use field for Chrome search

### Administration

* Animation when display shelves
* Cool Dialog for Delete confirmation
* Control Rest (check fields validity)
* Handle error with dialog when REST client fail
* Notify Update shelf OK
* Add delete btw on shelf detail
* Trim String
* Handle invalid field UI ?
* Use Mustache.js and ICanHaz.js to render Shelf, Source, …
* Allow Markdown as description formating

### About

* Shiny HTML5 Canvas animation


### Images

<https://secure.flickr.com/photos/24557420@N05/3975359536/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/nouspique/4633864261/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/tomtolkien/4670166552/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/wxmom/3541976351/sizes/o/in/photostream/>
<https://secure.flickr.com/photos/martinlabar/3380449660/sizes/o/in/photostream/>
