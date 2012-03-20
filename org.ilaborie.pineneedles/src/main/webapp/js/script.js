/* Author: Igor Laborie <ilaborie@gmail.com>
	TODO @see http://blog.ippon.fr/2012/02/20/mustache-js-et-icanhaz-js-au-secours-de-jquery/
 */
var LocalGed = {
	currentPage : 'search', 				// Between 'search', 'admin', ‘about‘
	shelf : null,							// Current shelf
	source : null,							// Current source
	debug : true							// Debug mode
};

// Do when page is loading
$(document).ready(function() {
	// Add a startsWith function on String
	if (typeof String.prototype.startsWith != 'function') {
			String.prototype.startsWith = function (str){
				return this.indexOf(str) == 0;
			};
		}
	
	// Define Menu Action
	$("#search").click(LocalGed.showSearch);
	$("#admin").click(LocalGed.showAdmin);
	$("#about").click(LocalGed.showAbout);
	
	// Search
	$("#btnSearch").click(LocalGed.doSearch);
	$("#q").keypress(LocalGed.handleSearch);
	
	// Shelves Administration
	$("#btnAddShelf").click(LocalGed.addShelf);
	$("#btnBackShelves").click(LocalGed.backShelves);
	$("#btnSaveShelf").click(LocalGed.saveShelf);
	$("#shelf-create-name").keypress(LocalGed.createShelf);
	$("#btnCreateShelf").click(LocalGed.createShelf);
	
	// Sources Administration
	$("#btnBackShelf").click(LocalGed.backShelf);
	$("#btnAddSource").click(LocalGed.addSource);
	$(".showSource").click(LocalGed.showSource);
	$(".delSource").click(LocalGed.deleteSource);
	$("#btnCreateShelf").click(LocalGed.createSource);
	$("#btnSaveShelf").click(LocalGed.saveSource);
	
	// Initial State
	LocalGed.showSearch();
	$("#q").focus();
});

//Handle Menu
/**
* Switch to a page
* @param page the page
*/
LocalGed.switchPage = function(page) {
	// Switch Menu
	$("#" + this.currentPage).parent().removeClass("active"); 
	this.currentPage = page;
	$("#" + this.currentPage).parent().addClass("active");
	
	// Switch content
	$(".content").each(function () {
		var elt = $(this);
		var id = elt.attr('id');
		if (id  && id.startsWith(page)) {
			elt.removeClass("hidden");
		} else {
			elt.addClass("hidden");
		}
	});
};
/**
* Show Search Page
*/
LocalGed.showSearch = function () {
	LocalGed.switchPage('search');
	$("#q").focus();
};
/**
* Show Admin Page
*/
LocalGed.showAdmin = function () {
	LocalGed.switchPage('admin');
	LocalGed.loadShelves();
};
/**
* Show About Page
*/
LocalGed.showAbout = function () {
	LocalGed.switchPage('about');
};

// Search
/**
 * Handle KeyPres on Search input
 */
LocalGed.handleSearch = function(e) {
	LocalGed.doOnEnter(e,LocalGed.doSearch);
};
/**
 * Launch search
 */
LocalGed.doSearch = function() {
	var query = $("#q").attr("value");
	
	// TODO rest get search/{query}

	$("#search-result").removeClass("hidden");
	// TODO handle search
	// $("#search-result-info").text(result.info);
	// TODO result;
};

// Handle Shelves Administration
/**
 * Show the selected shelf
 */
LocalGed.showShelf = function () {
	var btn = $(this);
	var id = btn.attr("id").substring("btnShowShelf-".length);
	LocalGed.showDetailShelf(id);
};
/**
 * Show Shelf creation dialog
 */
LocalGed.addShelf = function() {
	$("#diaNewShelf").modal('show');
};
/**
 * Show shelf (new or existing)
 * @param id the shelf id of false
 */
LocalGed.showDetailShelf = function(id) {
	// Hide shelves
	$("#shelves").addClass("hidden");
	// Hide source
	$("#sourceDetail").addClass("hidden");
	// show Detail
	$("#shelfDetail").removeClass("hidden");
	
	// Detail of a Shelf
	LocalGed.shelf = id;
	$("#shelf-update-id").attr("value",id);
	
	var name = $("#shelf-"+id+" h2").text();
	$("#shelf-name").attr("value",name);

	var descr = $("#shelf-"+id+" p").text();
	$("#shelf-description").attr("value",descr);
	
	// Save button
	$("#btnSaveShelf").text("Update");
	$("#btnAddSource").removeAttr("disabled");
	
	// REST get shelf/{id}
	var shelves = new RestServiceJs("rest/shelves");
	shelves.find(id, function(json) {
		$("#shelf-name").attr("value",json.name);
		$("#shelf-description").attr("value",json.description)		
	});
};
/**
 * Delete a shelf
 */
LocalGed.deleteShelf = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnDelShelf-".length);
	
	var name = $("#shelf-"+id +" h2").text();
	var msg = "Do you realy want to delete '" + name+"' ?";

	LocalGed.doConfirm(msg, function() {
		// REST delete shelf/{id}
		var shelves = new RestServiceJs("rest/shelves");
		shelves.remove(id, function() {
			LocalGed.loadShelves(); 
		});
	});
};
/**
 * Update the current shelf
 */
LocalGed.saveShelf = function() {
	var shelf = {};
	shelf.id = $("#shelf-update-id").attr("value");
	shelf.name = $("#shelf-name").attr("value");
	shelf.description = $("#shelf-description").attr("value");
	
	//  REST put shelf
	var shelves = new RestServiceJs("rest/shelves");
	shelves.put(shelf, function(json) {
		// update field on OK
		$("#shelf-name").attr("value",json.name);
		$("#shelf-description").attr("value",json.description)		
	});
};
/**
 * Create the current shelf
 */
LocalGed.createShelf = function() {
	var shelf = {};
	shelf.name = $("#shelf-create-name").attr("value");
	shelf.description = $("#shelf-create-description").attr("value");
	
	var shelves = new RestServiceJs("rest/shelves");
	shelves.put(shelf, function(json) {
		$('#diaNewShelf').modal('hide');
		LocalGed.loadShelves();
	});
};
/**
 * Show all Shelves
 */
LocalGed.backShelves = function() {
	// Hide details
	$("#shelfDetail").addClass("hidden");
	// show Shelves
	$("#shelves").removeClass("hidden");
	
	LocalGed.shelf = null;
	LocalGed.loadShelves();
};
/**
 * Load all Shelves
 */
LocalGed.loadShelves = function() {
	var shelves = new RestServiceJs("rest/shelves");
	shelves.findAll(function(json) {
		// Refresh shelves
		$("#shelves .row").html('');
		// TODO mustache.js
		$.each(json, function(index,s){
			var html = '';
			var id= s.id;
			html += '<div id="shelf-'+id+'" class="span10 shelf offset1">';
			html += '<h2>'+s.name+'</h2>';
			html += '<p>'+s.description+'</p>';
			html += '<a id="btnShowShelf-'+id+'" class="btn showShelf" href="#">View details &raquo;</a>';
			html += '<div class="pull-right"><a id="btnDelShelf-'+id+'" class="btn btn-danger delShelf" href="#"><i class="icon-trash icon-white"></i>&nbsp;Delete</a></div>';
			html += '</div>'
			$("#shelves .row").append(html);

			// bind actions
			$("#btnShowShelf-"+id).click(LocalGed.showShelf);
			$("#btnDelShelf-"+id).click(LocalGed.deleteShelf);
		});
	});
	
}

//Handle Sources Administration
/**
* Go back to current shelf
*/
LocalGed.backShelf = function() {
	var shelfId = LocalGed.shelf;
	LocalGed.showDetailShelf(shelfId);	
};
/**
* Show selected source
*/
LocalGed.showSource = function () {
	var btn = $(this);
	var id = btn.attr("id").substring("btnShowSource-".length);
	LocalGed.showDetailSource(id);
};
/**
* Create a new Source
*/
LocalGed.addSource = function () {
	$("#diaNewSource").modal('show');
};
/**
* Show source (new or existing)
* @param id the source id of false
*/
LocalGed.showDetailSource = function (id) {
	// Hide shelves
	$("#shelves").addClass("hidden");
	// Hide Shelf
	$("#shelfDetail").addClass("hidden");
	// Show source
	$("#sourceDetail").removeClass("hidden");
	
	// Detail of current source
	$("#source-id").attr("value",id);
	LocalGed.source = id;
	
	var name = $("#source-"+id+" h2").text();
	$("#source-name").attr("value",name);

	var descr = $("#source-"+id+" p").text();
	$("#source-description").attr("value",descr);
	
	// TODO REST get source/{id}
	
	// Save button
	$("#btnSaveSource").text("Update");
};
/**
 * Update the selected source 
 */
LocalGed.saveSource = function() {
	var src = {};
	src.id = $("#source-id").attr("value");
	src.name = $("#source-name").attr("value");
	src.description = $("#source-description").attr("value");
	
	// Folder attributes 
	src.folder = $("#source-folder").attr("value");
	src.recursif = ("checked"==$("#source-recursif").attr("checked"));
	
	// TODO REST put source
	// TODO update field on OK
};
/**
 * Create a new source 
 */
LocalGed.createSource = function() {
	var src = {};
	src.name = $("#source-name").attr("value");
	src.description = $("#source-description").attr("value");
	
	// Folder attributes 
	src.folder = $("#source-folder").attr("value");
	src.recursif = ("checked"==$("#source-recursif").attr("checked"));
	
	// TODO REST put source
	// TODO update field on OK
};
/**
 * Delete a source
 */
LocalGed.deleteSource = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnDelSource-".length);
	
	var name = $("#source-"+id +" h2").text();
	var msg = "Do you realy want to delete '" + name+"' ?";

	LocalGed.doConfirm(msg, function() {
		// TODO REST delete shelf/{id}
	});
};

// REST
function RestServiceJs(newurl) {
	this.myurl = newurl;

	this.post = function(model, callback) {
		$.ajax({
			type: 'POST',
			url: this.myurl,
			data: JSON.stringify(model), // '{"name":"' + model.name + '"}',
			dataType: 'text',
			processData: false,
			contentType: 'application/json',
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	};

	this.put= function(model, callback) {
		$.ajax({
			type: 'PUT',
			url: this.myurl,
			data: JSON.stringify(model), // '{"name":"' + model.name + '"}',
			dataType: 'text',
			processData: false,
			contentType: 'application/json',
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	};
	
	this.find = function(id, callback) {
		$.ajax({
			type: 'GET',
			url: this.myurl + '/' + id,
			contentType: 'application/json',
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	};

	this.findAll = function(callback) {
		$.ajax({
			type: 'GET',
			url: this.myurl,
			contentType: 'application/json',
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	};
	
	this.remove = function(id, callback) {
		$.ajax({
			type: 'DELETE',
			url: this.myurl + '/' + id,
			contentType: 'application/json',
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	};
	
	this.loadTmpl = function(turl, callback) {
		$.ajax({
			url: turl,
			success: callback,
			error: function(req, status, ex) {},
			timeout:60000
		});
	}
};

// Utilities
/**
 * Do on Enter
 */
LocalGed.doOnEnter = function(event,func) {
	if (e.which==13)  {
		e.preventDefault();
		func();
		return false;
	}
	return true;
};
/**
 * Confirm
 */
LocalGed.doConfirm = function(msg, func) {
	if (confirm(msg)) {
		func();
	}
};
/**
 * Error
 */
LocalGed.doError = function(msg) {
	alert(msg);
};

