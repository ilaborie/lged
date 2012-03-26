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

	$("#source-create-name").keypress(LocalGed.handleCreateSource);
	$("#source-create-folder").keypress(LocalGed.handleCreateSource);

	$("#source-name").keypress(LocalGed.handleSaveSource);
	$("#source-folder-field").keypress(LocalGed.handleSaveSource);

	$("#btnCreateSource").click(LocalGed.createSource);
	$("#btnSaveSource").click(LocalGed.saveSource);

	$("#diaNewSource").on('shown', function() {
		$("#source-create-name").focus();
	});
	// Links administration
	$("#btnAddLink").click(LocalGed.addLink);
	$("#btnCreateLink").click(LocalGed.createLink);
	$("#btnSaveLink").click(LocalGed.updateLink);
	$("#diaNewLink").on('shown', function() {
		$("#source-create-link").focus();
	});
	$("#diaUpdateLink").on('shown', function() {
		$("#source-update-link-tags").focus();
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
	$("#admin-content").addClass("hidden");
	$("#q").focus();
};
/**
 * Show Admin Page
 */
LocalGed.showAdmin = function() {
	LocalGed.switchPage('admin');
	$("#admin-content").removeClass("hidden");
	LocalGed.loadShelves();
};
/**
 * Show About Page
 */
LocalGed.showAbout = function() {
	LocalGed.switchPage('about');
	$("#admin-content").addClass("hidden");
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
	search
			.findAll(function(json) {
				$("#search-result").removeClass("hidden");

				var msg = "Found " + json.results + " documents in "
						+ json.time + "ms";
				$("#search-result-info").html(msg);

				// Display result
				$("#search-result").html(ich.docs(json));
				$("#search-result").fadeIn();
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
	$("#shelf-create-name").val("");
	$("#shelf-create-description").val("");
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
	$("#shelf-update-id").val(id);

	var name = $("#shelf-" + id + " h2").text();
	$("#shelf-name").val(name);
	$("#shelf-name").focus();

	var descr = $("#shelf-" + id + " p").text();
	$("#shelf-description").val(descr);

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
		$("#shelf-name").val(json.name);
		$("#shelf-description").val(json.description)
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
		var s = JSON.parse(json);
		$("#shelf-name").val(s.name);
		$("#shelf-description").val(s.description)

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
		$("#shelves").empty();
		$("#shelves").html(ich.shelves({
			shelves : json
		}));

		// bind actions
		$(".showShelf").click(LocalGed.showShelf);
		$(".delShelf").click(LocalGed.deleteShelf);
		if ($("#btnAddShelf").click != LocalGed.addShelf) {
			$("#btnAddShelf").click(LocalGed.addShelf);
		}

		$("#shelves .row").fadeIn();

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

	$("#source-create-name").val("");
	$("#source-create-description").val("");
	$("#source-create-folder").val("");
	$("#source-create-recursif").attr("checked", true);
	$("#source-create-links").val("");

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
	$("#source-id").val(id);
	LocalGed.source = id;

	var name = $("#source-" + id + " h2").text();
	$("#source-name").val(name);
	$("#source-name").focus();

	var descr = $("#source-" + id + " p").text();
	$("#source-description").val(descr);

	$("#breadcrumb-shelves").addClass("hidden");
	$("#breadcrumb-shelf").addClass("hidden");
	$("#breadcrumb-source").removeClass("hidden");

	$("#breadcrumb-source li.active").text(name);

	// Save button
	$("#btnSaveSource").text("Update");

	// REST get sources/{id}
	var sources = new RestServiceJs("rest/sources");
	sources.find(id, LocalGed.refreshSource);
};
/**
 * Refresh Resource
 */
LocalGed.refreshSource = function(json) {
	$("#source-name").val(json.name);
	$("#source-description").val(json.description);

	if (json.folder) {
		$("#source-folder-detail").removeClass("hidden");
		$("#source-links-detail").addClass("hidden");

		$("#source-folder-field").val(json.folder);
		$("#source-recursif").attr("checked", json.recursive);

	} else if (json.links) {
		$("#source-folder-detail").addClass("hidden");
		$("#source-links-detail").removeClass("hidden");

		$("#source-links-all").empty();

		var tmp = new Array();
		var elt;
		var lnk;
		for ( var i = 0; i < json.links.length; i++) {
			lnk = json.links[i];
			elt = tmp[lnk.displayHost];
			if (!elt) {
				elt = {
					host : lnk.displayHost,
					links : []
				}
				tmp[lnk.displayHost] = elt;
			}
			elt.links.push(lnk);
		}
		var group = {
			hosts : []
		};
		for (prop in tmp) {
			group.hosts.push(tmp[prop]);
		}
		var html = ich.links(group);
		$("#source-links-all").append(html);
		
		// Actions Bindings
		$(".delEntry").click(LocalGed.deleteLink);
		$(".editEntry").click(LocalGed.showUpdateLink);
	}
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
		$("#source-name").val(json.name);
		$("#source-description").val(json.description)

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
		$("#shelf-sources").empty();
		$("#shelf-sources").html(ich.sources({
			sources : json
		}));

		// bind actions
		$(".showSource").click(LocalGed.showSource);
		$(".delSource").click(LocalGed.deleteSource);

		if ($("#btnAddSource").click != LocalGed.addSource) {
			$("#btnAddSource").click(LocalGed.addSource);
		}
		if ($("#btnAddFolderSource").click != LocalGed.addFolders) {
			$("#btnAddFolderSource").click(LocalGed.addFolders);
		}
		if ($("#btnAddLinksSource").click != LocalGed.addLinks) {
			$("#btnAddLinksSource").click(LocalGed.addLinks);
		}
		if ($("#source-create-type").change != LocalGed.changeSource) {
			$("#source-create-type").change(LocalGed.changeSource)
		}

		$("#sources").fadeIn();

		if (LocalGed.source) {
			$("#btnShowSource-" + LocalGed.source).focus();
			LocalGed.source = null;
		}
	});
}
// Links
/**
 * Show Link creation dialog
 */
LocalGed.addLink = function() {
	$("#source-create-link").val("");
	$("#source-create-link-tags").val("");
	$("#diaNewLink").modal('show');
};
/**
 * Show Link update dialog
 */
LocalGed.showUpdateLink = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnEditLink-".length);
	
	$("#source-update-link-id").val(id);
	var lnk = $("#link-"+id + " a").attr("href");
	
	var tags = null;
	$("#link-"+id + " .label").each(function (i,e) {
		if (tags) {
			tags += ', ';
		} else {
			tags = '';
		}
		tags += $(e).text();
	});
	
	$("#source-update-link").val(lnk);
	$("#source-update-link-tags").val(tags);
	
	$("#diaUpdateLink").modal('show');
};
/**
 * Create a new source
 */
LocalGed.createLink = function() {
	var src = {};
	src.link = $("#source-create-link").val();
	src.tags = $("#source-create-link-tags").val();
	src.sourceId = LocalGed.source;

	var sources = new RestServiceJs("rest/links/link");

	// REST put source
	sources.put(src, function(json) {
		var src = JSON.parse(json);
		LocalGed.refreshSource(src);
		$("#diaNewLink").modal('hide');
	});
};
/**
 * Update a new source
 */
LocalGed.updateLink = function() {
	var src = {};
	src.id = $("#source-update-link-id").val();
	src.link = $("#source-update-link").val();
	src.tags = $("#source-update-link-tags").val();
	src.sourceId = LocalGed.source;

	var sources = new RestServiceJs("rest/links/link");

	// REST put source
	sources.put(src, function(json) {
		var src = JSON.parse(json);
		LocalGed.refreshSource(src);
		$("#diaUpdateLink").modal('hide');
	});
};

/**
 * Delete a Link
 */
LocalGed.deleteLink = function() {
	var btn = $(this);
	var id = btn.attr("id").substring("btnDelLink-".length);

	var name = $("#link-" + id + " a").text();
	var msg = "Do you realy want to delete '" + name + "' ?";

	LocalGed.doConfirm(msg, function() {
		var sources = new RestServiceJs("rest/links");
		sources.remove(id, function() {
			// Display
			$("#link-" + id).hide();
			$("#link-" + id).detach();
		});
	});
};
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
