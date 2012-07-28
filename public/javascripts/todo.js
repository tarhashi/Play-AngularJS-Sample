function TodoCtrl($scope) {
  jsRouter.controllers.Application.tasks().ajax({
    async: false,
    success: function(data) {
      $scope.todos = data;
    },
    error: function() {
      alert("error:tasks");
    }
  });
  $scope.addTodo = function() {
    jsRouter.controllers.Application.newTask().ajax({
      data: { label: $scope.todoText },
      success: function(data) {
        $scope.$apply(function(){
          $scope.todos.push(data);
          $scope.todoText = '';
        });
      },
      error: function() {
        alert("error:addTask");
      }
    });
  };
  
  $scope.changeStatus = function(id, done) {
    console.log(id);
    console.log(done);
    if (done) {
      jsRouter.controllers.Application.doneTask(id).ajax({});
    } else {
      jsRouter.controllers.Application.undoneTask(id).ajax({});
    }
  }
 
  $scope.remaining = function() {
    var count = 0;
    angular.forEach($scope.todos, function(todo) {
      count += todo.done ? 0 : 1;
    });
    return count;
  };
 
  $scope.archive = function() {
    var oldTodos = $scope.todos;
    $scope.todos = [];
    angular.forEach(oldTodos, function(todo) {
      if (!todo.done) $scope.todos.push(todo);
    });
  };
}
