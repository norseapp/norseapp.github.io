
<?php
 
/*
 * Following code will list all the todos
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// get all todos from todo_list table
$result = mysql_query("SELECT *FROM todo_list") or die(mysql_error());
 
// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // todo_list node
    $response["todo_list"] = array();
 
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $todos = array();
        $todos["tid"] = $row["tid"];
        $todos["name"] = $row["name"];
        $todos["location"] = $row["location"];
        $todos["due_time"] = $row["due_time"];
        $todos["due_date"] = $row["due_date"];
        $todos["created_at"] = $row["created_at"];
        $todos["updated_at"] = $row["updated_at"];
 
        // push single todo into final response array
        array_push($response["todo_list"], $todos);
    }
    // success
    $response["success"] = 1;
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no todos found
    $response["success"] = 0;
    $response["message"] = "No todo found";
 
    // echo no users JSON
    echo json_encode($response);
}
?>