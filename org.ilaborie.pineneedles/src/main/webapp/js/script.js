/* Author: Igor Laborie <ilaborie@gmail.com>
	TODO @see http://blog.ippon.fr/2012/02/20/mustache-js-et-icanhaz-js-au-secours-de-jquery/
 */
var LocalGed = {
	currentPage : 'search', 				// Between 'search', 'admin', ‘about‘
	shelf : null,							// Current shelf
	source : null,							// Current source
	confirm: null,							// Confirm function
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
	$("#shelf-create-name").keypress(LocalGed.handleCreateShelf);
	$("#shelf-name").keypress(LocalGed.handleUpdateShelf);
	$("#btnCreateShelf").click(LocalGed.createShelf);
	$("#diaNewShelf").on('shown', function() { 
		$("#shelf-create-name").focus(); 
	});
	
	// Sources Administration
	$("#btnBackShelf").click(LocalGed.backShelf);
	$("#btnAddSource").click(LocalGed.addSource);
	$(".showSource").click(LocalGed.showSource);
	$(".delSource").click(LocalGed.deleteSource);
	$("#btnCreateShelf").click(LocalGed.createSource);
	$("#btnSaveShelf").click(LocalGed.saveSource);
	
	// Utilities
	$("#diaConfirm").on('shown', function() { 
		$("#btnConfirm").focus(); 
	});
	$("#btnConfirm").click(function(){
		$("#diaConfirm").modal('hide');
		if (LocalGed.confirm) {
			LocalGed.confirm();
		}
	});
	
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
	$("#shelf-create-name").attr("value","");
	$("#shelf-create-description").attr("value","");
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
			// Display
			$("#shelf-"+id).hide();
			$("#shelf-"+id).detach();
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
		
		LocalGed.notify("admin","success","Shelf updated");
	});
};
/**
 * Handle Create Shelf
 */
LocalGed.handleCreateShelf = function(e) {
	LocalGed.doOnEnter(e,LocalGed.createShelf);
}
/**
 * Handle Update Shelf
 */
LocalGed.handleUpdateShelf = function(e) {
	LocalGed.doOnEnter(e,LocalGed.saveShelf);
}
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
		LocalGed.shelf = JSON.parse(json).id;
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
		$("#shelves .row").empty();
		// TODO mustache.js
		$.each(json, function(index,s){
			var id = s.id;

			var html= ich.shelf(s);
			$("#shelves .row").append(html);
			
			// bind actions
			$("#btnShowShelf-"+id).click(LocalGed.showShelf);
			$("#btnDelShelf-"+id).click(LocalGed.deleteShelf);
			
			// Display
			$("#shelf-"+id).fadeIn();
		});
		
		if (LocalGed.shelf) {
			$("#btnShowShelf-"+LocalGed.shelf).focus();
			LocalGed.shelf = null;
		}
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
			error: LocalGed.restError,
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
			error: LocalGed.restError,
			timeout:60000
		});
	};
	
	this.find = function(id, callback) {
		$.ajax({
			type: 'GET',
			url: this.myurl + '/' + id,
			contentType: 'application/json',
			success: callback,
			error: LocalGed.restError,
			timeout:60000
		});
	};

	this.findAll = function(callback) {
		$.ajax({
			type: 'GET',
			url: this.myurl,
			contentType: 'application/json',
			success: callback,
			error: LocalGed.restError,
			timeout:60000
		});
	};
	
	this.remove = function(id, callback) {
		$.ajax({
			type: 'DELETE',
			url: this.myurl + '/' + id,
			contentType: 'application/json',
			success: callback,
			error: LocalGed.restError,
			timeout:60000
		});
	};
	
	this.loadTmpl = function(turl, callback) {
		$.ajax({
			url: turl,
			success: callback,
			error: LocalGed.restError,
			timeout:60000
		});
	}
};

// Utilities
/**
 * Do on Enter
 */
LocalGed.doOnEnter = function(event,func) {
	if (event.which==13)  {
		event.preventDefault();
		func();
		return false;
	}
	return true;
};
/**
 * Show notify block
 */
LocalGed.notify = function(page,kind,message) {
	var div = $('#' +page +'-notify');
	div.empty();
	
	var data ={};
	data.kind = kind;
	data.message = message;
	
	if (kind=='success') {
		data.title = "Yeah !";
	} else if (kind=='error') {
		data.title = "Ooops !";
	} else if (kind=='info') {
		data.title = "Yop !";
	} else {
		data.title = "Hey !";
	}
	
	var html = ich.notify(data);
	div.append(html);
	
	// Animate
	div.fadeIn().delay(4000).fadeOut('slow'); 
}
/**
 * Confirm
 */
LocalGed.doConfirm = function(msg, func) {
	LocalGed.confirm = func; 
	$("#diaConfirmMessage").html(msg);
	$("#diaConfirm").modal('show');
};
/**
 * Handle rest error
 */
LocalGed.restError = function (req, status, ex) {
	var msg = "Error " + status + " while executing "+req;
	if (ex) {
		msg += "\n" + ex;
	}
	LocalGed.doError(msg);
}
/**
 * Error
 */
LocalGed.doError = function(msg) {
	alert(msg);
};

