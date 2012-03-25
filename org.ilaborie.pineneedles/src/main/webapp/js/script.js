/* Author: Igor Laborie <ilaborie@gmail.com>
 */
var LocalGed = {
	currentPage : 'search', // Between 'search', 'admin', ‘about‘
	shelf : null, // Current shelf
	source : null, // Current source
	confirm : null, // Confirm function
	debug : true
// Debug mode
};

// Do when page is loading
$(document).ready(function() {
	// Add a startsWith function on String
	if (typeof String.prototype.startsWith != 'function') {
		String.prototype.startsWith = function(str) {
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
	$("#search-input form").submit(LocalGed.doSearch);

	// Shelves Administration
	$("#btnAddShelf").click(LocalGed.addShelf);
	$("#btnBackShelves").click(LocalGed.backShelves);

	$("#btnSaveShelf").click(LocalGed.saveShelf);
	$("#btnCreateShelf").click(LocalGed.createShelf);

	$("#shelf-create-name").keypress(LocalGed.handleCreateShelf);
	$("#shelf-name").keypress(LocalGed.handleUpdateShelf);
	$("#diaNewShelf").on('shown', function() {
		$("#shelf-create-name").focus();
	});

	// Sources Administration
	$("#btnBackShelf").click(LocalGed.backShelf);

	$("#btnAddSource").click(LocalGed.addSource);
	$("#btnAddFolderSource").click(LocalGed.addFolders);
	$("#btnAddLinksSource").click(LocalGed.addLinks);
	$("#source-create-type").change(LocalGed.changeSource)

	$("#source-create-name").keypress(LocalGed.handleCreateSource);
	$("#source-create-folder").keypress(LocalGed.handleCreateSource);
	$("#source-create-links").keypress(LocalGed.handleCreateSource);

	$("#source-name").keypress(LocalGed.handleSaveSource);
	$("#source-folder-field").keypress(LocalGed.handleSaveSource);
	$("#source-links").keypress(LocalGed.handleSaveSource);

	$("#btnCreateSource").click(LocalGed.createSource);
	$("#btnSaveSource").click(LocalGed.saveSource);

	$("#diaNewSource").on('shown', function() {
		$("#source-create-name").focus();
	});

	// Breadcrumb
	$(".returnToShelves").click(LocalGed.backShelves);
	$(".returnToShelf").click(LocalGed.backShelf);

	// Utilities
	$("#diaConfirm").on('shown', function() {
		$("#btnConfirm").focus();
	});
	$("#btnConfirm").click(function() {
		$("#diaConfirm").modal('hide');
		if (LocalGed.confirm) {
			LocalGed.confirm();
		}
	});

	// Initial State
	LocalGed.showSearch();
	$("#q").focus();
});

// Handle Menu
/**
 * Switch to a page
 * 
 * @param page
 *            the page
 */
LocalGed.switchPage = function(page) {
	// Switch Menu
	$("#" + this.currentPage).parent().removeClass("active");
	this.currentPage = page;
	$("#" + this.currentPage).parent().addClass("active");

	// Switch content
	$(".content").each(function() {
		var elt = $(this);
		var id = elt.attr('id');
		if (id && id.startsWith(page)) {
			elt.removeClass("hidden");
		} else {
			elt.addClass("hidden");
		}
	});
};
/**
 * Show Search Page
 */
LocalGed.showSearch = function() {
	LocalGed.switchPage('search');
	$("#q").focus();
};
/**
 * Show Admin Page
 */
LocalGed.showAdmin = function() {
	LocalGed.switchPage('admin');
	LocalGed.loadShelves();
};
/**
 * Show About Page
 */
LocalGed.showAbout = function() {
	LocalGed.switchPage('about');
};

// Search
/**
 * Handle KeyPres on Search input
 */
LocalGed.handleSearch = function(e) {
	LocalGed.doOnEnter(e, LocalGed.doSearch);
};
/**
 * Launch search
 */
LocalGed.doSearch = function(event) {
	var query = $("#q").val();
	$("#search-result").addClass("hidden");
	
	// rest get search/{query}
	var search = new RestServiceJs("rest/search?q=" + query);
	search.findAll(function(json) {
		$("#search-result").removeClass("hidden");
		
		var msg = "Found " + json.results + " documents in " + json.time + "ms";
		$("#search-result-info").html(msg);
		
		// Display result
		$("#shelves .row").empty();
		$.each(json.docs, function(index, s) {
			var id = s.id;
			
			var html = ich.doc(s);
			$("#search-result .row").append(html);

			// Display
			$("#doc-" + id).fadeIn();
		});
	});

	if (event) {
		event.preventDefault();
	}
	return false;
};

// Handle Shelves Administration
/**
 * Show the selected shelf
 */
LocalGed.showShelf = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnShowShelf-".length);
	LocalGed.showDetailShelf(id);

	LocalGed.loadSources(id);
};
/**
 * Show Shelf creation dialog
 */
LocalGed.addShelf = function() {
	$("#shelf-create-name").attr("value", "");
	$("#shelf-create-description").attr("value", "");
	$("#diaNewShelf").modal('show');
};
/**
 * Show shelf (new or existing)
 * 
 * @param id
 *            the shelf id of false
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
	$("#shelf-update-id").attr("value", id);

	var name = $("#shelf-" + id + " h2").text();
	$("#shelf-name").attr("value", name);
	$("#shelf-name").focus();

	var descr = $("#shelf-" + id + " p").text();
	$("#shelf-description").attr("value", descr);

	$("#breadcrumb-shelves").addClass("hidden");
	$("#breadcrumb-shelf").removeClass("hidden");
	$("#breadcrumb-source").addClass("hidden");

	$("#breadcrumb-shelf li.active").text(name);
	$("#breadcrumb-source .returnToShelf").text(name);

	// Save button
	$("#btnSaveShelf").text("Update");
	$("#btnAddSource").removeAttr("disabled");

	// REST get shelf/{id}
	var shelves = new RestServiceJs("rest/shelves");
	shelves.find(id, function(json) {
		$("#shelf-name").attr("value", json.name);
		$("#shelf-description").attr("value", json.description)
	});
};
/**
 * Delete a shelf
 */
LocalGed.deleteShelf = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnDelShelf-".length);

	var name = $("#shelf-" + id + " h2").text();
	var msg = "Do you realy want to delete '" + name + "' ?";

	LocalGed.doConfirm(msg, function() {
		// REST delete shelf/{id}
		var shelves = new RestServiceJs("rest/shelves");
		shelves.remove(id, function() {
			// Display
			$("#shelf-" + id).hide();
			$("#shelf-" + id).detach();
		});
	});
};
/**
 * Update the current shelf
 */
LocalGed.saveShelf = function() {
	var shelf = {};
	shelf.id = $("#shelf-update-id").val();
	shelf.name = $("#shelf-name").val();
	shelf.description = $("#shelf-description").val();

	// REST put shelf
	var shelves = new RestServiceJs("rest/shelves");
	shelves.put(shelf, function(json) {
		// update field on OK
		$("#shelf-name").attr("value", json.name);
		$("#shelf-description").attr("value", json.description)

		LocalGed.notify("admin", "success", "Shelf updated");
	});
};
/**
 * Handle Create Shelf
 */
LocalGed.handleCreateShelf = function(e) {
	LocalGed.doOnEnter(e, LocalGed.createShelf);
}
/**
 * Handle Update Shelf
 */
LocalGed.handleUpdateShelf = function(e) {
	LocalGed.doOnEnter(e, LocalGed.saveShelf);
}
/**
 * Create the current shelf
 */
LocalGed.createShelf = function() {
	var shelf = {};
	shelf.name = $("#shelf-create-name").val();
	shelf.description = $("#shelf-create-description").val();

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
	$("#sourceDetail").addClass("hidden");
	// show Shelves
	$("#shelves").removeClass("hidden");

	$("#breadcrumb-shelves").removeClass("hidden");
	$("#breadcrumb-shelf").addClass("hidden");
	$("#breadcrumb-source").addClass("hidden");

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
		$.each(json, function(index, s) {
			var id = s.id;

			var html = ich.shelf(s);
			$("#shelves .row").append(html);

			// bind actions
			$("#btnShowShelf-" + id).click(LocalGed.showShelf);
			$("#btnDelShelf-" + id).click(LocalGed.deleteShelf);

			// Display
			$("#shelf-" + id).fadeIn();
		});

		if (LocalGed.shelf) {
			$("#btnShowShelf-" + LocalGed.shelf).focus();
			LocalGed.shelf = null;
		}
	});
}

// Handle Sources Administration
/**
 * Go back to current shelf
 */
LocalGed.backShelf = function() {
	var shelfId = LocalGed.shelf;
	LocalGed.showDetailShelf(shelfId);

	$("#breadcrumb-shelves").addClass("hidden");
	$("#breadcrumb-shelf").removeClass("hidden");
	$("#breadcrumb-source").addClass("hidden");

	var name = $("#shelf-name").val();
	$("#breadcrumb-shelf li.active").text(name);
	$("#breadcrumb-source .returnToShelf").text(name);
};
/**
 * Show selected source
 */
LocalGed.showSource = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnShowSource-".length);
	LocalGed.showDetailSource(id);
};
/**
 * Handle change source
 */
LocalGed.changeSource = function(e) {
	var type = $("#source-create-type").val();
	if (type == "folders") {
		$("#source-folders-create-detail").removeClass("hidden");
		$("#source-links-create-detail").addClass("hidden");

		$("#diaNewSource h3").text('Create a new Folder Source');
	} else if (type == "links") {
		$("#source-folders-create-detail").addClass("hidden");
		$("#source-links-create-detail").removeClass("hidden");

		$("#diaNewSource h3").text('Create a new Links Source');
	} else {
		$("#source-folders-create-detail").addClass("hidden");
		$("#source-links-create-detail").addClass("hidden");

		$("#diaNewSource h3").text('Create a new Source');
	}
}
/**
 * Create a new Source
 */
LocalGed.addSource = function(type) {
	var kind = 'folders';
	if (type == 'folders' || type == 'links') {
		kind = type;
	} else {
		$("#source-create-type-container").removeClass("hidden");
	}

	// Update Select
	$("#source-create-type").val(kind);
	LocalGed.changeSource();

	$("#source-create-type").removeClass("hidden");

	$("#source-create-name").attr("value", "");
	$("#source-create-description").attr("value", "");
	$("#source-create-folder").attr("value", "");
	$("#source-create-recursif").attr("checked", true);
	$("#source-create-links").attr("value", "");

	$("#diaNewSource").modal('show');
};
/**
 * Add a Folder Source
 */
LocalGed.addFolders = function(event) {
	$("#source-create-type-container").addClass("hidden");
	LocalGed.addSource("folders");
}
/**
 * Add a Links Source
 */
LocalGed.addLinks = function(event) {
	$("#source-create-type-container").addClass("hidden");
	LocalGed.addSource("links");
}
/**
 * Show source (new or existing)
 * 
 * @param id
 *            the source id of false
 */
LocalGed.showDetailSource = function(id) {
	// Hide shelves
	$("#shelves").addClass("hidden");
	// Hide Shelf
	$("#shelfDetail").addClass("hidden");
	// Show source
	$("#sourceDetail").removeClass("hidden");

	// Detail of current source
	$("#source-id").attr("value", id);
	LocalGed.source = id;

	var name = $("#source-" + id + " h2").text();
	$("#source-name").attr("value", name);
	$("#source-name").focus();

	var descr = $("#source-" + id + " p").text();
	$("#source-description").attr("value", descr);

	$("#breadcrumb-shelves").addClass("hidden");
	$("#breadcrumb-shelf").addClass("hidden");
	$("#breadcrumb-source").removeClass("hidden");

	$("#breadcrumb-source li.active").text(name);

	// Save button
	$("#btnSaveSource").text("Update");

	// REST get sources/{id}
	var sources = new RestServiceJs("rest/sources");
	sources.find(id, function(json) {
		$("#source-name").attr("value", json.name);
		$("#source-description").attr("value", json.description);

		if (json.folder) {
			$("#source-folder-detail").removeClass("hidden");
			$("#source-links-detail").addClass("hidden");

			$("#source-folder-field").attr("value", json.folder);
			$("#source-recursif").attr("checked", json.recursive);

		} else if (json.links) {
			$("#source-folder-detail").addClass("hidden");
			$("#source-links-detail").removeClass("hidden");

			$("#source-links-all").empty();
			$("#source-links-all").append("<ul></ul>");
			$.each(json.links, function(index, lnk) {
				var id = lnk.id;
				var html = ich.link(lnk);
				$("#source-links-all ul").append(html);
			});
		}
	});
};
/**
 * Handle save source
 */
LocalGed.handleSaveSource = function(event) {
	LocalGed.doOnEnter(event, LocalGed.saveSource);
}
/**
 * Update the selected source
 */
LocalGed.saveSource = function() {
	var src = {};
	src.id = $("#source-id").val();
	src.shelfId = LocalGed.shelf;
	src.name = $("#source-name").val();
	src.description = $("#source-description").val();

	var isFolder = !$("#source-folder-detail").hasClass("hidden");

	var sources;
	if (isFolder) {
		// Folder attributes
		src.path = $("#source-folder-field").val();
		src.recursive = $("#source-recursif").is(":checked");

		sources = new RestServiceJs("rest/folders");
	} else {
		src.links = $("#source-links").val();

		sources = new RestServiceJs("rest/links");
	}

	// REST put source
	sources.put(src, function(json) {
		// update field on OK
		$("#source-name").attr("value", json.name);
		$("#source-description").attr("value", json.description)
		// TODO location

		LocalGed.notify("admin", "success", "Source updated");
	});
};
/**
 * Handle create source
 */
LocalGed.handleCreateSource = function(event) {
	LocalGed.doOnEnter(event, LocalGed.createSource);
}
/**
 * Create a new source
 */
LocalGed.createSource = function() {
	var src = {};
	src.name = $("#source-create-name").val();
	src.description = $("#source-create-description").val();
	src.shelfId = LocalGed.shelf;

	var type = $("#source-create-type").val();

	var sources;
	if (type == "folders") {
		// Folder attributes
		src.path = $("#source-create-folder").val();
		src.recursive = $("#source-create-recursif").is(":checked");

		sources = new RestServiceJs("rest/folders");
	} else if (type == "links") {
		src.links = $("#source-create-links").val();

		sources = new RestServiceJs("rest/links");
	}

	// REST put source
	sources.put(src, function(json) {
		$('#diaNewSource').modal('hide');
		LocalGed.source = JSON.parse(json).id;
		LocalGed.loadSources(LocalGed.shelf);
	});
};
/**
 * Delete a source
 */
LocalGed.deleteSource = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnDelSource-".length);

	var name = $("#source-" + id + " h2").text();
	var msg = "Do you realy want to delete '" + name + "' ?";

	LocalGed.doConfirm(msg, function() {
		var sources = new RestServiceJs("rest/sources");
		sources.remove(id, function() {
			// Display
			$("#source-" + id).hide();
			$("#source-" + id).detach();
		});
	});
};
/**
 * Load Shelf Sources
 */
LocalGed.loadSources = function(shelfId) {
	var sources = new RestServiceJs("rest/sources/shelf");
	sources.find(shelfId, function(json) {
		// Refresh shelves
		$("#sources").empty();
		$.each(json, function(index, s) {
			var id = s.id;

			// Label & detail
			if (s.path) {
				s.kind = 'warning';
				s.kindLabel = 'Folder';
				s.detail = s.path;
			} else {
				s.kind = 'info';
				s.kindLabel = 'Links';
				s.detail = s.links;
			}

			var html = ich.source(s);
			$("#sources").append(html);

			// bind actions
			$("#btnShowSource-" + id).click(LocalGed.showSource);
			$("#btnDelSource-" + id).click(LocalGed.deleteSource);

			// Display
			$("#source-" + id).fadeIn();
		});

		if (LocalGed.source) {
			$("#btnShowSource-" + LocalGed.source).focus();
			LocalGed.source = null;
		}
	});
}

// REST
function RestServiceJs(newurl) {
	this.myurl = newurl;

	this.post = function(model, callback) {
		$.ajax({
			type : 'POST',
			url : this.myurl,
			data : JSON.stringify(model), // '{"name":"' + model.name + '"}',
			dataType : 'text',
			processData : false,
			contentType : 'application/json',
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	};

	this.put = function(model, callback) {
		$.ajax({
			type : 'PUT',
			url : this.myurl,
			data : JSON.stringify(model), // '{"name":"' + model.name + '"}',
			dataType : 'text',
			processData : false,
			contentType : 'application/json',
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	};

	this.find = function(id, callback) {
		$.ajax({
			type : 'GET',
			url : this.myurl + '/' + id,
			contentType : 'application/json',
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	};

	this.findAll = function(callback) {
		$.ajax({
			type : 'GET',
			url : this.myurl,
			contentType : 'application/json',
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	};

	this.remove = function(id, callback) {
		$.ajax({
			type : 'DELETE',
			url : this.myurl + '/' + id,
			contentType : 'application/json',
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	};

	this.loadTmpl = function(turl, callback) {
		$.ajax({
			url : turl,
			success : callback,
			error : LocalGed.restError,
			timeout : 60000
		});
	}
};

// Utilities
/**
 * Do on Enter
 */
LocalGed.doOnEnter = function(event, func) {
	if (event.which == 13) {
		event.preventDefault();
		func();
		return false;
	}
	return true;
};
/**
 * Show notify block
 */
LocalGed.notify = function(page, kind, message) {
	var div = $('#' + page + '-notify');
	div.empty();

	var data = {};
	data.kind = kind;
	data.message = message;

	if (kind == 'success') {
		data.title = "Yeah !";
	} else if (kind == 'error') {
		data.title = "Ooops !";
	} else if (kind == 'info') {
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
LocalGed.restError = function(req, status, ex) {
	var msg;
	if (status) {
		msg = status;
	} else {
		msg = "Error";
	}
	if (req && req.responseText) {
		msg += ": ";
		try {
			msg += JSON.parse(req.responseText).message;
		} catch (e) {
			msg += req.responseText;
		}
	}
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
