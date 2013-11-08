/*
$(function() {

	// Function to update meter settings with an ajax call
	var setMeterSetting = function(meterName, newSetting) {
		var data = {};
		data.setting = newSetting;
		$.ajax({
			'url': "/meter/" + meterName, 
			'type': 'PUT',
			'headers' : {'Content-Type' : 'application/json'},
			'data' : JSON.stringify(data),
			'processData': false,
			'dataType': 'text',
			'success': function(jsonReply) {
				//$("#msgDiv").html("<p>Debug area:</p><p>" + jsonReply + "</p>");
			},
			'error': function(jqXHR, data) {
				console.log(data);
			}
		});
	};

	// Function to initialize a selected page
	var initializeGroupingsMetersForPage = function(page) {

		// Make ajax call to retrieve all meter data
		$.get("/meters", function(meterData, status) {

			// Key the meters by name (for ease of reference)
			var meters = {};
			for (var m = 0; m < meterData.list.length; m++) {
				var meter = meterData.list[m];
				var meterObj = {};
				meterObj.setting = meter.setting;
				meterObj.value = meter.value;
				meters[meter.name] = meterObj;
			}

			// Make ajax call to retrieve groupings of meters for the specific page
			$.get("/page/" + page, function(jsonReply, status) {

				// Prepare a heading and table for each grouping of meters
				var $groupingsDiv = $("#groupingsDiv");
				$groupingsDiv.empty();
				for (var i = 0; i < jsonReply.groupings.length; i++) {
					var groupingName = jsonReply.groupings[i].name;
					var groupingId = groupingName.replace(" ", "");
					$groupingsDiv.append("<h1>" + groupingName + "</h1>");
					var $groupingTable = $("<table id='" + groupingId + "'></table>");
					$groupingTable.append("<tr><th>Meter</th><th>Value</th><th>Setting</th></tr>");

					// Prepare a table row for each meter within the grouping
					for (var j = 0; j < jsonReply.groupings[i].meters.length; j++) {
						var meterName = jsonReply.groupings[i].meters[j];
						var meterId = meterName.replace(" ", "");
						var meterValue = meters[meterName].value;
						var meterSetting = meters[meterName].setting;
						var $rdoIncrease = $("<input type='radio' name='" + groupingId + meterId + "' value='increase'>Increase</input>");
						var $rdoHold = $("<input type='radio' name='" + groupingId + meterId + "' value='hold'>Hold</input>");
						var $rdoDecrease = $("<input type='radio' name='" + groupingId + meterId + "' value='decrease'>Decrease</input>");
						if (meterSetting > 0) $rdoIncrease.prop('checked', true);
						if (meterSetting === 0) $rdoHold.prop('checked', true);
						if (meterSetting < 0) $rdoDecrease.prop('checked', true);

						var $settingEntry = $("<td class='meterSetting'/>");
						$settingEntry.append($rdoIncrease).append($rdoHold).append($rdoDecrease);

						// Attach event handlers that will update the meter settings when changed
						$settingEntry.children().on("change", function() {
							var $this = $(this);
							if ($this.prop('checked')) {
								var meterName = $this.parent().siblings().first().text();
								var newSetting = $this.val();
								setMeterSetting(meterName, newSetting);
							}
						});

						// Create and append the row
						var $meterRow = $("<tr id='" + meterId + "'></tr>");
						$meterRow.append("<td class='meterName'>" + meterName + "</td>")
							.append("<td class='meterValue'>" + meterValue + "</td>")
							.append($settingEntry);
						$groupingTable.append($meterRow);
					}

					// Append the table to the groupings div
					$groupingsDiv.append($groupingTable);
				}

				//$("#msgDiv").html("<p>Debug area:</p><p>" + JSON.stringify(jsonReply, null, '\t') + "</p>");
			});

		});

	};

	// Set up the change handler for the page selector
	$("#pageSelector").on("change", function() {
		initializeGroupingsMetersForPage($("#pageSelector :selected").val());
	});

	// set up the socket.io connection
	var socket = io.connect();

	// Set up the socket.io event handlers
	socket.on('nameResult', function(result) {
		var message;

		if (result.success) {
			message = 'You are now known as ' + result.name + '.';
		} else {
			message = result.message;
		}
		//$('#msgDiv').append("<p>" + message + "</p>");
	});

	socket.on('joinResult', function(result) {
		var message;
		message = "You are joined to channel " + result.channel;
		//$('#msdDiv').append("<p>" + message + "</p>");
	});

	socket.on('meterupdate', function(result) {

		// Get the meter information and use it to update the meter rows on the page
		var meters = result.list;
		for (var i = 0; i < meters.length; i++) {
			var meter = meters[i];
			var meterName = meter.name;
			var meterValue = meter.value;
			var meterSetting = meter.setting;
			var meterId = meterName.replace(" ", "");
			var $meterRow = $("#" + meterId);
			var $meterRowValueElem = $meterRow.children("td:nth-child(2)");
			$meterRowValueElem.text(meterValue);
			var $meterRowSettingElem = $meterRow.children("td:nth-child(3)");
			if (meterSetting > 0) $meterRowSettingElem.children("input:nth-child(1)").prop('checked', true);
			if (meterSetting === 0) $meterRowSettingElem.children("input:nth-child(2)").prop('checked', true);
			if (meterSetting < 0) $meterRowSettingElem.children("input:nth-child(3)").prop('checked', true);
		}
		//$('#msgDiv').append("<p>" + "set value to " + meterValue + "</p>");
	});

});
*/