/*******************************************************************************
 * @author: eschuhmacher
 *
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("wp-progress-bar");
    var len = nodes.length;
    var progressbars = {};
    var currentAlarms = {};

    function updateProgressBarAlarm(severity, id, widget) {
        var currentAlarm = currentAlarms[id];
        if (currentAlarm) {
            widget.find( ".ui-progressbar-value" ).removeClass(currentAlarm);
        }
        switch (severity) {
            case "MINOR":
                currentAlarm = "alarm-minor";
                break;
            case "MAJOR":
                currentAlarm = "alarm-major";
                break;
            case "INVALID":
                currentAlarm = "alarm-invalid";
                break;
            case "UNDEFINED":
                currentAlarm = "alarm-undefined";
                break;
            default:
                currentAlarm = "alarm-none";
                break;
        }
        currentAlarms[id] = currentAlarm;
        widget.find( ".ui-progressbar-value" ).addClass(currentAlarm);
    }
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var max = nodes[i].getAttribute("data-displayHigh") != null ? parseFloat(nodes[i].getAttribute("data-displayHigh")) : 100;
        var min = nodes[i].getAttribute("data-displayLow") != null ? parseFloat(nodes[i].getAttribute("data-displayLow")) : 0;
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "wp-progress-bar-" + i;
            nodes[i].id = id;
        }
        var callback = function(evt, channel) {
           switch (evt.type) {
           case "connection": //connection state changed
               channel.readOnly = !evt.writeConnected;
               break;
           case "value": //value changed
               var channelValue = channel.getValue();
               if ("value" in channelValue) {
                   if(channelValue.display && channelValue.display.highDisplay != null) {
                       progressbars[channel.getId()].progressbar({"value" : channelValue.value, "max" : channelValue.display.highDisplay}).children('.ui-progressbar-value')
                                                    .html(channelValue.value).css("display", "block");

                   } else {
                        progressbars[channel.getId()].progressbar( "value", channelValue.value ).children('.ui-progressbar-value')
                                                     .html(channelValue.value).css("display", "block");
                   }
               } else {
                   // If something else, display the type name
                   progressbars[channel.getId()].progressbar( "value", false).children('.ui-progressbar-value')
                                                .html(channelValue.type.name).css("display", "block");
               }
               // Change the style based on the alarm
               if ("alarm" in channelValue) {
                   updateProgressBarAlarm(channelValue.alarm.severity, channel.getId(), progressbars[channel.getId()]);
               } else {
                   updateProgressBarAlarm("NONE", channel.getId(), progressbars[channel.getId()]);
               }

               break;
           case "error": //error happened
               // Change displayed alarm to invalid, and set the
               updateProgressBarAlarm("INVALID", channel.getId(), progressbars[channel.getId()]);
               break;
           case "writePermission":	// write permission changed.
               break;
           case "writeCompleted": // write finished.
               break;
           default:
               break;
           }
        };
        var channel = wp.subscribeChannel(channelname, callback, readOnly);
        var progressLabel = $( ".progress-label" );
        var progressbar = $("#" + id).progressbar({value: false });
        progressbars[channel.getId()] = progressbar;
    }

});


window.onbeforeunload = function() {
	wp.close();
};
