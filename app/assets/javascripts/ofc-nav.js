function decorateNumber( id, path ) {
    $.getJSON( path, function( data ) {

        var numItems = data['count']

        if (numItems == 0) {
            $("#" + id).addClass("hidden")
        } else {
            $("#" + id).removeClass("hidden").html('&nbsp' + numItems + '&nbsp')
        }
    });
}