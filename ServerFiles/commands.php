<?
if($_SERVER['REQUEST_METHOD'] == 'POST')
{
    if(isset($_POST['command']))
    {
        $command = $_POST['command'];
        $t = true;
        if($command == 0)
        {
            $t = file_put_contents('commands.txt', 'stop');
        }
        else if($command == 1)
        {
            file_put_contents('commands.txt', 'red');
        }
        else if($command == 2)
        {
            file_put_contents('commands.txt', 'green');
        }
        else if($command == 3)
        {
            file_put_contents('commands.txt', 'blue');
        }
        else if($command == 4)
        {
            file_put_contents('commands.txt', 'cancel');
        }
        else if($command == 5)
        {
            file_put_contents('commands.txt', 'spazz');
        }
        else
        {
            
        }
       echo $t;
    }
    else
    {
        http_response_code(400);
    }
}
else if($_SERVER['REQUEST_METHOD'] == 'GET')
{
    echo file_get_contents('commands.txt');
}
?>
