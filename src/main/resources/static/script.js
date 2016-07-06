$(document).ready(function() {
  var popupCloseTimeout = null;

  // define that the popup opens on a mouseover
  $('.popupLink').mouseover(function(e) {
    var pos = $(this).position();
    var width = $(this).outerWidth();

    // position the popup relative to the link
    $('#popup').css({
      position: 'absolute',
      top: pos.top + 'px',
      left: (pos.left + width) + 'px'
    }).show();

    // define a timeout in which the user needs to hover over the popup to let it stay open
    popupCloseTimeout = setTimeout(function() {
      $('#popup').hide();
    }, 2000);
  });

  // define that the timeout to close the popup will be removed when entering the popup and
  // that the popup will be hidden after leaving it
  $('#popup').hover(function(e) {
    clearTimeout(popupCloseTimeout);
  }, function(e) {
    $(this).hide();
  });
});