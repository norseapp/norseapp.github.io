<?php
 
/*
 * Following code will get single todo details
 * A product is identified by todo id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["tid"])) {
    $tid = $_GET['tid'];
 
    // get a product from products table
    $result = mysql_query("SELECT *FROM todo_list WHERE tid = $tid");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $todos = array();
            $todos["tid"] = $result["tid"];
            $todos["name"] = $result["name"];
            $todos["location"] = $result["location"];
            $todos["due_time"] = $result["due_time"];
            $todos["due_date"] = $result["due_date"];
            $todos["created_at"] = $result["created_at"];
            $todos["updated_at"] = $result["updated_at"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["todo_list"] = array();
 
            array_push($response["todo_list"], $todos);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No todo found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No todo found";
 
        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>