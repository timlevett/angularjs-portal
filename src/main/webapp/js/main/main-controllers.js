'use strict';

(function() {
  var app = angular.module('portal.main.controllers', []);

  app.controller('MainController', [ '$firebase', '$sessionStorage', '$rootScope', '$scope', 'mainService', 'miscService', function($firebase, $sessionStorage, $rootScope, $scope, mainService, miscService) {


    miscService.pushPageview();
    $scope.layout = [];

    mainService.getLayout().then(function(data){
      $scope.layout = data.layout;
    });

    this.directToPortlet = function directToPortlet(url) {
      $location.path(url);
    }
    this.removePortlet = function removePortletFunction(nodeId, title) {
        $.ajax({
                url: "/portal/api/layout?action=removeElement&elementID=" + nodeId,
                type: "POST",
                data: null,
                dataType: "json",
                async: true,
                success: function (request, text){
                  $scope.$apply(function(){
                    var result = $.grep($scope.layout, function(e) { return e.nodeId === nodeId});
                    var index = $.inArray(result[0], $scope.layout);
                    //remove
                    $scope.layout.splice(index,1);
                    if($sessionStorage.marketplace != null) {
                        var marketplaceEntries = $.grep($sessionStorage.marketplace, function(e) { return e.fname === result[0].fname});
                        if(marketplaceEntries.length > 0) {
                            marketplaceEntries[0].hasInLayout = false;
                        }
                    }
                  });
                  miscService.pushGAEvent('Layout Modification', 'Remove', title);
                },
                error: function(request, text, error) {

                }
            });
      };
      
      $scope.sortableOptions = {
              cursorAt : {top: 15, left: 15},
    		  stop: function(e, ui) {
    		      if(ui.item.sortable.dropindex != ui.item.sortable.index) {
    		          
        		      var node = $scope.layout[ui.item.sortable.dropindex];
        		      console.log("Change happened, logging move of " + node.fname + " from " + ui.item.sortable.index + " to " + ui.item.sortable.dropindex);
        		      //index, length, movingNodeId, previousNodeId, nextNodeId
        		      var prevNodeId = ui.item.sortable.dropindex != 0 ? $scope.layout[ui.item.sortable.dropindex - 1].nodeId : null;
        		      var nextNodeId = ui.item.sortable.dropindex != $scope.layout.length - 1 ? $scope.layout[ui.item.sortable.dropindex + 1].nodeId : null;
        		      mainService.moveStuff(ui.item.sortable.dropindex, $scope.layout.length, node.nodeId, prevNodeId, nextNodeId);
        		      
    		      }
    		  }
      };
      
      this.toggleDiv = function toggleDiv(nodeId) {
          //Toggle Make full row
          $('#portlet-id-' + nodeId).parent().parent('.portlet-container').toggleClass('col-sm-6');
          $('#portlet-id-' + nodeId).parent().parent('.portlet-container').toggleClass('col-md-6');
          $('#portlet-id-' + nodeId).parent().parent('.portlet-container').toggleClass('col-lg-4');

          $('#portlet-id-' + nodeId).parent().parent('.portlet-container').toggleClass('col-sm-12');

          //Toggle height : auto
          if( $('#portlet-id-' + nodeId).css('height') == '150px') {
              $('#portlet-id-' + nodeId).css('height','auto');
          } else {
              $('#portlet-id-' + nodeId).css('height','150px');
    	  }
    	  
    	  //Toggle content visible
    	  $('#content-' + nodeId).toggleClass('hidden');
      };

  } ]);

  /* Username */

  app.controller('SessionCheckController', [ 'mainService', function(mainService) {
    var that = this;
    that.user = [];
    mainService.getUser().then(function(result){
      that.user = result.data.person;
    });
  }]);

  /* Header */
  app.controller('HeaderController', ['$scope','$location', 'marketplaceService', function($scope, $location, marketplaceService) {
    $scope.showSearch = false;
    $scope.submit = function(){
      if($scope.initialFilter != "") {
        marketplaceService.initialFilter($scope.initialFilter);
        $location.path("/apps/search/"+ $scope.initialFilter);
        $scope.initialFilter = "";
        $scope.showSearch = false;
      }
    };
  }]);



})();
