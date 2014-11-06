//Download AngularJS (http://angularjs.org/) - need angular.min.js
//Download Twitter Bootstrap (http://getbootstrap.com/) - need bootstrap.min.css

//<link rel="stylesheet" type="text/css" href="bootstrap.min.css"/>
//<script type="text/javascript" src="angular.min.js"></script>

//http://docs.angularjs.org/guide/expression

var myapp = angular.module('myapp', ['ngRoute']);

myapp.controller(
	'AppCtrl', 
	[
		'$scope', 
		'$log',
		function($scope, $log) {
			$scope.handleMyButton = function(){
				$log.log('Angular Button clicked');
			}
		}
	]
);

myapp.config(specifyRoutes);
function specifyRoutes ($routeProvider) {
	$routeProvider
		.when('#/login', {templateUrl: 'login.html' })
		.when('#/signup', {templateUrl: 'signup.html' });
}