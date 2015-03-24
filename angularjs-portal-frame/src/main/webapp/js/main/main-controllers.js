'use strict';

(function() {
  var app = angular.module('portal.main.controllers', []);

  app.controller('MainController', ['$localStorage', '$sessionStorage','$scope', function($localStorage, $sessionStorage, $scope) {
    var defaults = {
            showSidebar: true, 
            sidebarQuicklinks: false, 
            showKeywordsInMarketplace : false,
            homeImg : "img/square.jpg", 
            sidebarShowProfile: false, 
            profileImg: "img/terrace.jpg", 
            notificationsDemo : false,
            typeaheadSearch: false,
            exampleWidgets: false,
            layoutMode : 'list' //other option is 'widgets'
            };
    $scope.$storage = $localStorage.$default(defaults);
    
    $scope.resetLocal = function() {
        $localStorage.$reset(defaults);
    }
    
    $scope.clearSession = function() {
        $sessionStorage.$reset();
    }
  } ]);

  /* Username */

  app.controller('SessionCheckController', [ 'mainService', function(mainService) {
    var that = this;
    that.user = [];
    mainService.getUser().then(function(result){
      that.user = result;
    });
  }]);

  /* Header */
  app.controller('HeaderController', ['$scope','$location', function($scope, $location) {
    this.navbarCollapsed = true;
    $scope.showSearch = false;
    $scope.showSearchFocus = false;
    
    this.toggleSearch = function() {
        $scope.showSearch = !$scope.showSearch;
        $scope.showSearchFocus = !$scope.showSearchFocus; 
    }
  }]);
  
  app.controller('SidebarController',[ '$localStorage', '$scope', 'mainService', function($localStorage, $scope, mainService) {
      $scope.$storage = $localStorage;
      $scope.sidebar = [];
      mainService.getSidebar().then(function(result){
          $scope.sidebar = result.data;
      });
      
      this.canSee = function(storageVar) {
          if(storageVar === '')
              return true;
          else {
              return $scope.$storage[storageVar];
          }
      }
  }]);

})();
