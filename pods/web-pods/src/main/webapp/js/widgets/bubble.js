google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    var nodes = document.getElementsByClassName("bubble-graph");
    var len = nodes.length;
    var charts = {};
    var values = {};
    var selectX = {};
    var selectY = {};
    var selectColor = {};
    var graphDivs = {};

    for (var i = 0; i < len; i++) {
        // Extract the node and all its properties
        var masterDiv = nodes[i];
        var id = nodes[i].getAttribute("id");
        var channelname = nodes[i].getAttribute("data-channel");
        var xColumn = nodes[i].getAttribute("data-x-column");
        var yColumn = nodes[i].getAttribute("data-y-column");
        var colorColumn = nodes[i].getAttribute("data-color-column");
        var readOnly = "true";
        
        // Prepare html
        var tableDiv = document.createElement("div");
        tableDiv.style.display = "table";
        tableDiv.style.height = "100%";
        tableDiv.style.width = "100%";
        masterDiv.appendChild(tableDiv);
        
        var columnsDiv = document.createElement("div");
        columnsDiv.style.display = "table-row";
        tableDiv.appendChild(columnsDiv);
        selectX[i] = document.createElement("select");
        selectX[i].id = id + "-select-x";
        columnsDiv.appendChild(selectX[i]);
        selectY[i] = document.createElement("select");
        selectY[i].id = id + "-select-y";
        columnsDiv.appendChild(selectY[i]);
        selectColor[i] = document.createElement("select");
        selectColor[i].id = id + "-select-color";
        columnsDiv.appendChild(selectColor[i]);

        var graphDiv = document.createElement("div");
        graphDiv.style.display = "table-row";
        graphDiv.style.height = "100%";
        tableDiv.appendChild(graphDiv);
        graphDivs[i] = graphDiv;
        
        // Helper functions
        var populateSelect = function (select, list) {
            var currentSelection = null;
            if (select.selectedIndex !== -1) {
                currentSelection = select.options[select.selectedIndex].text;
            }
            for (nElement = 0; nElement < list.length; nElement++) {
                if (nElement < select.options.length) {
                    select.options[nElement].text = list[nElement];
                } else {
                    var option = document.createElement("option");
                    option.text = list[nElement];
                    select.add(option, select[nElement]);
                }
                if (select[nElement].text === currentSelection) {
                    select.selectedIndex = nElement;
                }
            }
            if (select.options.length > list.length) {
                for (nElement = select.options.length - 1; nElement >= list.length; nElement--) {
                    select.remove(nElement);
                }
            }
        };
        
        var processValue = function (channel, nNode) {
            var value = values[channel.getId()];
            populateSelect(selectX[nNode], value.columnNames);
            populateSelect(selectY[nNode], value.columnNames);
            populateSelect(selectColor[nNode], value.columnNames);
            var xId = selectX[nNode].selectedIndex;
            var yId = selectY[nNode].selectedIndex;
            var colorId = selectColor[nNode].selectedIndex;
            var dataArray = [];
            dataArray[0] = ['ID', selectX[nNode].options[xId].text, selectY[nNode].options[yId].text, selectColor[nNode].options[colorId].text];
            var nPoints = value.columnValues[xId].length;
            for (var i=0; i < nPoints; i++) {
                dataArray[i+1] = ['', value.columnValues[xId][i], value.columnValues[yId][i], value.columnValues[colorId][i]];
            }
            var data = google.visualization.arrayToDataTable(dataArray);


            var options = {
                hAxis: {title: selectX[nNode].options[xId].text},
                vAxis: {title: selectY[nNode].options[yId].text},
                bubble: {textStyle: {fontSize: 11}},
                sizeAxis: {minValue: 0,  maxSize: 10}
            };

            charts[channel.getId()].draw(data, options);
        };
        
        var addError = function (message, channel, nNode) {
            google.visualization.errors.addError(graphDivs[nNode],
                message, "", {'removable': true});
        };
        
        var createCallback = function (nNode) {

            return function (evt, channel) {
                switch (evt.type) {
                    case "connection": //connection state changed
                        channel.readOnly = !evt.writeConnected;
                        break;
                    case "value": //value changed
                        values[channel.getId()] = evt.value;
                        processValue(channel, nNode);
                        break;
                    case "error": //error happened
                        addError(evt.error, channel, nNode);
                        break;
                    case "writePermission":	// write permission changed.
                        break;
                    case "writeCompleted": // write finished.
                        break;
                    default:
                        break;
                }
            };
        
        };
        
        var channel = wp.subscribeChannel(channelname, createCallback(i), readOnly);

        var data = google.visualization.arrayToDataTable([
            ['ID', 'X', 'Y', 'Color', 'Size'],
            ['', 0, 0, '', 0]
        ]);

        var options = {
            title: 'Waiting for data',
            hAxis: {title: ''},
            vAxis: {title: ''},
            bubble: {textStyle: {fontSize: 11}}
        };
        
        populateSelect(selectX[i], [xColumn]);
        populateSelect(selectY[i], [yColumn]);
        populateSelect(selectColor[i], [colorColumn]);

        var chart = new google.visualization.BubbleChart(graphDiv);
        chart.draw(data, options);

        charts[channel.getId()] = chart;
        
        var createOnClick = function (theChannel, nNode) {
            return function () {
                processValue(theChannel, nNode);
            };
        };
        
        selectX[i].onclick = createOnClick(channel, i);
        selectY[i].onclick = createOnClick(channel, i);
        selectColor[i].onclick = createOnClick(channel, i);
    }
}
