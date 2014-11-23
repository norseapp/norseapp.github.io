
<?php
 
/*
 * Following code will update a todo information
 * A product is identified by todo id (pid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['tid']) && isset($_POST['name']) && isset($_POST['location']) && isset($_POST['due_time']) && isset($_POST['due_date'])) {
 
    $tid = $_POST['tid'];
    $name = $_POST['name'];
    $location = $_POST['location'];
    $due_time = $_POST['due_time'];
    $due_date = $_POST['due_date'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched tid
    $result = mysql_query("UPDATE todo_list SET name = '$name', location = '$location', due_time = '$due_time', due_date = '$due_date' WHERE tid = $tid");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "todo successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>