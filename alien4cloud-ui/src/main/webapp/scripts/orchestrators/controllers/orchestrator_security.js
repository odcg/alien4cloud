define(function (require) {
  'use strict';

  var modules = require('modules');
  var states = require('states');
  var angular = require('angular');
  var _ = require('lodash');

  states.state('admin.orchestrators.details.security', {
    url: '/security',
    templateUrl: 'views/orchestrators/orchestrator_security.html',
    controller: 'OrchestratorArtifactsCtrl',
    menu: {
      id: 'menu.orchestrators.security',
      state: 'admin.orchestrators.details.security',
      key: 'ORCHESTRATORS.NAV.AUTHORIZATIONS',
      icon: 'fa fa-users',
      priority: 600
    }
  });

  modules.get('a4c-orchestrators').controller('OrchestratorArtifactsCtrl',
    ['$scope', '$modal', '$state',
    function($scope, $modal, $state) {
    }
  ]); // controller
}); // define